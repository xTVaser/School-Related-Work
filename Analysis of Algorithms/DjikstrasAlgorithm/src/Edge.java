/**
 * Author - Tyler Wilding
 *
 * Edge class, has a weight, and the node it is connected to.
 */
public class Edge {

    Node endPoint;
    int weight;

    /**
     * Constructor for the Edge, must provide an end node, and the weight of the edge.
     * @param endPoint What node is the edge connected to.
     * @param weight The weight of the edge.
     */
    public Edge(Node endPoint, int weight) {
        this.endPoint = endPoint;
        this.weight = weight;
    }

    /**
     * Once again mostly for debugging purposes.
     * @return A string with the edge's endpoint and it's weight.
     */
    public String toString() {
        return endPoint.toString() + ", Weight:" + weight;
    }
}
