package com.carecircle.user_profile_service.parent.dto;

public class CertificationResponse {

    private final String certificationName;
    private final String issuedBy;

    public CertificationResponse(String certificationName, String issuedBy) {
        this.certificationName = certificationName;
        this.issuedBy = issuedBy;
    }

	public String getCertificationName() {
		return certificationName;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

}
