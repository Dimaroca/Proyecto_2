package algorithms;

import models.Restaurant;
import models.User;

public class CalculadoraDePeso {

    public int calculateAffinity(Restaurant restaurant, User user) {

        int weight = 0;

        // Tipo de comida
        if (restaurant.getCategory() != null && user.getFavoriteFood() != null) {
            if (restaurant.getCategory().toLowerCase()
                    .contains(user.getFavoriteFood().toLowerCase())) {
                weight += 40;
            }
        }

        // Presupuesto: "bajo"=1, "medio"=2, "alto"=3
        int budgetLevel = budgetToLevel(user.getBudget());
        if (restaurant.getPrice() <= budgetLevel + 1) {
            weight += 25;
        }

        // Rating
        if (restaurant.getRating()
        >= user.getMinRating()) {

            weight += 20;
        }

        // Ambiente
        if (restaurant.getEnvironment() != null && user.getEnvironment() != null) {
            if (restaurant.getEnvironment().equalsIgnoreCase(user.getEnvironment())) {
                weight += 15;
            }
        }

        if (user.getDistance() != null) {

            switch(user.getDistance()) {

                case "same":

                    if (restaurant.getCity()
                            .equalsIgnoreCase(
                                user.getCity())) {

                        weight += 10;
                    }

                    break;

                case "near":

                    weight += 5;
                    break;

                case "any":

                    weight += 10;
                    break;
            }
        }

        return weight;
    }

    private int budgetToLevel(String budget) {
        if (budget == null) return 2;
        return switch (budget.toLowerCase()) {
            case "bajo" -> 1;
            case "alto" -> 3;
            default     -> 2;
        };
    }
}

