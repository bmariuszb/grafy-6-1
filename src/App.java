import java.lang.reflect.Array;
import java.util.*;

public class App {
    public static void main(String [] args) {
        Graph graph = new Graph();
        System.out.println(graph);

        HashMap<Integer, Integer> visited = new HashMap<>();

        double d = 0.15;
        int vertex = 65;//start from A
        for(int i = 0; i < 1_000_000; i++) {//1_000_000
            if(Math.random() > 1.0 - d) {//teleport
                vertex = App.random(0, graph.getSize() - 1) + 65;
            } else {// go to neighbour
                vertex = graph.get(vertex - 65).get(App.random(0, graph.getSize(vertex - 65) - 1));
            }
            if(visited.containsKey(vertex)) {
                visited.put(vertex, visited.get(vertex) + 1);
            } else {
                visited.put(vertex, 1);
            }
        }

        List<Map.Entry<Integer, Integer>> pageRank = new ArrayList<>(visited.entrySet());
        pageRank.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        System.out.println("Metoda bladzenia przypadkowego");
        for(var e: pageRank) {
            System.out.println((char)((int) e.getKey()) + ": " + e.getValue()/1_000_000.0);
        }
        double P[][] = new double[graph.getSize()][graph.getSize()];
        for(int i = 0; i < graph.getSize(); i++) {
            for(int j = 0; j < graph.getSize(); j++) {
                P[i][j] = (1.0 - d) * graph.isAdjacent(i,j) / graph.stopien(i) + d / graph.getSize();
//                P[i][j] = (1.0 - d) * graph.isAdjacent(i,j) / graph.getSize(i) + d / graph.getSize();
            }
        }
        double p[] = new double[graph.getSize()];
        double pSum[] = new double[graph.getSize()];
        for(int i = 0; i < graph.getSize(); i++) {
            p[i] = 1.0/ graph.getSize();
            pSum[i] = p[i];
        }
        for(int i = 0; i < 1_000_000; i++) {
//            p = App.multiply(P, p);
            double[] p2 = new double[p.length];
            for(int j = 0; j < p.length; j++) {
                for(int k = 0; k < p.length; k++) {
                    p2[j] += P[j][k] * p[k];
                }
                    pSum[j] += p2[j];
            }
            p = p2;
        }
        System.out.println("Metoda iteracyjna");
//        ArrayList<> result = new ArrayList<>();

        for(int i = 0; i < graph.getSize(); i++) {
//            result.add()
//            System.out.println((char)(i+65) + ": " + pSum[i]/1_000_000);
            System.out.println((char)(i+65) + ": " + p[i]);
        }
    }

    private static double[] multiply(double[][] p, double[] p1) {
        double[] p2 = new double[p.length];
        for(int i = 0; i < p.length; i++) {
            for(int j = 0; j < p.length; j++) {
                p2[i] += p[i][j] * p1[j];
            }
        }
        return p2;
    }


    public static int random(int a, int b) {// range [a, b]
        return (int) (Math.random() * (b + 1 - a) + a);
    }
}

class Graph {
    private List<List<Character>> graph = new ArrayList<>();
    Graph() {
        int maxVertices = 'Z' - 'A';
        int minVertices = 1;// vertex 0 and 1
        int countVertices = App.random(minVertices, maxVertices);//[min, max]
        for(int i = 0; i <= countVertices; i++) {
            List<Character> set = new ArrayList<>();
//            amount of edges
            int countEdges = App.random(1, countVertices);
//            list of characters to choose
            List<Integer> leftChars = new ArrayList<>();
            for(int j = 0; j < i; j++) {
                leftChars.add(j);
            }
            for(int j = i+1; j <= countVertices; j++) {
                leftChars.add(j);
            }
//            drawing edges
            for(int j = 0; j < countEdges; j++) {
                Character c = (char)(leftChars.remove(App.random(0, leftChars.size()-1))+ 65);// [0 - 24]
                set.add(c);
            }
            graph.add(set);
        }
    }

    Graph(int minVertices) {
        int maxVertices = 'Z' - 'A';
        int countVertices = App.random(minVertices, maxVertices);
        for(int i = 0; i <= countVertices; i++) {
            List<Character> set = new ArrayList<>();
            int countEdges = App.random(1, countVertices);
            List<Integer> leftChars = new ArrayList<>();
            for(int j = 0; j < i; j++) {
                leftChars.add(j);
            }
            for(int j = i+1; j <= countVertices; j++) {
                leftChars.add(j);
            }
            for(int j = 0; j < countEdges; j++) {
                Character c = (char)(leftChars.remove(App.random(0, leftChars.size()-1))+ 65);
                set.add(c);
            }
            graph.add(set);
        }
    }

    public void setGraph(List<List<Character>> graph) {
        this.graph = graph;
    }

    public int getSize(int index) {
        return graph.get(index).size();
    }

    public int getSize() {
        return graph.size();
    }

    public List<Character> get(int index) {
        return graph.get(index);
    }

    public int stopien(int j) {
        int sum = 0;
        for(int i = 0; i < graph.size(); i++) {
            List<Character> list = graph.get(i);
            if (list.contains((char)(j+65))) {
                sum ++;
            }
        }
        return sum;
    }

    public int isAdjacent(int i, int j){
        List<Character> list = graph.get(i);
        if (list.contains((char)(j+65))) {
            return 1;
        }
        return 0;
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < graph.size(); i++) {
            res.append((char) (i + 65)).append(": ");
            res.append(graph.get(i)).append("\n");
        }
        return res.toString();
    }
}