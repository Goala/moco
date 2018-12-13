package topgrost.mocoquizer.lobby.model;

import java.io.Serializable;
import java.util.Objects;

public class Device implements Serializable {

    private String name;
    private String mac;
    private boolean available;

    public Device() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return available == device.available &&
                Objects.equals(name, device.name) &&
                Objects.equals(mac, device.mac);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, mac, available);
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", available=" + available +
                '}';
    }
}
