package src;
//this is the publisher pattern!!
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
public class EventPublisher {
    private List<EventSubscriber> subscribers = new ArrayList<>();

    public void addSubscriber(EventSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    public void publishEvent(String event, boolean tracker) {
        for (EventSubscriber subscriber : subscribers) {
            subscriber.receiveEvent(event, tracker);
        }
    }
}

interface EventSubscriber {
    void receiveEvent(String event, boolean tracker);
}


//class Logger implements EventSubscriber {
//    private FileWriter fileWriter;
//    Logger(int day)  {
//        try {
//            fileWriter = new FileWriter("src.Logger-" + day + ".txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void receiveEvent(String event, boolean tracker) {
//        try {
//            fileWriter.write(event + "\n");
//            fileWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void close() {
//        try {
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//Lazy Instantiation Singleton

class Logger implements EventSubscriber {
    private static Logger instance = null;
    private FileWriter fileWriter;
    int day;
    private Logger() {

    }
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    public void setDay(int day) {
        if (instance == null) {
            instance = new Logger();
        }
        instance.day = day;
        try {
            fileWriter = new FileWriter("src.Logger-" + day + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveEvent(String event, boolean tracker) {
        try {
            fileWriter.write(event + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//Eager Instantiation Singleton
class Tracker implements EventSubscriber {
    private static final Tracker instance = new Tracker();
    private double staffMoney = 0.0;
    private double fncdMoney = 0.0;
    int day;

    private Tracker() {}

    public static Tracker getInstance() {
        return instance;
    }

    public void setDay(int day) {
        instance.day = day;
    }

    @Override
    public void receiveEvent(String event, boolean tracker) {
        if (tracker) {
            String[] result = event.split(" ");
            Double cash = Double.valueOf(result[0]);
            String party = result[2];

            if (party.equals("Staff")) {
                staffMoney += cash;
            }
            if (party.equals("FNCD")) {
                fncdMoney += cash;
            }
        }
    }

    public void printSummary() {
        System.out.println("Tracker: Day " + day);
        System.out.println("Total money earned by all Staff: $" + staffMoney);
        System.out.println("Total money earned by the FNCD: $" + fncdMoney);
    }
}