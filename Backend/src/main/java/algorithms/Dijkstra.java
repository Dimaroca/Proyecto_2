package algorithms;

public class Dijkstra {

    private static final int INF = Integer.MAX_VALUE;

    public int[] dijkstra(int[][] graph,
                          int source) {

        int n = graph.length;

        int[] dist = new int[n];

        boolean[] visited = new boolean[n];

        // Inicializar distancias
        for (int i = 0; i < n; i++) {
            dist[i] = INF;
            visited[i] = false;
        }

        dist[source] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = minDistance(dist, visited);
            visited[u] = true;

            for (int v = 0; v < n; v++) {

                if (!visited[v] && graph[u][v] != 0 && dist[u] != INF && dist[u]+ graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }

        return dist;
    }

    private int minDistance( int[] dist, boolean[] visited) {

        int min = INF;
        int minIndex = -1;

        for (int v = 0; v < dist.length; v++) {

            if (!visited[v] && dist[v] <= min) {

                min = dist[v];
                minIndex = v;
            }
        }

        return minIndex;
    }
}