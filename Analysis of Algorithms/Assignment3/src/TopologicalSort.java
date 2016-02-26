import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Author - Tyler Wilding
 * Algorithm    - First step is to create a node for each course and add to the arraylist.
 *              - Then for each node in the arraylist, we need to find which courses it leads to.
 *                  - Go through each row, if the nodes item appears, find the node that it leads to and add it to it's list.
 */
public class TopologicalSort {

    public static void main(String[] args) throws Exception {


        ArrayList<Node> adjacencyList = generateGraph(new File("test1"));
        System.out.println(topologicalSort(adjacencyList)+"Finished");

    }

    public static ArrayList<Node> generateGraph(File file) throws Exception {

        String inputString = new String(Files.readAllBytes(file.toPath()));

        String[] lines = inputString.split("\\n+");

        ArrayList<Node> adjacencyList = new ArrayList<Node>();

        for (int i = 0; i < lines.length; i++) {

            String[] courses = lines[i].split("\\s+");

            Node newNode = new Node(courses[0]);

            adjacencyList.add(newNode);
        }

        generatePrerequisites(adjacencyList, lines);
        return adjacencyList;
    }

    public static void generatePrerequisites(ArrayList<Node> courses, String[] info) {

        //Loop through each item in the arraylist
        //Loop through each course line
        //For each course line, loop through its items comparing them.

        for(int c = 0; c < courses.size(); c++) {

            for (int l = 0; l < info.length; l++) {

                String[] courseInfo = info[l].split("\\s+");

                for (int i = 1; i < courseInfo.length; i++) {

                    //If the course is a prerequisite
                    if (courseInfo[i].equalsIgnoreCase(courses.get(c).getCourse())) {
                        courses.get(c).addPrereq(findCourse(courses, courseInfo[0]));
                        findCourse(courses, courseInfo[0]).eligible++;
                    }
                }
            }
        }
    }

    public static Node findCourse(ArrayList<Node> courses, String courseCode) {

        for(int i = 0; i < courses.size(); i++) {

            if(courses.get(i).getCourse().equalsIgnoreCase(courseCode))
                return courses.get(i);
        }
        return null;
    }

    public static String topologicalSort(ArrayList<Node> courses) {

        //Search for node with an empty prerequisite list.
        //Add the item to the topological sort
            //Remove the item from all prerequisite lists "we've done the course"
            //Remove the node from the list.

        String topSort = "";

        while(!courses.isEmpty()) {

            for(int i = 0; i < courses.size(); i++) {

                if(courses.get(i).eligible == 0) {

                    updatePrereq(courses.get(i));
                    topSort += courses.get(i).course + " > ";
                    courses.remove(courses.get(i));
                }
            }
        }

        return topSort;
    }

    public static void updatePrereq(Node node) {

        for(int i = 0; i < node.prereq.size(); i++) {

            node.prereq.get(i).eligible--;
        }
    }
}
