package com.example.ea2soa.data;

public class SoaErrorMessage {

    private String msg;
    private Boolean status;

    public SoaErrorMessage(String msg, Boolean status) {
        this.msg = msg;
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
