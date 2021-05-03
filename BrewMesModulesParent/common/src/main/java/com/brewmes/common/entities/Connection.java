package com.brewmes.common.entities;

public class Connection {
    private int id;
    private String ip;
    private String name;

    public Connection(int id, String ip, String name) {
        this.id = id;
        this.ip = ip;
        this.name = name;
    }

    public Connection(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
