/**
 * Author - Tyler Wilding
 * Description - Small object to store the direction, mobility, and the item itself.
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
