package services;

import models.Restaurant;
import models.User;

import java.util.List;

public class RecommendationService {

    private final RestaurantService restaurantService;
    private final UserService userService;
    private final SearchService searchService;

    public RecommendationService(RestaurantService restaurantService,
                                 UserService userService,
                                 SearchService searchService) {
        this.restaurantService = restaurantService;
        this.userService       = userService;
        this.searchService     = searchService;
    }

    public List<Restaurant> getTopRecommendations(String userId, int limit) {
        User user = userService.getUserPreferences(userId);
        if (user == null) return List.of();
        return restaurantService.recommendForUser(user, limit);
    }

    public List<Restaurant> getRecommendationsByPreferences(
            String food, String budget, String environment,
            double minRating, String city, int limit) {

        User tempUser = new User("", "", "", city);
        tempUser.setFavoriteFood(food);
        tempUser.setBudget(budget);
        tempUser.setEnvironment(environment);
        tempUser.setMinRating(minRating);

        return restaurantService.recommendForUser(tempUser, limit);
    }
}
