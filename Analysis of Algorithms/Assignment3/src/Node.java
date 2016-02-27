import java.util.ArrayList;

/**
 * Author - Tyler Wilding
 * Description - Node class to hold the name of the course, and the courses that it is a prerequsite to.
 */
public class Node {

    public String course;
    public ArrayList<Node> prereq = new ArrayList<Node>();
    public int eligible = 0;

    public Node(String course) {

        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    /**
     * Adds the item to the prereq. arraylist
     * @param course Node to be added
     */
    public void addPrereq(Node course) {

        prereq.add(course);
    }

}
