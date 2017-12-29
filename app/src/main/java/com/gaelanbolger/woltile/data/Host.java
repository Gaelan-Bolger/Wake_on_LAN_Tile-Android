package com.gaelanbolger.woltile.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Host {

    public static final int DEFAULT_PORT = 9;

    @PrimaryKey
    private int id;
    private String name;
    private String icon;
    private String ip;
    private String mac;
    private int port = DEFAULT_PORT;

    public Host() {
    }

    @Ignore
    public Host(int id) {
        this.id = id;
    }

    @Ignore
    public Host(String name, String ip, String mac) {
        this.name = name;
        this.ip = ip;
        this.mac = mac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Host)) return false;
        Host host = (Host) o;
        return id == host.id &&
                port == host.port &&
                Objects.equals(name, host.name) &&
                Objects.equals(icon, host.icon) &&
                Objects.equals(ip, host.ip) &&
                Objects.equals(mac, host.mac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, icon, ip, mac, port);
    }

    @Override
    public String toString() {
        return "Host{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", port=" + port +
                '}';
    }
}
