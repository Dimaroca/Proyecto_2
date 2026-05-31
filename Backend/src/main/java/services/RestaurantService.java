package services;

import java.util.List;

import algorithms.CalculadoraDePeso;
import algorithms.Dijkstra;
import models.Restaurant;
import models.User;

public class RestaurantService {

    private CalculadoraDePeso calculator =
            new CalculadoraDePeso();

    private Dijkstra dijkstra =
            new Dijkstra();

    public List<Restaurant> recommendRestaurants( List<Restaurant> restaurants, User user) {
        int size = restaurants.size() + 1;

        int[][] graph = new int[size][size];

        // Nodo 0 = usuario

        for (int i = 1; i < size; i++) {

            Restaurant restaurant = restaurants.get(i - 1);

            // Calcular afinidad
            int affinity = calculator.calculateAffinity(restaurant,user);

            // Convertir afinidad a costo
            int cost = 100 - affinity;

            graph[0][i] = cost;
            graph[i][0] = cost;
        }

        // Ejecutar Dijkstra
        int[] distances = dijkstra.dijkstra(graph, 0);

        for (int i = 1; i < size; i++) {

            Restaurant restaurant = restaurants.get(i - 1);

            int affinity;

            if (distances[i] == Integer.MAX_VALUE) {
                affinity = 100;

            } else {

                affinity = 100 - distances[i];
            }

            restaurant.setWeight(affinity);

        }

        restaurants.sort((a,b) -> Integer.compare(b.getWeight(), a.getWeight()));

    return restaurants;
    }
}