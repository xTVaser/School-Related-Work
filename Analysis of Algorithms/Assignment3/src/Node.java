import java.util.ArrayList;

/**
 * Created by tyler on 26/02/16.
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

    public void setCourse(String course) {
        this.course = course;
    }

    public void addPrereq(Node course) {

        prereq.add(course);
    }

}
