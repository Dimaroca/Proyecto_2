package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

public class DatabaseInitializer {

    private final Neo4jManager manager;

    public DatabaseInitializer(Neo4jManager manager) {
        this.manager = manager;
    }

    public int initializeIfEmpty() {
        if (hasRestaurants()) {
            System.out.println("[DatabaseInitializer] La base de datos ya contiene restaurantes. Se omite la carga.");
            return 0;
        }
        return loadFromCsv();
    }

    public int forceReload() {
        clearDatabase();
        return loadFromCsv();
    }

    private boolean hasRestaurants() {
        try (Session session = manager.openSession()) {
            var result = session.run("MATCH (r:Restaurant) RETURN COUNT(r) AS total");
            return result.single().get("total").asLong() > 0;
        } catch (Exception e) {
            System.out.println("[DatabaseInitializer] Error al verificar restaurantes: " + e.getMessage());
            return false;
        }
    }

    private void clearDatabase() {
        try (Session session = manager.openSession()) {
            session.run("MATCH (n) DETACH DELETE n");
            System.out.println("[DatabaseInitializer] Base de datos limpiada.");
        }
    }

    public int loadFromCsv() {
        List<String[]> rows = parseCsv();
        if (rows.isEmpty()) {
            System.out.println("[DatabaseInitializer] CSV vacío o no encontrado.");
            return 0;
        }

        int inserted = 0;
        try (Session session = manager.openSession()) {
            for (String[] row : rows) {
                try {
                    session.run(
                        """
                        MERGE (r:Restaurant {id: $id})
                        SET r.nombre      = $nombre,
                            r.ciudad      = $ciudad,
                            r.cocina      = $cocina,
                            r.rating      = $rating,
                            r.precio      = $precio,
                            r.zona        = $zona,
                            r.imagen      = '',
                            r.descripcion = '',
                            r.ambiente    = ''
                        """,
                        Values.parameters(
                            "id",     row[0],
                            "nombre", row[1],
                            "ciudad", row[2],
                            "cocina", row[3],
                            "rating", parseDouble(row[4]),
                            "precio", parseInt(row[5]),
                            "zona",   row[6]
                        )
                    );
                    inserted++;
                } catch (Exception e) {
                    System.out.println("[DatabaseInitializer] Fila ignorada: " + e.getMessage());
                }
            }
        }

        System.out.println("[DatabaseInitializer] " + inserted + " restaurantes insertados desde CSV.");
        return inserted;
    }

    private List<String[]> parseCsv() {
        List<String[]> rows = new ArrayList<>();

        InputStream is = getClass().getClassLoader().getResourceAsStream("restaurants.csv");
        if (is == null) {
            System.out.println("[DatabaseInitializer] No se encontró restaurants.csv en el classpath.");
            return rows;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String headerLine = br.readLine();
            if (headerLine == null) return rows;

            String[] headers = splitCsvLine(headerLine);
            int idxId      = findColumn(headers, "Restaurant ID");
            int idxName    = findColumn(headers, "Restaurant Name");
            int idxCity    = findColumn(headers, "City");
            int idxCuisine = findColumn(headers, "Cuisines");
            int idxRating  = findColumn(headers, "Aggregate rating");
            int idxPrice   = findColumn(headers, "Price range");
            int idxZone    = findColumn(headers, "Locality");

            String line;
            int limit = 500;
            int count = 0;

            while ((line = br.readLine()) != null && count < limit) {
                String[] cols = splitCsvLine(line);
                if (cols.length <= Math.max(idxId, Math.max(idxName, idxCity))) continue;
                rows.add(new String[]{
                    safe(cols, idxId),
                    safe(cols, idxName),
                    safe(cols, idxCity),
                    safe(cols, idxCuisine),
                    safe(cols, idxRating),
                    safe(cols, idxPrice),
                    safe(cols, idxZone)
                });
                count++;
            }
        } catch (IOException e) {
            System.out.println("[DatabaseInitializer] Error leyendo CSV: " + e.getMessage());
        }

        return rows;
    }

    private int findColumn(String[] headers, String name) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    private String safe(String[] cols, int idx) {
        if (idx < 0 || idx >= cols.length) return "";
        return cols[idx].trim();
    }

    private String[] splitCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 1; }
    }
}
