import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { getMyChatRooms, initiateChat, getMessages, sendMessage, markAsRead } from "../api/chatApi";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import API_BASE_URL from "../api/api";

export default function ChatPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const { partnerId: urlPartnerId } = useParams(); // Optional URL param

    const [rooms, setRooms] = useState([]);
    const [activeRoom, setActiveRoom] = useState(null);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(true);
    const [stompClient, setStompClient] = useState(null);
    const messagesEndRef = useRef(null);
    const [isConnected, setIsConnected] = useState(false);

    const currentUser = localStorage.getItem("userId");

    // 1. Initialize & Socket Connection
    useEffect(() => {
        const fetchRooms = async () => {
            try {
                const myRooms = await getMyChatRooms();
                setRooms(myRooms);

                // Check for requested chat (from Booking or Admin)
                const state = location.state || {}; // { bookingId, partnerId }

                let targetPartnerId = urlPartnerId || state.partnerId;

                if (targetPartnerId) {
                    // Find existing room
                    const existing = myRooms.find(r => r.partnerId === targetPartnerId);
                    if (existing) {
                        setActiveRoom(existing);
                    } else if (state.bookingId || state.isAdminAction) {
                        // Create New if bookingId exists OR it's an admin action
                        try {
                            const res = await initiateChat(state.bookingId || null, targetPartnerId);
                            if (res.isNew) {
                                // Add to list and select (res.roomId)
                                const newRoom = {
                                    roomId: res.roomId,
                                    partnerId: targetPartnerId,
                                    partnerName: "New Chat", // Will refresh on reload
                                    unreadCount: 0,
                                    lastMessage: ""
                                };
                                setRooms(prev => [newRoom, ...prev]);
                                setActiveRoom(newRoom);
                            } else {
                                // Should have found it in list, but maybe race condition or paging
                                // Force fetch room details? Or just set active with basic info
                                setActiveRoom({ roomId: res.roomId, partnerId: targetPartnerId });
                            }
                        } catch (err) {
                            console.error("Failed to initiate chat", err);
                            alert("Could not start chat: " + err.message);
                        }
                    }
                }
            } catch (err) {
                console.error("Failed to load rooms", err);
            } finally {
                setLoading(false);
            }
        };

        fetchRooms();

        // WS Setup
        const socketUrl = "http://localhost:8080/ws"; // Gateway endpoint for WS

        const client = new Client({
            webSocketFactory: () => new SockJS(socketUrl),
            connectHeaders: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
                "X-User-Id": currentUser
            },
            onConnect: () => {
                console.log("Connected to WS");
                setIsConnected(true);
            },
            onDisconnect: () => {
                setIsConnected(false);
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            }
        });

        client.activate();
        setStompClient(client);

        return () => {
            if (client) client.deactivate();
        };
    }, [location.state, urlPartnerId, currentUser]); // Added dependencies to be safe, though [] is okay for init

    // 2. Room Selection & Message Loading
    useEffect(() => {
        if (!activeRoom) return;

        const loadMessages = async () => {
            try {
                const data = await getMessages(activeRoom.roomId);
                const msgs = data.content ? data.content : data;
                // Sort ascending for display
                const sorted = [...msgs].sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
                setMessages(sorted);

                // Mark as read
                if (activeRoom.unreadCount > 0) {
                    await markAsRead(activeRoom.roomId);
                    setRooms(prev => prev.map(r => r.roomId === activeRoom.roomId ? { ...r, unreadCount: 0 } : r));
                }
            } catch (err) {
                console.error("Failed to load messages", err);
            }
        };

        loadMessages();

        // Subscribe to Room Topic
        // Only verify connection using state
        if (stompClient && isConnected) {
            console.log("Subscribing to topic: /topic/chat/" + activeRoom.roomId);
            const sub = stompClient.subscribe(`/topic/chat/${activeRoom.roomId}`, (message) => {
                const msg = JSON.parse(message.body);
                setMessages(prev => {
                    // Dedup just in case
                    if (prev.some(p => p.id === msg.id)) return prev;
                    return [...prev, msg];
                });
            });
            return () => sub.unsubscribe();
        }
    }, [activeRoom, isConnected, stompClient]);

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    const handleSend = async (e) => {
        e.preventDefault();
        if (!input.trim() || !activeRoom) return;

        try {
            await sendMessage(activeRoom.roomId, input);
            setInput("");
            // Message will arrive via WS
        } catch (err) {
            alert("Failed to send message");
        }
    };

    if (loading) return <div>Loading Chat...</div>;

    return (
        <div className="flex h-screen bg-[#f5f5f7] pt-[80px] overflow-hidden">
            {/* Sidebar */}
            <div className="w-1/3 min-w-[300px] border-r border-gray-200 bg-white flex flex-col">
                <div className="p-4 border-b border-gray-200">
                    <h2 className="text-xl font-bold text-[#1d1d1f]">Messages</h2>
                </div>
                <div className="flex-1 overflow-y-auto">
                    {rooms.length === 0 && <div className="p-4 text-gray-500">No conversations yet.</div>}
                    {rooms.map(room => (
                        <div
                            key={room.roomId}
                            onClick={() => setActiveRoom(room)}
                            className={`p-4 border-b border-gray-100 cursor-pointer hover:bg-gray-50 flex justify-between items-center transition-colors ${activeRoom?.roomId === room.roomId ? 'bg-blue-50/50' : ''}`}
                        >
                            <div className="overflow-hidden">
                                <h4 className="font-bold text-[#1d1d1f] truncate">{room.partnerName || "Unknown"}</h4>
                                <p className="text-sm text-gray-500 truncate">{room.lastMessage || "No messages yet"}</p>
                            </div>
                            {room.unreadCount > 0 && (
                                <span className="bg-[#0071e3] text-white text-xs font-bold px-2 py-1 rounded-full">{room.unreadCount}</span>
                            )}
                        </div>
                    ))}
                </div>
            </div>

            {/* Chat Area */}
            <div className="flex-1 flex flex-col bg-white">
                {activeRoom ? (
                    <>
                        <div className="p-4 border-b border-gray-200 bg-white/80 backdrop-blur-md flex justify-between items-center">
                            <h3 className="font-bold text-lg text-[#1d1d1f]">{activeRoom.partnerName}</h3>
                        </div>

                        <div className="flex-1 overflow-y-auto p-6 space-y-4 bg-[#f5f5f7]">
                            {messages.map((msg, idx) => {
                                const isMe = String(msg.senderId).toLowerCase() === String(currentUser).toLowerCase();
                                return (
                                    <div key={idx} className={`flex ${isMe ? 'justify-end' : 'justify-start'}`}>
                                        <div className={`max-w-[70%] rounded-2xl px-4 py-2 shadow-sm ${isMe
                                            ? 'bg-[#0071e3] text-white rounded-br-none'
                                            : 'bg-white text-[#1d1d1f] rounded-bl-none'
                                            }`}>
                                            <p className="text-sm">{msg.content}</p>
                                            <span className={`text-[10px] block mt-1 ${isMe ? 'text-blue-100' : 'text-gray-400'}`}>
                                                {new Date(msg.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                            </span>
                                        </div>
                                    </div>
                                );
                            })}
                            <div ref={messagesEndRef} />
                        </div>

                        <div className="p-4 bg-white border-t border-gray-200">
                            <form onSubmit={handleSend} className="flex gap-2">
                                <input
                                    type="text"
                                    value={input}
                                    onChange={e => setInput(e.target.value)}
                                    placeholder="Type a message..."
                                    className="flex-1 border border-gray-300 rounded-full px-4 py-2 focus:outline-none focus:border-[#0071e3] focus:ring-1 focus:ring-[#0071e3]"
                                />
                                <button
                                    type="submit"
                                    className="bg-[#0071e3] text-white rounded-full p-2 w-10 h-10 flex items-center justify-center hover:bg-[#0077ED] transition-colors"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-5 h-5">
                                        <path strokeLinecap="round" strokeLinejoin="round" d="M6 12L3.269 3.126A59.768 59.768 0 0 1 21.485 12 59.77 59.77 0 0 1 3.27 20.876L5.999 12zm0 0h7.5" />
                                    </svg>
                                </button>
                            </form>
                        </div>
                    </>
                ) : (
                    <div className="flex-1 flex flex-col items-center justify-center text-gray-400">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1} stroke="currentColor" className="w-16 h-16 mb-4">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M8.625 12a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H8.25m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H12m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0h-.375M21 12c0 4.556-4.03 8.25-9 8.25a9.764 9.764 0 0 1-2.555-.337A5.972 5.972 0 0 1 5.41 20.97a5.969 5.969 0 0 1-.474-.065 4.48 4.48 0 0 0 .978-2.025c.09-.457-.133-.901-.467-1.226C3.93 16.178 3 14.189 3 12c0-4.556 4.03-8.25 9-8.25s9 3.694 9 8.25Z" />
                        </svg>
                        <p className="text-lg">Select a conversation or start a new chat.</p>
                    </div>
                )}
            </div>
        </div>
    );
}
