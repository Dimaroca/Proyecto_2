package models;
public class Restaurant {

    private String name;
    private String category;
    private double rating;
    private int price;
    private String zone;

    private int weight;

    public Restaurant(String name, String category, double rating, int price, String zone) {

        this.name = name;
        this.category = category;
        this.rating = rating;
        this.price = price;
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getRating() {
        return rating;
    }

    public int getPrice() {
        return price;
    }

    public String getZone() {
        return zone;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Restaurant{" + "name='" + name + '\'' + ", category='" + category + '\'' + ", rating=" + rating + ", price=" + price + ", zone='" + zone + '\'' + ", weight=" + weight +'}';
    }
}