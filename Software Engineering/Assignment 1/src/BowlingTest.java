import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Author - Tyler Wilding
 * Description - Test class for bowling program.
 */
public class BowlingTest {

    @Test
    public void invalidRange() throws Exception {
        Bowling test = new Bowling();
        System.out.println("Testing Range!");
        assertEquals(false, test.checkUserInput("15"));
        assertEquals(false, test.checkUserInput("-15"));
        System.out.println();
    }

    @Test
    public void letters() throws Exception {
        Bowling test = new Bowling();
        System.out.println("Testing letter inputs!");
        assertEquals(false, test.checkUserInput("a"));
        assertEquals(false, test.checkUserInput("1a"));
        assertEquals(false, test.checkUserInput("a1"));
        System.out.println();
    }

    @Test
    public void score() throws Exception {
        Bowling test = new Bowling();
        System.out.println("Testing Score");
        assertEquals(true, test.updateScore(5, 0, 1));
        assertEquals(false, test.updateScore(6, 5, 1));
    }

    @Test
    public void perfectGame() throws Exception {

        System.out.println("Perfect Score");
        Bowling test = new Bowling();
        for(int i = 0; i < 9; i++)
            test.bowl("10");

        test.bowl("10","10","10");
        assertEquals(300, test.totalScore());

    }

    @Test
    public void allThree() throws Exception {

        System.out.println("All Three");
        Bowling test = new Bowling();
        for(int i = 0; i < 10; i++)
            test.bowl("3", "3");
        assertEquals(60, test.totalScore());

    }

    @Test
    public void oneSpare() throws Exception {

        System.out.println("One Spare");
        Bowling test = new Bowling();
        for(int i = 0; i < 10; i++) {
            if(i == 4)
                test.bowl("4", "6");
            else
                test.bowl("3", "3");
        }
        assertEquals(67, test.totalScore());

    }

    @Test
    public void twoSpare() throws Exception {

        System.out.println("Two Spares");
        Bowling test = new Bowling();
        for(int i = 0; i < 10; i++) {
            if(i == 4 || i == 5)
                test.bowl("4", "6");
            else
                test.bowl("3", "3");
        }
        assertEquals(75, test.totalScore());
    }

    @Test
    public void oneStrike() throws Exception {

        System.out.println("One Strike");
        Bowling test = new Bowling();
        for(int i = 0; i < 10; i++) {
            if(i == 4)
                test.bowl("10");
            else
                test.bowl("3", "3");
        }
        assertEquals(70, test.totalScore());
    }

    @Test
    public void twoStrikes() throws Exception {

        System.out.println("Two Strikes");
        Bowling test = new Bowling();
        for(int i = 0; i < 10; i++) {
            if(i == 4 || i == 5)
                test.bowl("10");
            else
                test.bowl("3", "3");
        }
        assertEquals(87, test.totalScore());
    }

    @Test
    public void oneStrikeTenth() throws Exception {

        System.out.println("One Strike in Tenth");
        Bowling test = new Bowling();
        for(int i = 0; i < 9; i++)
            test.bowl("3", "3");
        test.bowl("10","3","3");

        assertEquals(70, test.totalScore());
    }

    @Test
    public void threeStrikeTenth() throws Exception {

        System.out.println("Three Strikes in Tenth");
        Bowling test = new Bowling();
        for(int i = 0; i < 9; i++)
            test.bowl("3", "3");

        test.bowl("10","10","10");
        assertEquals(84, test.totalScore());

    }

    @Test
    public void allSpares() throws Exception {

        System.out.println("One Spare in Tenth");
        Bowling test = new Bowling();
        for(int i = 0; i < 9; i++) {
            test.bowl("3","7");
        }
        test.bowl("3","7","3");
        assertEquals(130, test.totalScore());

    }
}