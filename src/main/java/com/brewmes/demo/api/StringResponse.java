package com.brewmes.demo.api;

//Class for wrapping string responses in a simple object associating the http response code.
public class StringResponse {

    private String response;
    private int status;

    public StringResponse(String response, int status) {
        this.response = response;
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
