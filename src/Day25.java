import org.jgrapht.Graph;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day25 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/25.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        Graph<String, DefaultWeightedEdge> graph  = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(": ");
            graph.addVertex(split[0]);
            for (String connection : split[1].split(" ")) {
                if (!graph.containsVertex(connection)) {
                    graph.addVertex(connection);
                }
                graph.addEdge(split[0], connection);
            }
        }
        StoerWagnerMinimumCut<String, DefaultWeightedEdge> minimumCut = new StoerWagnerMinimumCut<>(graph);
        int oneSide = minimumCut.minCut().size();
        System.out.printf("Part one: %d\n", oneSide * (graph.vertexSet().size() - oneSide));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }
}