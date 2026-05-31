package algorithms;

import models.Restaurant;
import models.User;

public class CalculadoraDePeso {

    public int calculateAffinity(Restaurant restaurant, User user) {

        int weight = 0;

        // Comida favorita (category en Restaurant, favoriteFood en User)
        if (restaurant.getCategory() != null && user.getFavoriteFood() != null) {
            if (restaurant.getCategory().toLowerCase()
                    .contains(user.getFavoriteFood().toLowerCase())) {
                weight += 40;
            }
        }

        // Presupuesto: "bajo"=1, "medio"=2, "alto"=3 (price range de Neo4j es 1-4)
        int budgetLevel = budgetToLevel(user.getBudget());
        if (restaurant.getPrice() <= budgetLevel + 1) {
            weight += 25;
        }

        // Rating mínimo aceptable: si restaurante tiene ≥ 4.0 suma puntos
        if (restaurant.getRating() >= 4.0) {
            weight += 20;
        }

        // Ambiente
        if (restaurant.getEnvironment() != null && user.getEnvironment() != null) {
            if (restaurant.getEnvironment().equalsIgnoreCase(user.getEnvironment())) {
                weight += 15;
            }
        }

        return weight;
    }

    private int budgetToLevel(String budget) {
        if (budget == null) return 2;
        return switch (budget.toLowerCase()) {
            case "bajo"  -> 1;
            case "alto"  -> 3;
            default      -> 2; // medio
        };
    }
}
