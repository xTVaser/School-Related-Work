import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Author - Tyler Wilding
 * Purpose - Networks Assignment
 *
 * NOTICE - Text file should be in the same directory as the code, this depends on your IDE configuration however.
 */
public class Algorithm {

    /**
     * All the things happen here.
     * @param args
     * @throws Exception All the bad things leave here.
     */
    public static void main(String[] args) throws Exception {

        //Read the entire file in at once and split on new line characters.
        String inputString = new String(Files.readAllBytes(Paths.get("input.txt")));
        String[] inputArray = inputString.split("\n");

        //Once we have that, we can now grab the number of nodes and the source node information.
        int numberOfNodes = Integer.parseInt(inputArray[0]);
        int startNode = 2;

        //Initialize an array to hold the nodes, we begin to create the adjacency list.
        Node[] nodes = new Node[numberOfNodes];
        for(int i = 0; i < nodes.length; i++) { //Initiallizing all the nodes so they are not null.
            nodes[i] = new Node("Node " + (i + 1));
        }

        //Pass the text file information to a method to make the code appear less terrible.
        getNodeInformation(inputArray, nodes);

        for(int i = 1; i < numberOfNodes+1; i++)
            allPairs(numberOfNodes, i, nodes);
    }

    public static void allPairs(int numberOfNodes, int startNode, Node[] nodes) {


        Node start = nodes[startNode-1]; //Minus one because we are using zero based indexes.

        //Call the algorithm, and hope it works.
        dkijstra(start);

        //Construct our forwarding table and string output.
        String[][] forwardingTable = new String[numberOfNodes+1][2];
        forwardingTable[0][0] = "NODE";
        forwardingTable[0][1] = "NEXT HOP";

        String fullRoutes = "---Full Complete Paths---\n";
        fullRoutes += "Source Node = "+nodes[startNode-1]+"\n";

        //Loop through each node, and call the method to find the shortest path for that particular node.
        for(int i = 0; i < nodes.length; i++) {

            if((i+1) == startNode)
                forwardingTable[i+1][0] = "*"+Integer.toString(i+1);
            else
                forwardingTable[i+1][0] = Integer.toString(i+1);
            ArrayList<Node> path = findShortestPath(nodes[i]);

            //Also output the full path of the graph because why not.
            fullRoutes += "Path to "+nodes[i]+": "+path+" with a distance of: ("+nodes[i].distance+")\n";

            //With this information, we can determine the next hop in the sequence, if it the node itself, just output --
            if((i+1) == startNode)
                forwardingTable[i+1][1] = "--";
            else
                forwardingTable[i+1][1] = path.get(1).nodeInfo;

        }

        //Now we can print out the forwarding table.
        for(int row = 0; row < forwardingTable.length; row++) {
            for(int col = 0; col < forwardingTable[row].length; col++) {
                System.out.printf("%15s",forwardingTable[row][col]);
            }
            System.out.println();
        }

        //And the full paths.
        System.out.println(fullRoutes);

        for(int i = 0; i < nodes.length; i++) {
            nodes[i].distance = Integer.MAX_VALUE;
            nodes[i].previousNode = null;
        }
    }

    /**
     * Collects the information from the input to determine the edges of the graph.
     * @param input String array that holds all text file infomration split on new lines.
     * @param nodes Node array that holds all of the references to the node objects.
     */
    public static void getNodeInformation(String[] input, Node[] nodes) throws Exception {

        //Start at the second index, because we have already dealt with the first two lines.
        for(int i = 1; i < input.length; i++) {

            String line = input[i];
            //Split the individual line of input into two parts.
            String firstSplit[] = line.split("[:;]");

            //The first index is the node we are referring to.
            int indexOfNode = Integer.parseInt(firstSplit[0]);

            //We can now loop through the rest as there may be several edges.
            //Each one is in the format, Adjacent node, weight, so we exploit that, and split the pair.
            for(int j = 1; j < firstSplit.length; j++) {

                String tempSplit[] = firstSplit[j].split("[,]");
                int newNodeIndex = Integer.parseInt(tempSplit[0]);
                int weight = Integer.parseInt(tempSplit[1]);
                //Subtract 1 because once again, zero based indexing.
                nodes[indexOfNode-1].adjNodes.add(new Edge(nodes[newNodeIndex-1], weight));
            }
        }
    }

    /**
     * Dkijstra's algorithm, calculates the minimal spanning tree of the graph, we can use that later.
     * @param start The node that we want to start the minimal spanning tree from.
     */
    public static void dkijstra(Node start) {

        //The distance of the start node is 0, because we start there, all others are infinite by default.
        start.distance = 0;

        //We need to use a priority queue to keep track of the unvisited node with the least distance.
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        //So add the start node, it is the only one we know about right now.
        pq.add(start);

        while(!pq.isEmpty()) { //Loop while the priority queue has items in it.

            Node top = pq.poll(); // Retrieves and removes the top of the queue.

            //Grab all the adjacent edges to the current node we are examining.
            ArrayList<Edge> adjNodes = top.adjNodes;
            //Loop through these adjacent edges.
            for(int i = 0; i < adjNodes.size(); i++) {

                //Make a temporary node, which is the endpoint of the edge we are examining.
                Node n = adjNodes.get(i).endPoint;
                //As well as, get that edge's weight.
                int weight = adjNodes.get(i).weight;

                //Calculate the distance from the current node, to the endpoint node over the weighted edge.
                int distanceFromTop = top.distance + weight;

                //If the distance is less than the distance currently stored in the node, (originally infinite), update it.
                if(distanceFromTop < n.distance) {
                    //Java's priority queue is bad and doesn't like an item's key being changed, so we have to remove it.
                    pq.remove(n);

                    //We can then update its distance, and set it's previous node for determining the path's later on.
                    n.distance = distanceFromTop;
                    n.previousNode = top;
                    pq.add(n);
                }
            }
        }
    }

    /**
     * Finally, after we have a completed algorithm, we can use the previous nodes to find the short paths to a targetnode.
     * @param target The node that we want to find the shortest path to.
     * @return An arraylist containing all of the nodes involved in the path.
     */
    public static ArrayList<Node> findShortestPath(Node target) {

        //Define an empty list.
        ArrayList<Node> path = new ArrayList<Node>();

        //We start at the target node.
        Node n = target;

        //Each node has a previous node field associated with it after the algorithm finishes, so work backwards from the target.
        while(n != null) { //Until n is null, aka we have hit the source node.
            //Add the target node to the path.
            path.add(n);
            //Move to the next previous, similar to linked list traversals. (It is one.)
            n = n.previousNode;
        }
        //Cool method to flip the list around, since we got it in reverse order.
        Collections.reverse(path);
        return path;
    }
}
