package models;
public class User {

    private String favoriteFood;
    private int budget;
    private double minimumRating;
    private String preferredZone;

    public User(String favoriteFood, int budget, double minimumRating, String preferredZone) {

        this.favoriteFood = favoriteFood;
        this.budget = budget;
        this.minimumRating = minimumRating;
        this.preferredZone = preferredZone;
    }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public int getBudget() {
        return budget;
    }

    public double getMinimumRating() {
        return minimumRating;
    }

    public String getPreferredZone() {
        return preferredZone;
    }
}