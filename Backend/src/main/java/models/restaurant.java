package models;

public class Restaurant {

    private String id;
    private String name;
    private String category;
    private double rating;
    private int price;
    private String zone;
    private String city;
    private String image;
    private String description;
    private String environment;
    private int score; // puntuación de afinidad calculada por Dijkstra

    public Restaurant(String id, String name, String category, double rating, int price, String zone, String city, String image, String description,String environment) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.price = price;
        this.zone = zone;
        this.city = city;
        this.image = image;
        this.description = description;
        this.environment = environment;
    }

    public String getId(){ 
        return id; }

    public String getName(){ 
        return name; }

    public String getCategory(){
         return category; }

    public double getRating(){
         return rating; }

    public int    getPrice(){
         return price; }

    public String getZone(){
         return zone; }

    public String getCity(){
         return city; }

    public String getImage(){
         return image; }

    public String getDescription(){
         return description; }

    public String getEnvironment(){
         return environment; }

    public int    getScore(){
         return score; }

    public void   setScore(int score){
         this.score = score; }
}
