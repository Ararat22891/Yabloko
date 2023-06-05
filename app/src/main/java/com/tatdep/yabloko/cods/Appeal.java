package com.tatdep.yabloko.cods;

import java.util.Objects;

public class Appeal {
    private String senderName;
    private String senderEmail;

    private String appealType;
    private String message;
    private String status;

    public Appeal() {
    }

    public Appeal(String senderName, String appealType , String senderEmail, String message, String status) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.message = message;
        this.status = status;
        this.appealType = appealType;
    }

    public String getAppealType() {
        return appealType;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getSenderEmail() {
        return senderEmail;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Appeal other = (Appeal) obj;
        return Objects.equals(senderName, other.senderName)
                && Objects.equals(senderEmail, other.senderEmail)
                && Objects.equals(appealType, other.appealType)
                && Objects.equals(message, other.message)
                && Objects.equals(status, other.status);
    }
}
