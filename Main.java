package dataset;

public class Main {

    public static void main(String[] args) {

        // URI de conexión al servidor Neo4j (protocolo bolt)
        String uri = "neo4j://127.0.0.1:7687";

        // Establecer las credenciales de autenticación
        String user = "neo4j";
        String password = "12345678";

        // Se usa try-with-resources para cerrar la conexión automáticamente
        try (Neo4jManager neo4j =
                     new Neo4jManager(uri, user, password)) {

            // Verificar que la conexión al servidor sea exitosa
            if (neo4j.testConnection()) {

                // Cargar el dataset en la base de datos
                neo4j.loadDataset();

                System.out.println("OK");
            }
            else {
                System.out.println("Error de conexión");
            }
        }
    }
}