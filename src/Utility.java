package src;

import java.text.NumberFormat;
import java.util.Random;
import java.util.ArrayList;
import java.util.*;

// Some static functions to call when you want things to be random or prettied
public interface Utility{

    // making this utility function that can be used by saying Utility.rndFromRange(min,max)
    // https://www.baeldung.com/java-generating-random-numbers-in-range
    static int rndFromRange(int min, int max) {
        //returns a uniform inclusive random number from a given min and max range
        return (int) ((Math.random() * ((max+1) - min)) + min);
    }

    static ArrayList<Integer> randomRacePlacing(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 1; i<=20; i++){
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }

    static ArrayList<Vehicle> sortForDirty(ArrayList<Vehicle> vList){
        //simple method to convert list into a priority queue(dirty vehicles first, then clean, then sparkling)
        //converts to priority queue, then back to list and returns
        Comparator<Vehicle> sorter = Comparator.comparing(Vehicle::getCleanliness);
        PriorityQueue<Vehicle> pq = new PriorityQueue<>(sorter);
        for(int i = 0; i< vList.size(); i++){
            pq.add(vList.get(i));
        }
        ArrayList<Vehicle> sortedAList = new ArrayList<Vehicle>();
        while(true){
            Vehicle v = pq.poll();
            if(v == null)break;
            sortedAList.add(v);
        }
        return sortedAList;
    }
    static double createSizeRating(){
        //generates a random sample from a normal distribution, mean 700, std dev 300
        //source: https://www.geeksforgeeks.org/random-nextgaussian-method-in-java-with-examples/
        Random rand = new Random();
        double valtoreturn = 0;
        while(valtoreturn < 50){
            valtoreturn = rand.nextGaussian(700, 300);
        }
        return valtoreturn;
    }

    // Short random 0-1 call
    static double rnd() {
        return Math.random();
    }

    // another utility for printing out pretty $ values
    // https://stackoverflow.com/questions/13791409/java-format-double-value-as-dollar-amount
    static String asDollar(double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(value);
    }

    // a utility for getting a random enum value from any enum - so nice
    // https://stackoverflow.com/questions/1972392/pick-a-random-value-from-an-enum
    // call like randomEnum(MyEnum.class)
    static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}