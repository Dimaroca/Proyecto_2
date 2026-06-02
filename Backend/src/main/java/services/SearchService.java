package services;

import database.Neo4jManager;
import models.Restaurant;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {

    private final Neo4jManager neo4j;

    public SearchService(Neo4jManager neo4j) {
        this.neo4j = neo4j;
    }

    public List<Restaurant> searchByFood(String foodType) {
        List<Restaurant> all = neo4j.getAllRestaurants();
        if (foodType == null || foodType.isBlank()) return all;
        String lower = foodType.toLowerCase();
        return all.stream()
                  .filter(r -> r.getCategory() != null && r.getCategory().toLowerCase().contains(lower))
                  .collect(Collectors.toList());
    }

    public List<Restaurant> searchByMaxPrice(int maxPriceLevel) {
        List<Restaurant> all = neo4j.getAllRestaurants();
        return all.stream()
                  .filter(r -> r.getPrice() <= maxPriceLevel)
                  .collect(Collectors.toList());
    }

    public List<Restaurant> searchByBudget(String budget) {
        int level = switch (budget == null ? "" : budget.toLowerCase()) {
            case "bajo"  -> 1;
            case "medio" -> 2;
            case "alto"  -> 3;
            default      -> 4;
        };
        return searchByMaxPrice(level);
    }

    public List<Restaurant> searchByMinRating(double minRating) {
        List<Restaurant> all = neo4j.getAllRestaurants();
        return all.stream()
                  .filter(r -> r.getRating() >= minRating)
                  .collect(Collectors.toList());
    }

    public List<Restaurant> searchByZone(String zone) {
        List<Restaurant> all = neo4j.getAllRestaurants();
        if (zone == null || zone.isBlank()) return all;
        String lower = zone.toLowerCase();
        return all.stream()
                  .filter(r -> r.getZone() != null && r.getZone().toLowerCase().contains(lower))
                  .collect(Collectors.toList());
    }

    public List<Restaurant> searchCombined(String city, String foodType, String budget, Double minRating, String zone) {
        List<Restaurant> base = (city != null && !city.isBlank())
                ? neo4j.getRestaurantsByCity(city)
                : neo4j.getAllRestaurants();

        return base.stream()
                   .filter(r -> foodType == null || foodType.isBlank()
                             || (r.getCategory() != null && r.getCategory().toLowerCase().contains(foodType.toLowerCase())))
                   .filter(r -> budget == null || budget.isBlank()
                             || r.getPrice() <= budgetToLevel(budget))
                   .filter(r -> minRating == null || r.getRating() >= minRating)
                   .filter(r -> zone == null || zone.isBlank()
                             || (r.getZone() != null && r.getZone().toLowerCase().contains(zone.toLowerCase())))
                   .collect(Collectors.toList());
    }

    private int budgetToLevel(String budget) {
        return switch (budget.toLowerCase()) {
            case "bajo"  -> 1;
            case "medio" -> 2;
            case "alto"  -> 3;
            default      -> 4;
        };
    }
}
