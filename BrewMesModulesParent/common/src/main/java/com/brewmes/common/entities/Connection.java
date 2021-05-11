package com.brewmes.common.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;

@Document(collection = "connection")
public class Connection {
    @Id
    private String id;

    @NotNull
    private String ip;

    @NotNull
    private String name;

    private boolean autobrewing = false;

    public Connection() {
    }

    public Connection(String id, String ip, String name) {
        this.id = id;
        this.ip = ip;
        this.name = name;
    }

    public Connection(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean isAutobrewing() {
        return autobrewing;
    }

    public void setAutobrew(boolean autobrew) {
        this.autobrewing = autobrew;
    }
}
