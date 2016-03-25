import java.util.ArrayList;

/**
 * Author - Tyler Wilding
 *
 * Node class that is comparable, so we can use it in a priority queue.
 * Holds a name of the node for printing reasons, as well as a distance, list of edges, and a previous node reference.
 */
public class Node implements Comparable<Node> {

    String nodeInfo;
    int distance = Integer.MAX_VALUE;
    ArrayList<Edge> adjNodes = new ArrayList<Edge>();
    Node previousNode;

    /**
     * Constructor for a node, distance is default set to infinity, and previous node is indefined.
     * @param name The name of the node, ex. Node 1. as a String.
     */
    public Node(String name) {
        nodeInfo = name;
    }

    /**
     * Mostly for debugging purposes, just prints the name of the node.
     * @return
     */
    public String toString() {
        return nodeInfo;
    }

    /**
     * Compare two node's distance values, implemented for the priority queue.
     * @param n The other node we wnat to compare with
     * @return Returns an integer representing if it is too big or small.
     */
    public int compareTo(Node n) {
        return Integer.compare(distance, n.distance);
    }
}