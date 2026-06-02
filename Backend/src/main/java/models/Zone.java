package models;

public class Zone {

    private final String name;
    private final String city;

    public Zone(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() { return name; }
    public String getCity() { return city; }

    public boolean isInCity(String cityName) {
        if (city == null || cityName == null) return false;
        return city.equalsIgnoreCase(cityName);
    }

    @Override
    public String toString() {
        return name + " (" + city + ")";
    }
}
