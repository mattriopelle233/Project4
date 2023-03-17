package src;

import src.AddOn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Vehicle {
    String name;
    Enums.VehicleType type;
    Enums.Condition condition;
    Enums.Cleanliness cleanliness;
    double cost;
    double price;
    double repair_bonus;
    double wash_bonus;
    double sale_bonus;
    int winCount;
    List<AddOn> addOns = new ArrayList<>();  // list of add-ons for the vehicle

    Vehicle () {
        // all vehicles have the same cleanliness arrival chance
        double chance = Utility.rnd();
        if (chance <= .05) cleanliness = Enums.Cleanliness.Sparkling;
        else if (chance>.05 && chance<=.4) cleanliness = Enums.Cleanliness.Clean;
        else cleanliness = Enums.Cleanliness.Dirty;
        // all vehicles have the same condition arrival chance (even chance of any)
        condition = Utility.randomEnum(Enums.Condition.class);
        winCount = 0;
    }

    void incrWinCount(){
        winCount++;
    }
    Enums.Cleanliness getCleanliness(){
        return this.cleanliness;
    }

    // utility for getting adjusted cost by condition
    double getCost(int low,int high) {
        double cost = Utility.rndFromRange(low, high);
        if (condition== Enums.Condition.Used) cost = cost*.8;
        if (condition== Enums.Condition.Broken) cost = cost*.5;
        return cost;
    }

    // utility for getting Vehicles by Type
    // You could do this with getClass instead of Type, but I use the enum
    // because it's clearer to me (less Java-y)
    static ArrayList<Vehicle> getVehiclesByType(ArrayList<Vehicle> vehicleList, Enums.VehicleType t) {
        ArrayList<Vehicle> subclassInstances = new ArrayList<>();
        for (Vehicle v : vehicleList) {
            if (v.type == t) subclassInstances.add(v);
        }
        return subclassInstances;
    }

    // Utility for finding out how many of a Vehicle there are
    static int howManyVehiclesByType(ArrayList<Vehicle> vehicleList, Enums.VehicleType t) {
        int n = 0;
        for (Vehicle v: vehicleList) {
            if (v.type == t) n++;
        }
        return n;
    }
    // add-on purchase methods
    public void addAddOn(AddOn addOn) {
        addOns.add(addOn);
        price += (addOn.getPriceIncrease()* price);
    }
}

class Car extends Vehicle {
    // could make the name list longer to avoid as many duplicates if you like...
    static List<String> names = Arrays.asList("Probe","Escort","Taurus","Fiesta");
    static Namer namer = new Namer(names);
    Car() {
        super();
        type = Enums.VehicleType.Car;
        name = namer.getNext();  // every new car gets a new name
        cost = getCost(10000,20000);
        price = cost * 2;
        repair_bonus = 100;
        wash_bonus = 20;
        sale_bonus = 500;
    }
}

class PerfCar extends Vehicle {
    static List<String> names = Arrays.asList("Europa","Cayman","Corvette","Mustang");
    static Namer namer = new Namer(names);
    PerfCar() {
        super();
        type = Enums.VehicleType.PerfCar;
        name = namer.getNext();  // every new perf car gets a unique new name
        cost = getCost(20000,40000);
        price = cost * 2;
        repair_bonus = 300;
        wash_bonus = 100;
        sale_bonus = 1000;
    }
}

class Pickup extends Vehicle {
    static List<String> names = Arrays.asList("Ranger","F-250","Colorado","Tundra");
    static Namer namer = new Namer(names);
    Pickup() {
        super();
        type = Enums.VehicleType.Pickup;
        name = namer.getNext();  // every new truck gets a unique new name
        cost = getCost(10000,40000);
        price = cost * 2;
        repair_bonus = 200;
        wash_bonus = 75;
        sale_bonus = 750;
    }
    double getPrice(){
        return this.price;
    }
    double getCost(){
        return this.cost;
    }
}

class ElectricCar extends Vehicle {
    static List<String> names = Arrays.asList("Prius","Tesla","Bolt","Mustang Mach");
    static Namer namer = new Namer(names);
    int range;
    ElectricCar() {
        super();
        type = Enums.VehicleType.ElectricCar;
        name = namer.getNext();  // every new electric car gets a unique new name
        cost = getCost(25000,50000);
        price = cost * 2;
        repair_bonus = 250;
        wash_bonus = 100;
        sale_bonus = 900;
        range = Utility.rndFromRange(60, 400);
        if(condition == Enums.Condition.LikeNew) range += 100; //increase range by 100 if car is Like New
    }

    int getRange(){
        return this.range;
    }
}


class MotorCycle extends Vehicle{
    static List<String> names = Arrays.asList("Harley Davidson","Yamaha","Suzuki","Ducati");
    static Namer namer = new Namer(names);
    double engineSizeRating;
    MotorCycle() {
        super();
        type = Enums.VehicleType.MotorCycle;
        name = namer.getNext();  // every new motorcycle gets a unique new name
        cost = getCost(15000,40000);
        price = cost * 2;
        repair_bonus = 175;
        wash_bonus = 50;
        sale_bonus = 600;
        //references the createSizeRating utility method we wrote
        engineSizeRating = Utility.createSizeRating();
    }
}

class MonsterTruck extends Vehicle{
    static List<String> names = Arrays.asList("Air Force Afterburner","Avenger","Batman","Bear Foot USA", "Bigfoot", "Bulldozer", "Cyborg", "El Toro Loco", "Gunslinger", "Predator", "Snake Bite", "The Destroyer","Zombie");
    static Namer namer = new Namer(names);
    MonsterTruck() {
        super();
        type = Enums.VehicleType.MonsterTruck;
        name = namer.getNext();  // every new monsterTruck gets a unique new name
        cost = getCost(20000,60000);
        price = cost * 2;
        repair_bonus = 250;
        wash_bonus = 100;
        sale_bonus = 875;
    }
}

class Vespa extends Vehicle{
    static List<String> names = Arrays.asList("Vespa PX","Vespa X","Vespa LX","Vespa GT", "Vespa GTS");
    static Namer namer = new Namer(names);
    Vespa() {
        super();
        type = Enums.VehicleType.Vespa;
        name = namer.getNext();  // every new Vespa gets a unique new name
        cost = getCost(5000,20000);
        price = cost * 2;
        repair_bonus = 50;
        wash_bonus = 30;
        sale_bonus = 100;
    }
}

class HydrogenCar extends Vehicle{
    static List<String> names = Arrays.asList("Toyota Mirai","Hyundai Nexo","Honda Clarity");
    static Namer namer = new Namer(names);
    HydrogenCar() {
        super();
        type = Enums.VehicleType.HydrogenCar;
        name = namer.getNext();  // every new Hydrogen Car gets a unique new name
        cost = getCost(40000,55000);
        price = cost * 2;
        repair_bonus = 250;
        wash_bonus = 100;
        sale_bonus = 875;
    }
}

class HybridCar extends Vehicle{
    static List<String> names = Arrays.asList("Hyundai Ioniq","Hyundai Sonata","Toyota Corolla","Honda Insight");
    static Namer namer = new Namer(names);
    HybridCar() {
        super();
        type = Enums.VehicleType.HybridCar;
        name = namer.getNext();  // every new motorcycle gets a unique new name
        cost = getCost(25000,33000);
        price = cost * 2;
        repair_bonus = 250;
        wash_bonus = 100;
        sale_bonus = 875;
    }
}
