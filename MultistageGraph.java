import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Edge {
    int start;
    int end;
    int weight;

    public Edge(int start, int end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }
}

class Graph {
    int numVertices;
    int numStages;
    int[][] cost;
    ArrayList<Edge> edges;

    public Graph(int numVertices, int numStages) {
        this.numVertices = numVertices;
        this.numStages = numStages;
        cost = new int[numVertices][numVertices];
        edges = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                cost[i][j] = (i == j) ? 0 : Integer.MAX_VALUE;
            }
        }
    }

    public void addEdge(int start, int end, int weight) {
        edges.add(new Edge(start, end, weight));
        cost[start][end] = weight;
    }
}

public class MultistageGraph extends JFrame {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private Graph graph;
    private int[] path;

    public MultistageGraph(Graph graph, int[] path) {
        this.graph = graph;
        this.path = path;
        setTitle("Multistage Graph");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new GraphPanel());
    }

    class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGraph(g);
        }

        private void drawGraph(Graphics g) {
            int radius = 20;

            // Draw vertices
            for (int i = 0; i < graph.numVertices; i++) {
                int x = (i + 1) * (WINDOW_WIDTH / (graph.numVertices + 1));
                int y = WINDOW_HEIGHT / 2;
                g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
            }

            // Draw edges
            g.setColor(Color.BLACK);
            for (Edge edge : graph.edges) {
                int x1 = (edge.start + 1) * (WINDOW_WIDTH / (graph.numVertices + 1));
                int y1 = WINDOW_HEIGHT / 2;
                int x2 = (edge.end + 1) * (WINDOW_WIDTH / (graph.numVertices + 1));
                int y2 = WINDOW_HEIGHT / 2;
                g.drawLine(x1, y1, x2, y2);
            }

            // Highlight minimum cost path
            g.setColor(Color.RED);
            for (int i = 0; path[i] != graph.numVertices - 1; i++) {
                int x1 = (path[i] + 1) * (WINDOW_WIDTH / (graph.numVertices + 1));
                int y1 = WINDOW_HEIGHT / 2;
                int x2 = (path[i + 1] + 1) * (WINDOW_WIDTH / (graph.numVertices + 1));
                int y2 = WINDOW_HEIGHT / 2;
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public static void findMinCostPath(Graph graph, int[] path) {
        int numVertices = graph.numVertices;
        int[] minCost = new int[numVertices];
        int[] nextVertex = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            minCost[i] = Integer.MAX_VALUE;
            nextVertex[i] = -1;
        }
        minCost[numVertices - 1] = 0;

        for (int i = numVertices - 2; i >= 0; i--) {
            for (int j = i + 1; j < numVertices; j++) {
                if (graph.cost[i][j] != Integer.MAX_VALUE && minCost[i] > graph.cost[i][j] + minCost[j]) {
                    minCost[i] = graph.cost[i][j] + minCost[j];
                    nextVertex[i] = j;
                }
            }
        }

        path[0] = 0;
        for (int i = 0; path[i] != numVertices - 1; i++) {
            path[i + 1] = nextVertex[path[i]];
        }
    }

    public static Graph getInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of vertices: ");
            int numVertices = scanner.nextInt();

            System.out.print("Enter the number of stages: ");
            int numStages = scanner.nextInt();

            Graph graph = new Graph(numVertices, numStages);

            System.out.print("Enter the number of edges: ");
            int numEdges = scanner.nextInt();

            for (int i = 0; i < numEdges; i++) {
                System.out.printf("Enter edge %d (start end weight): ", i + 1);
                int start = scanner.nextInt();
                int end = scanner.nextInt();
                int weight = scanner.nextInt();
                graph.addEdge(start, end, weight);
            }

            return graph;
        }
    }

    public static void main(String[] args) {
        Graph graph = getInput();
        int[] path = new int[graph.numVertices];
        findMinCostPath(graph, path);

        MultistageGraph frame = new MultistageGraph(graph, path);
        frame.setVisible(true);
    }
}
