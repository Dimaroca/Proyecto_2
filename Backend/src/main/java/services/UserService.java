package services;

import database.Neo4jManager;
import models.User;

public class UserService {

    private final Neo4jManager neo4j;

    public UserService(Neo4jManager neo4j) {
        this.neo4j = neo4j;
    }

    public User createUser(String name, String email, String password, String city) {
        if (name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            System.out.println("[UserService] Datos de usuario incompletos.");
            return null;
        }
        User user = new User(name, email, password, city);
        User created = neo4j.registerUser(user);
        if (created == null) System.out.println("[UserService] El email ya está registrado: " + email);
        return created;
    }

    public User login(String email, String password) {
        if (email == null || password == null) return null;
        User user = neo4j.loginUser(email, password);
        if (user == null) System.out.println("[UserService] Credenciales inválidas para: " + email);
        return user;
    }

    public User getUserPreferences(String userId) {
        if (userId == null || userId.isBlank()) return null;
        return neo4j.getUserById(userId);
    }

    public boolean updatePreferences(String userId, String favoriteFood, String budget, String environment, double minRating, String distance) {
        if (userId == null || userId.isBlank()) return false;
        return neo4j.savePreferences(userId, favoriteFood, budget, environment, minRating, distance);
    }

    public boolean updateReviewPreference(String userId, int rating) {
        if (userId == null || userId.isBlank()) return false;
        return neo4j.saveReviewPreference(userId, rating);
    }

    public boolean deleteUser(String userId) {
        if (userId == null || userId.isBlank()) return false;
        return neo4j.deleteUser(userId);
    }
}
