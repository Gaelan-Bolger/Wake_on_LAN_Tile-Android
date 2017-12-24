package com.gaelanbolger.woltile.data;

import java.util.Objects;

public class Host {

    private String name;
    private String address;
    private String mac;

    public Host(String name, String address, String mac) {
        this.name = name;
        this.address = address;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getMac() {
        return mac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Host)) return false;
        Host host = (Host) o;
        return Objects.equals(getName(), host.getName()) &&
                Objects.equals(getAddress(), host.getAddress()) &&
                Objects.equals(getMac(), host.getMac());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAddress(), getMac());
    }

    @Override
    public String toString() {
        return "Host{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }
}
