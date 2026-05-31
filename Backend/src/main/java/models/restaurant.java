package models;
public class Restaurant {

    private String id;
    private String name;
    private String category;
    private double rating;
    private String city;
    private String address;
    private int price;
    private String description;
    private int weight;

    public Restaurant(String id, String name, String category, double rating, String city, String address, int price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.city = city;
        this.address = address;
        this.price = price;
    }

    public String getID(){
        return id;
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

    public String getCity(){
        return city;
    }

    public int getPrice() {
        return price;
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