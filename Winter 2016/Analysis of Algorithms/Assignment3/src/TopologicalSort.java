import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Author - Tyler Wilding
 * Description - Finds a topological sort of a list of courses given in a file.
 * Algorithm    - First step is to create a node for each course and add to the arraylist.
 *              - Then for each node in the arraylist, we need to find which courses it leads to.
 *                  - Go through each row, if the nodes item appears, find the node that it leads to and add it to it's list.
 */
public class TopologicalSort {

    public static void main(String[] args) throws Exception {

        ArrayList<Node> adjacencyList = generateGraph(new File("test1"));

        System.out.println(topologicalSort(adjacencyList)+"Finished");
    }

    /**
     * We start off by generating our basic adjacency list for the graph
     * @param file The file that holds all of the information
     * @return The completed adjacency list.
     * @throws Exception If the file isnt found
     */
    public static ArrayList<Node> generateGraph(File file) throws Exception {

        //First get the entire file as one larger string, and split it on new lines.
        String inputString = new String(Files.readAllBytes(file.toPath()));
        String[] lines = inputString.split("\\n+");

        ArrayList<Node> adjacencyList = new ArrayList<Node>();

        //Loop through each line of the file
        for (int i = 0; i < lines.length; i++) {

            //Split each line on a space, make new nodes all of the courses, these are the headers of the adjacency list
            String[] courses = lines[i].split("\\s+");

            Node newNode = new Node(courses[0]);
            adjacencyList.add(newNode);
        }

        //Since we are given the prequesites as a pseudo-reverse order than adjacencies, we have to do a bit more
        generatePrerequisites(adjacencyList, lines);
        return adjacencyList;
    }

    public static void generatePrerequisites(ArrayList<Node> courses, String[] info) {

        //Loop through each item in the arraylist
        //Loop through each course line
        //For each course line, loop through its items comparing them.

        //Loop through each course in the arraylist.
        for(int c = 0; c < courses.size(); c++) {

            //For each course, we will loop through each line of the file
            for (int l = 0; l < info.length; l++) {


                String[] courseInfo = info[l].split("\\s+");

                //And for each line of the file, we will examine it's prequesites.
                for (int i = 1; i < courseInfo.length; i++) {

                    //If we find the course that we are examining 'c', in a line of the file, than 'c' is a prequesite for the heading line
                    if (courseInfo[i].equalsIgnoreCase(courses.get(c).getCourse())) {
                        //Add the item to the nodes adjacency list, and increment its eligibility (requires 1 more course to take)
                        courses.get(c).addPrereq(findCourse(courses, courseInfo[0]));
                        findCourse(courses, courseInfo[0]).eligible++;
                    }
                }
            }
        }
    }

    /**
     * Simple method to return the node given a course name, as we want to use the same objects not create newones
     * @param courses ArrayList of nodes already created
     * @param courseCode String of the course code to find the node
     * @return The Node that matches the course code.
     */
    public static Node findCourse(ArrayList<Node> courses, String courseCode) {

        for(int i = 0; i < courses.size(); i++) {

            if(courses.get(i).getCourse().equalsIgnoreCase(courseCode))
                return courses.get(i);
        }
        return null;
    }

    /**
     * Main topological sort method to find a topological sort.
     * @param courses Adjacency list of the courses with each node containing the courses it is a prequesite for.
     * @return String representing the topological sort
     */
    public static String topologicalSort(ArrayList<Node> courses) {

        //Search for node with an empty prerequisite list.
        //Add the item to the topological sort
            //Remove the item from all prerequisite lists "we've done the course"
            //Remove the node from the list.

        String topSort = "";

        //Keep going until we have no courses left to take
        while(!courses.isEmpty()) {

            //Loop through the courses
            for(int i = 0; i < courses.size(); i++) {

                //If the course has all it's prequesites fulfilled
                if(courses.get(i).eligible == 0) {

                    //Update the eligbility of the courses it is a prequesite for (we've completed a prerequesite)
                    updatePrereq(courses.get(i));
                    //Add the item to the topological sort.
                    topSort += courses.get(i).course + " > ";
                    //Remove the node from the adjacencylist
                    courses.remove(courses.get(i));
                }
            }
        }

        return topSort;
    }

    /**
     * Simple method to update the eligiblity of all courses that a given course is a prequesite for.
     * @param node Node containing a list of courses it is a prequesite for.
     */
    public static void updatePrereq(Node node) {

        for(int i = 0; i < node.prereq.size(); i++) {

            node.prereq.get(i).eligible--;
        }
    }
}
