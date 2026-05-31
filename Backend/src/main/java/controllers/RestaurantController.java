package controllers;

import com.google.gson.Gson;
import database.Neo4jManager;

public class RestaurantController {

    public String getRestaurants(
            Neo4jManager neo4j){

        Gson gson = new Gson();

        return gson.toJson(
            neo4j.getRestaurants()
        );
    }
}