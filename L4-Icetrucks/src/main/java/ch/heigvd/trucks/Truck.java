package ch.heigvd.trucks;

import java.util.HashSet;
import java.util.Set;

public class Truck {
    private Integer id;

    private String name;

    private String driver;

    private Set<Integer> availableIceCreams = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Set<Integer> getAvailableIceCreams() {
        return availableIceCreams;
    }

    public void setAvailableIceCreams(Set<Integer> availableIceCreams) {
        this.availableIceCreams = availableIceCreams;
    }
}
