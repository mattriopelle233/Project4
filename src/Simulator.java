package src;
import java.util.Scanner;

// Simulator to cycle for select number of days
public class Simulator implements SysOut {
    final int numDays;
    Enums.DayOfWeek dayOfWeek;
    FNCD fncd1;
    FNCD fncd2;
    Simulator() {
        numDays = 30;  // magic number for days to run here
        dayOfWeek = Utility.randomEnum(Enums.DayOfWeek.class);  // we'll start on a random day (for fun)
        fncd1 = new FNCD();
        fncd2 = new FNCD();
    }

    // cycling endlessly through enum values
    // https://stackoverflow.com/questions/34159413/java-get-next-enum-value-or-start-from-first
    public Enums.DayOfWeek getNextDay(Enums.DayOfWeek e)
    {
        int index = e.ordinal();
        int nextIndex = index + 1;
        Enums.DayOfWeek[] days = Enums.DayOfWeek.values();
        nextIndex %= days.length;
        return days[nextIndex];
    }

    public void runSimulations() {
        Thread thread1 = new Thread(() -> {
            long threadID = Thread.currentThread().getId();
            run(this.fncd1);
            System.out.println("Thread " + threadID + " finished.");
        });

        Thread thread2 = new Thread(() -> {
            long threadID = Thread.currentThread().getId();
            run(this.fncd2);
            System.out.println("Thread " + threadID + " finished.");
        });

        int input;
        Scanner scanner = new Scanner(System.in);
        FNCD chosenFncd;
        boolean stillRunning = true;

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }

        System.out.println();
        System.out.println("Welcome the the UI Based Buyer Interface!");
        System.out.println();

        Receiver receiver = new Receiver(this.fncd1,this.fncd2);

        BuyItem buyItem = new BuyItem(receiver);
        GetInventory getInventory = new GetInventory(receiver);
        GetItemDetails getItemDetails = new GetItemDetails(receiver);
        GetNewSalesperson getNewSalesperson = new GetNewSalesperson(receiver);
        GetSalespersonName getSalespersonName = new GetSalespersonName(receiver);
        GetTime getTime = new GetTime(receiver);
        SelectFNCD selectFNCD = new SelectFNCD(receiver);

        Invoker invoker = new Invoker();

        while(stillRunning){
            System.out.println("Select an action from the list below!");
            System.out.println();
            System.out.println("1: Select an FNCD to buy from (in the event of no selection, we will default to FNCD1)!");
            System.out.println("2: Ask your salesperson for their name!");
            System.out.println("3: Ask your salesperson for the current time!");
            System.out.println("4: Get a new salesperson!");
            System.out.println("5: Display the current inventory!");
            System.out.println("6: Display detailed information for an inventory item!");
            System.out.println("7: Enter the flow to buy a vehicle!");
            System.out.println("8: End your interaction!");
            System.out.println();
            try {
                input = scanner.nextInt();
                switch(input){
                    case 1:
                        invoker.setCommand(selectFNCD);
                        invoker.performCommand();
                        break;
                    case 2:
                        invoker.setCommand(getSalespersonName);
                        invoker.performCommand();
                        break;
                    case 3:
                        invoker.setCommand(getTime);
                        invoker.performCommand();
                        break;
                    case 4:
                        invoker.setCommand(getNewSalesperson);
                        invoker.performCommand();
                        break;
                    case 5:
                        invoker.setCommand(getInventory);
                        invoker.performCommand();
                        break;
                    case 6:
                        invoker.setCommand(getItemDetails);
                        invoker.performCommand();
                        break;
                    case 7:
                        invoker.setCommand(buyItem);
                        invoker.performCommand();
                        break;
                    case 8:
                        System.out.println("Thanks for buying with us!");
                        stillRunning = false;
                        break;
                    default:
                        System.out.println("Invalid Input, please try again!");
                        break;

                }
            }
            catch(Exception err){
                System.out.println(err);
            }
        }
    }
    void run(FNCD fncd) {
        Tracker tracker = Tracker.getInstance();
        Logger logger = Logger.getInstance();
        fncd.getEventPublisher().addSubscriber(tracker);
        for (int day = 1; day <= numDays; ++day) {
            logger.setDay(day);
            tracker.setDay(day);
            fncd.getEventPublisher().addSubscriber(logger);
            out(">>> Start Simulation Day "+day+" "+dayOfWeek);
            if (dayOfWeek == Enums.DayOfWeek.Sun || dayOfWeek == Enums.DayOfWeek.Wed) fncd.raceDay(dayOfWeek);
            else fncd.normalDay(dayOfWeek);  // normal stuff on other days
            out(">>> End Simulation Day "+day+" "+dayOfWeek+"\n");
            tracker.printSummary();
            dayOfWeek = getNextDay(dayOfWeek);  // increment to the next day
        }
    }
}
