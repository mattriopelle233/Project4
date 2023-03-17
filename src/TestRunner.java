package src;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.IOException;

class LoggerTest {
    @Test
    public void testGetInstance() {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        assertSame(logger1, logger2);
    }

    @Test
    public void testSetDay() throws IOException {
        Logger logger = Logger.getInstance();
        logger.setDay(2);

        // Check that the day was set correctly
        assertEquals(2, logger.day);

        // Check that a new log file was created for day 2
        assertDoesNotThrow(() -> logger.close());    }

    @Test
    public void testReceiveEvent() throws IOException {
        Logger logger = Logger.getInstance();
        logger.setDay(1);

        // Write an event to the log file
        logger.receiveEvent("10.0 USD Staff", true);

        // Check that the event was written to the log file
        BufferedReader reader = new BufferedReader(new FileReader("src.Logger-1.txt"));
        assertEquals("10.0 USD Staff", reader.readLine());
        reader.close();
    }

    @Test
    public void testClose() throws IOException {
        Logger logger = Logger.getInstance();
        assertThrows(NullPointerException.class, () -> logger.close());

        logger.setDay(1);
        assertDoesNotThrow(() -> logger.close());
    }
}
class TrackerTest {
    Tracker tracker;

    @BeforeEach
    void setUp() {
        tracker = Tracker.getInstance();
    }

    @Test
    void testGetInstance() {
        tracker = Tracker.getInstance();
        assertSame(tracker, Tracker.getInstance(), "Tracker instances are not the same.");
    }

    @Test
    void testSetDay() {
        tracker = Tracker.getInstance();
        tracker.setDay(10);
        assertEquals(10, tracker.day, "Day is not set correctly.");
    }

    @Test
    void testReceiveEvent() {
        tracker = Tracker.getInstance();
        tracker.receiveEvent("50.0 USD Staff", true);
        tracker.receiveEvent("100.0 USD FNCD", true);
        assertEquals(50.0, tracker.getStaffMoney(), 0.001, "Money earned by Staff is not calculated correctly.");
        assertEquals(100.0, tracker.getFncdMoney(), 0.001, "Money earned by FNCD is not calculated correctly.");
    }

    @Test
    void testPrintSummary() {
        tracker = Tracker.getInstance();
        tracker.setDay(15);
        tracker.receiveEvent("20.0 USD Staff", true);
        tracker.receiveEvent("40.0 USD FNCD", true);
        assertDoesNotThrow(() -> tracker.printSummary(), "An unexpected exception was thrown.");
        tracker.receiveEvent("-20.0 USD Staff", true);
        tracker.receiveEvent("-40.0 USD FNCD", true);
    }
}

class VehicleTest {
    @Test
    void testRange(){
        //ensure that electric vehicles have the correct range
        ElectricCar v = new ElectricCar();
        int range = v.getRange();
        assertTrue(range <= 400 && range >= 60);
    }
    @Test
    void testCost(){
        //assure that the cost is accurately 1/2 expensive as price
        Pickup v = new Pickup();
        assertTrue(v.getCost() * 2 == v.getPrice());
    }
}

class BuyerTest {
    Buyer b = new Buyer();
    @Test
    void testBuyerType(){
        //assure that the buyer gets assigned a valid buyerType
        assertTrue(b.getBuyerType() == Enums.BuyerType.JustLooking || b.getBuyerType() == Enums.BuyerType.NeedsOne || b.getBuyerType() == Enums.BuyerType.WantsOne);
    }
    Buyer c = new Buyer();
    @Test
    void testUniqueNames(){
        //tests to make sure two names are not the same
        assertFalse(b.getName() == c.getName());
    }
}

class FNCDTest{
    FNCD f = new FNCD();
    @Test
    void testBudget(){
        //making sure our updated budget is correct
        assertTrue(f.getBudget() == 1500000);
    }
    @Test
    void testMoneyIn(){
        //asserting that our money in works correctly
        f.moneyIn(10);
        assertTrue(f.getBudget() == 1500000 + 10);
    }
    @Test
    void testHiringNewEmployees(){
        //asserting that the FNCD correctly hires the correct number of staff
        f.hireNewStaff();
        assertTrue(f.getStaffSize() == 12);
    }
}