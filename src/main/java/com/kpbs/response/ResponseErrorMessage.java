package com.kpbs.response;

public class ResponseErrorMessage {
    private String status;
    private String message;

    public ResponseErrorMessage() { }

    public ResponseErrorMessage(String status) {
        this.status = status;
    }

    public ResponseErrorMessage(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
