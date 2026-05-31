package algorithms;

import models.Restaurant;
import models.User;

public class CalculadoraDePeso {

    public int calculateAffinity(Restaurant restaurant,User user) {

        int weight = 0;

        // Comida favorita
        if (restaurant.getCategory().equalsIgnoreCase(user.getFavoriteFood())) {
            weight += 40;
        }

        // Presupuesto
        if (restaurant.getPrice() <= user.getBudget()) {
            weight += 25;
        }

        // Rating
        if (restaurant.getRating() >= user.getMinimumRating()) {
            weight += 20;
        }

        // Zona
        if (restaurant.getZone() .equalsIgnoreCase(user.getPreferredZone())) {
            weight += 15;
        }

        return weight;
    }
}
