package services;

import algorithms.CalculadoraDePeso;
import algorithms.Dijkstra;
import database.Neo4jManager;
import models.Restaurant;
import models.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantService {

    private final Neo4jManager neo4j;
    private final CalculadoraDePeso calculator = new CalculadoraDePeso();
    private final Dijkstra dijkstra = new Dijkstra();

    public RestaurantService(Neo4jManager neo4j) {
        this.neo4j = neo4j;
    }

    public RestaurantService() {
        this.neo4j = null;
    }

    public List<Restaurant> getAllRestaurants() {
        requireNeo4j();
        return neo4j.getAllRestaurants();
    }

    public List<Restaurant> getRestaurantsByCity(String city) {
        requireNeo4j();
        return neo4j.getRestaurantsByCity(city);
    }

    public Restaurant findRestaurant(String id) {
        requireNeo4j();
        return neo4j.getRestaurantById(id);
    }

    public boolean addRestaurant(Restaurant restaurant) {
        requireNeo4j();
        return neo4j.createRestaurant(restaurant);
    }

    public boolean deleteRestaurant(String id) {
        requireNeo4j();
        return neo4j.deleteRestaurant(id);
    }

    public List<String> listCities() {
        requireNeo4j();
        return neo4j.getCities();
    }

    public List<Restaurant> recommendRestaurants(List<Restaurant> restaurants, User user) {
        if (restaurants == null || restaurants.isEmpty()) return List.of();

        int size = restaurants.size() + 1;
        int[][] graph = new int[size][size];

        for (int i = 1; i < size; i++) {
            Restaurant r = restaurants.get(i - 1);
            int affinity = calculator.calculateAffinity(r, user);
            int cost = 100 - affinity;
            graph[0][i] = cost;
            graph[i][0] = cost;
        }

        int[] distances = dijkstra.dijkstra(graph, 0);

        for (int i = 1; i < size; i++) {
            Restaurant r = restaurants.get(i - 1);
            int affinity = (distances[i] == Integer.MAX_VALUE) ? 0 : 100 - distances[i];
            r.setScore(affinity);
        }

        restaurants.sort(Comparator.comparingInt(Restaurant::getScore).reversed());
        return restaurants;
    }

    public List<Restaurant> recommendForUser(User user, int limit) {
        requireNeo4j();
        List<Restaurant> all = (user.getCity() != null && !user.getCity().isBlank())
                ? neo4j.getRestaurantsByCity(user.getCity())
                : neo4j.getAllRestaurants();

        List<Restaurant> recommended = recommendRestaurants(all, user);
        if (limit > 0) return recommended.stream().limit(limit).collect(Collectors.toList());
        return recommended;
    }

    private void requireNeo4j() {
        if (neo4j == null) throw new IllegalStateException("RestaurantService instanciado sin Neo4jManager.");
    }
}
