package com.gaelanbolger.woltile.data;

import java.util.Objects;

public class Host {

    private static final String EMPTY = "";
    private static final int DEFAULT_PORT = 9;

    private String name;
    private String ip;
    private String mac;
    private int port;

    public Host(String name, String ip) {
        this(name, ip, EMPTY, DEFAULT_PORT);
    }

    public Host(String name, String ip, String mac) {
        this(name, ip, mac, DEFAULT_PORT);
    }

    public Host(String name, String ip, String mac, int port) {
        this.name = name;
        this.ip = ip;
        this.mac = mac;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Host)) return false;
        Host host = (Host) o;
        return getPort() == host.getPort() &&
                Objects.equals(getName(), host.getName()) &&
                Objects.equals(getIp(), host.getIp()) &&
                Objects.equals(getMac(), host.getMac());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIp(), getMac(), getPort());
    }

    @Override
    public String toString() {
        return "Host{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", port=" + port +
                '}';
    }
}
