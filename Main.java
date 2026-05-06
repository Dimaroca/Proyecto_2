package dataset;

/**
 * Main class of the program.
 * Initializes the connection with Neo4j
 * and loads the restaurant dataset.
 */
public class Main {

    /**
     * Main method of the program.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {

        String uri = "neo4j://127.0.0.1:7687";
        String user = "neo4j";
        String password = "12345678";

        try (Neo4jManager neo4j =
                     new Neo4jManager(uri, user, password)) {

            /*
             * Verifies if the connection to Neo4j
             * was established successfully.
             */
            if (neo4j.testConnection()) {

                /*
                 * Loads the dataset into the graph database.
                 */
                neo4j.loadDataset();

                System.out.println("OK");
            }

            /*
             * Displays an error message if the
             * connection to Neo4j fails.
             */
            else {

                System.out.println("Conexión fallida");

                System.out.println(
                        "Verifique que Neo4j esté encendido y que las credenciales sean correctas"
                );
            }
        }

        /*
         * Handles unexpected execution errors.
         */
        catch (Exception e) {

            System.out.println(
                    "Error inesperado: " + e.getMessage()
            );
        }
    }
}