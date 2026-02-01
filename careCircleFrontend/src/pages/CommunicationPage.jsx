import { useState, useEffect } from "react";
import { getNotifications, getRoomMessages, sendMessage } from "../api/communicationApi";

export default function CommunicationPage() {
    const [notifications, setNotifications] = useState([]);
    const [messages, setMessages] = useState([]);
    const [msgInput, setMsgInput] = useState("");
    const [activeRoom, setActiveRoom] = useState(null);

    useEffect(() => {
        const fetchStatus = async () => {
            setNotifications(await getNotifications() || []);
        };
        fetchStatus();
        const timer = setInterval(fetchStatus, 5000);
        return () => clearInterval(timer);
    }, []);

    const handleSend = async (e) => {
        e.preventDefault();
        if (!activeRoom || !msgInput) return;
        await sendMessage(activeRoom, msgInput);
        setMsgInput("");
        setMessages(await getRoomMessages(activeRoom) || []);
    };

    return (
        <div className="min-h-screen pt-32 pb-20 px-6 bg-slate-50">
            <div className="max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-12 gap-12">
                {/* Notifications */}
                <div className="lg:col-span-4 h-fit">
                    <h2 className="text-2xl font-black text-slate-900 mb-8 px-2">Inbox Alerts</h2>
                    <div className="grid gap-4">
                        {notifications.length > 0 ? notifications.map((n, i) => (
                            <div key={i} className="p-6 bg-white rounded-3xl border border-slate-100 shadow-sm hover:shadow-md transition-all">
                                <p className="font-bold text-slate-800 text-sm mb-2">{n.message}</p>
                                <p className="text-[10px] font-black text-slate-300 uppercase tracking-tighter">{n.createdAt}</p>
                            </div>
                        )) : <p className="p-6 text-slate-400 font-bold italic">Clear skies! No alerts.</p>}
                    </div>
                </div>

                {/* Messaging Area */}
                <div className="lg:col-span-8">
                    <h2 className="text-2xl font-black text-slate-900 mb-8 px-2">Active Chats</h2>
                    <div className="h-[650px] bg-white rounded-[3rem] shadow-2xl border border-slate-100 flex flex-col overflow-hidden">
                        <div className="flex-1 p-8 overflow-y-auto bg-slate-50/30 space-y-4">
                            {messages.length > 0 ? messages.map((m, i) => (
                                <div key={i} className={`flex ${m.sender === 'ME' ? 'justify-end' : 'justify-start'}`}>
                                    <div className={`max-w-[70%] p-5 rounded-[2rem] font-medium text-sm shadow-sm ${m.sender === 'ME' ? 'bg-indigo-600 text-white rounded-tr-none' : 'bg-white text-slate-800 border border-slate-100 rounded-tl-none'}`}>
                                        {m.content}
                                    </div>
                                </div>
                            )) : (
                                <div className="h-full flex flex-col items-center justify-center text-slate-300">
                                    <span className="text-6xl mb-6">💬</span>
                                    <p className="font-black h6">Select a conversation to begin</p>
                                </div>
                            )}
                        </div>
                        <form onSubmit={handleSend} className="p-8 bg-white border-t border-slate-50 flex gap-4">
                            <input value={msgInput} onChange={e => setMsgInput(e.target.value)} placeholder="Say something..." className="flex-1 p-5 rounded-2xl bg-slate-50 border border-slate-100 outline-none focus:border-indigo-500 transition-all font-medium" />
                            <button type="submit" className="px-10 py-5 bg-indigo-600 text-white rounded-2xl font-black shadow-xl shadow-indigo-100 hover:bg-indigo-700 transition-all">Send</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}
