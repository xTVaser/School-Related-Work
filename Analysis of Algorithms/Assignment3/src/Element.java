/**
 * Created by tyler on 26/02/16.
 */
public class Element {

    public int direction = -1; //-1 is left, 1 is right.
    public boolean mobile;
    public int item;

    public Element(int item) {
        this.item = item;
    }

    public String toString() {

        return Integer.toString(item);
    }
}
