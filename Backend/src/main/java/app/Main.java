package app;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import database.DatabaseInitializer;
import database.Neo4jManager;
import models.Restaurant;
import models.User;
import services.RestaurantService;
import services.SearchService;
import services.UserService;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {

    private static final Gson gson = new Gson();

    private static final String NEO4J_URI  = "bolt://neo4j:7687";
    private static final String NEO4J_USER = "neo4j";
    private static final String NEO4J_PASS = "12345678";

    public static void main(String[] args) {

        Neo4jManager neo4j = new Neo4jManager(NEO4J_URI, NEO4J_USER, NEO4J_PASS);
        /*try {

            Thread.sleep(30000);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }*/
        //neo4j.loadDataset();
        System.out.println("Dataset cargado");
        RestaurantService service = new RestaurantService();

        DatabaseInitializer initializer = new DatabaseInitializer(neo4j);
        int loaded = initializer.initializeIfEmpty();
        if (loaded > 0) System.out.println("Dataset cargado: " + loaded + " restaurantes.");
        else            System.out.println("Dataset ya existente.");

        UserService       userService       = new UserService(neo4j);
        RestaurantService restaurantService = new RestaurantService(neo4j);
        SearchService     searchService     = new SearchService(neo4j);

        port(4567);

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin",  "*");
            res.header("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
            res.type("application/json");
        });
        options("/*", (req, res) -> { res.status(200); return "OK"; });

        post("/api/register", (req, res) -> {
            JsonObject body = gson.fromJson(req.body(), JsonObject.class);
            String name     = body.get("name").getAsString();
            String email    = body.get("email").getAsString();
            String password = body.get("password").getAsString();
            String city     = body.get("city").getAsString();

            User newUser = userService.createUser(name, email, password, city);
            if (newUser == null) {
                res.status(409);
                return gson.toJson(Map.of("error", "El correo ya está registrado."));
            }
            return gson.toJson(Map.of("message", "Registro exitoso.", "userId", newUser.getId(), "name", newUser.getName()));
        });

        post("/api/login", (req, res) -> {
            JsonObject body = gson.fromJson(req.body(), JsonObject.class);
            String email    = body.get("email").getAsString();
            String password = body.get("password").getAsString();

            User user = userService.login(email, password);
            if (user == null) {
                res.status(401);
                return gson.toJson(Map.of("error", "Credenciales incorrectas."));
            }
            return gson.toJson(Map.of("message", "Login exitoso.", "userId", user.getId(), "name", user.getName(), "city", user.getCity()));
        });

        post("/api/preferencias", (req, res) -> {
            JsonObject body    = gson.fromJson(req.body(), JsonObject.class);
            String userId      = body.get("userId").getAsString();
            String food        = body.get("food").getAsString();
            String budget      = body.get("budget").getAsString();
            String environment = body.get("environment").getAsString();
            double minRating   = body.get("minRating").getAsDouble();
            String distance    = body.get("distance").getAsString();

            boolean ok = userService.updatePreferences(userId, food, budget, environment, minRating, distance);
            if (!ok) { res.status(500); return gson.toJson(Map.of("error", "No se pudieron guardar las preferencias.")); }
            return gson.toJson(Map.of("message", "Preferencias guardadas."));
        });

        post("/api/review", (req, res) -> {
            JsonObject body = gson.fromJson(req.body(), JsonObject.class);
            String userId   = body.get("userId").getAsString();
            int    rating   = body.get("rating").getAsInt();

            boolean ok = userService.updateReviewPreference(userId, rating);
            if (!ok) { res.status(500); return gson.toJson(Map.of("error", "No se pudo guardar")); }
            return gson.toJson(Map.of("message", "Review guardada"));
        });

        delete("/api/usuario/:id", (req, res) -> {
            String id = req.params(":id");
            boolean ok = userService.deleteUser(id);
            if (!ok) { res.status(404); return gson.toJson(Map.of("error", "Usuario no encontrado.")); }
            return gson.toJson(Map.of("message", "Usuario eliminado."));
        });

        get("/api/restaurantes", (req, res) -> {
            String city = req.queryParams("city");
            List<Restaurant> restaurants = (city != null && !city.isBlank())
                    ? restaurantService.getRestaurantsByCity(city)
                    : restaurantService.getAllRestaurants();
            return gson.toJson(restaurants);
        });

        get("/api/restaurante/:id", (req, res) -> {
            String id = req.params(":id");
            Restaurant r = restaurantService.findRestaurant(id);
            if (r == null) { res.status(404); return gson.toJson(Map.of("error", "Restaurante no encontrado")); }
            return gson.toJson(r);
        });

        delete("/api/restaurante/:id", (req, res) -> {
            String id = req.params(":id");
            boolean ok = restaurantService.deleteRestaurant(id);
            if (!ok) { res.status(404); return gson.toJson(Map.of("error", "No se pudo eliminar.")); }
            return gson.toJson(Map.of("message", "Restaurante eliminado."));
        });

        get("/api/ciudades", (req, res) -> gson.toJson(restaurantService.listCities()));

        get("/api/buscar", (req, res) -> {
            String city      = req.queryParams("city");
            String food      = req.queryParams("food");
            String budget    = req.queryParams("budget");
            String ratingStr = req.queryParams("minRating");
            String zone      = req.queryParams("zone");
            Double minRating = ratingStr != null ? Double.parseDouble(ratingStr) : null;

            List<Restaurant> results = searchService.searchCombined(city, food, budget, minRating, zone);
            return gson.toJson(results);
        });

        post("/api/recomendaciones", (req, res) -> {
            JsonObject body = gson.fromJson(req.body(), JsonObject.class);

            User user = new User("", "", "", body.has("city") ? body.get("city").getAsString() : "");
            user.setFavoriteFood(body.get("food").getAsString());
            user.setBudget(body.get("budget").getAsString());
            user.setEnvironment(body.get("environment").getAsString());

            List<Restaurant> all = restaurantService.getAllRestaurants();
            List<Restaurant> recommended = restaurantService.recommendRestaurants(all, user);
            return gson.toJson(recommended.stream().limit(10).toList());
        });

        System.out.println("API corriendo en http://localhost:4567");
    }
}
