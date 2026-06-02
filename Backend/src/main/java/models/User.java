package models;

public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private String city;
    private String favoriteFood;
    private String budget;
    private String environment;
    private double minRating;
    private String distance;
    private int reviewPreference;

    public User(String name, String email, String password, String city) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
    }

    public String getId()                  
     { return id; }
    public void setId(String id)          
    { this.id = id; }
    public String getName()                 
    { return name; }
    public String getEmail()                
    { return email; }
    public String getPassword()             
    { return password; }
    public String getCity()                 
    { return city; }
    public String getFavoriteFood()         
    { return favoriteFood; }
    public void setFavoriteFood(String f) 
    { this.favoriteFood = f; }
    public String getBudget()               
    { return budget; }
    public void setBudget(String b)       
    { this.budget = b; }
    public String getEnvironment()          
    { return environment; }
    public void setEnvironment(String e)  
    { this.environment = e; }
    public double getMinRating() 
    {return minRating;}
    public void setMinRating(double minRating) 
    {this.minRating = minRating;}
    public String getDistance() 
    {return distance;}
    public void setDistance(String distance) 
    {this.distance = distance;}
    public int getReviewPreference() 
    {return reviewPreference;}
    public void setReviewPreference(int reviewPreference) 
    {this.reviewPreference = reviewPreference;}
}