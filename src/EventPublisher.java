package src;
//this is the publisher pattern!!
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
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


class Logger implements EventSubscriber {
    private FileWriter fileWriter;
    Logger(int day)  {
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

class Tracker implements EventSubscriber {
    private double staffMoney = 0.0;
    private double fncdMoney = 0.0;
    @Override
    public void receiveEvent(String event, boolean tracker) {
        if(tracker){
            String [] result = event.split(" ");
            Double cash = Double.valueOf(result[0]);
            String party = result[2];

            if (party.equals("Staff")) {
                staffMoney += cash;
            }
            if(party.equals("FNCD")) {
                fncdMoney += cash;
            }
        }
    }

    public void printSummary(int day) {
        System.out.println("src.Tracker: Day " + day);
        System.out.println("Total money earned by all Staff: $" + staffMoney);
        System.out.println("Total money earned by the FNCD: $" + fncdMoney);
    }
}
