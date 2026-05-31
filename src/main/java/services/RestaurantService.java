package services;

import algorithms.CalculadoraDePeso;
import algorithms.Dijkstra;
import models.Restaurant;
import models.User;

import java.util.Comparator;
import java.util.List;

public class RestaurantService {

    private final CalculadoraDePeso calculator = new CalculadoraDePeso();
    private final Dijkstra dijkstra = new Dijkstra();


    public List<Restaurant> recommendRestaurants(List<Restaurant> restaurants, User user) {

        int size = restaurants.size() + 1;
        int[][] graph = new int[size][size];

        // Nodo 0 = usuario; nodos n = restaurantes
        for (int i = 1; i < size; i++) {
            Restaurant r = restaurants.get(i - 1);
            int affinity = calculator.calculateAffinity(r, user);
            int cost = 100 - affinity;
            graph[0][i] = cost;
            graph[i][0] = cost;
        }

        int[] distances = dijkstra.dijkstra(graph, 0);

        // Asociar puntuación a cada restaurante y ordenar
        for (int i = 1; i < size; i++) {
            Restaurant r = restaurants.get(i - 1);
            int affinity = (distances[i] == Integer.MAX_VALUE)
                    ? 0
                    : 100 - distances[i];
            r.setScore(affinity);
        }

        restaurants.sort(Comparator.comparingInt(Restaurant::getScore).reversed());
        return restaurants;
    }
}