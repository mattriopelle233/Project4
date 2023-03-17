package src;
import src.Staff;
import src.AddOn;
import src.EventPublisher;
import src.Vehicle;

import java.util.ArrayList;

// This represents the FNCD business and things they would control
public class FNCD implements SysOut {
    ArrayList<Staff> staff;  // folks working
    ArrayList<Staff> departedStaff;   // folks that left
    ArrayList<Vehicle> inventory;   // vehicles at the FNCD
    ArrayList<AddOn> addOns;   // addOn Purchases for vehicles
    ArrayList<Vehicle> soldVehicles;   // vehicles the buyers bought
    EventPublisher eventPublisher = new EventPublisher();
    VehicleFactory vfactory = new VehicleFactory();
    StaffFactory sfactory = new StaffFactory();
    private double budget;   // big money pile

    FNCD() {
        staff = new ArrayList<>();
        departedStaff = new ArrayList<>();
        inventory = new ArrayList<>();
        soldVehicles = new ArrayList<>();
        budget = 1500000;  // increased initial budget for massive increase in car inventory
        addOns = new ArrayList<>();
        addOns.add(new ExtendedWarranty());
        addOns.add(new RoadRescueCoverage());
        addOns.add(new SatelliteRadio());
        addOns.add(new Undercoating());
    }
    double getBudget() {
        return budget;    // I'm keeping this private to be on the safe side
    }
    void moneyIn(double cash) {  // Nothing special about taking money in yet
        eventPublisher.publishEvent(Double.toString(cash)+" : FNCD made money", true);
        budget += cash;
    }
    void moneyOut(double cash) {   // I check for budget overruns on every payout
        if(budget > 0){
            budget -= cash;
        }
        if (budget<=0) {
            budget += 250000;
            out("***Budget overrun*** Added $250K, budget now: " + Utility.asDollar(budget));
            eventPublisher.publishEvent("Adding money to the FNCD budget due to low funds", false);
        }
    }

    int getStaffSize(){
        return staff.size();
    }

    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }
    // Here's where I process daily activities
    // I debated about moving the individual activities out to an Activity class
    // It would make the normal day less of a monster maybe, eh...


    void raceDay(Enums.DayOfWeek day) {  // On a normal day, we do all the activities

        // opening
        out("The FNCD is opening...");
        hireNewStaff();    // hire up to 3 of each staff type
        updateInventory();  // buy up to 4 of each type

        // washing - tell the interns to do the washing up
        out("The FNCD interns are washing...");
        ArrayList<Staff> interns = Staff.getStaffByType(staff, Enums.StaffType.Intern);
        for (Staff s:interns) {
            Intern i = (Intern) s;
            i.washVehicles(inventory, eventPublisher);
        }

        // repairing - tell the mechanics to do their repairing
        out("The FNCD mechanics are repairing...");
        ArrayList<Staff> mechanics = Staff.getStaffByType(staff, Enums.StaffType.Mechanic);
        for (Staff s:mechanics) {
            Mechanic m = (Mechanic) s;
            m.repairVehicles(inventory, eventPublisher);
        }

        // selling
        out("The FNCD salespeople are selling...");
        ArrayList<Buyer> buyers = getBuyers(day);
        ArrayList<Staff> salespeople = Staff.getStaffByType(staff, Enums.StaffType.Salesperson);
        // tell a random salesperson to sell each buyer a car - may get bonus
        for(Buyer b: buyers) {
            out("Buyer "+b.name+" wants a "+b.preference+" ("+b.type+")");
            int randomSeller = Utility.rndFromRange(0,salespeople.size()-1);
            Salesperson seller = (Salesperson) salespeople.get(randomSeller);
            Vehicle vSold = seller.sellVehicle(b, inventory, addOns, eventPublisher);
            // What the FNCD needs to do if a car is sold - change budget and inventory
            if (vSold != null) {
                soldVehicles.add(vSold);
                moneyIn(vSold.price);
                inventory.removeIf ( n -> n.name == vSold.name);
            }
        }
        //racing
        race();
        // ending
        // pay all the staff their salaries
        payStaff();
        // anyone quitting? replace with an intern (if not an intern)
        checkForQuitters();
        // daily report
        reportOut();
    }

    void normalDay(Enums.DayOfWeek day) {  // On a normal day, we do all the activities
        // opening
        out("The FNCD is opening...");
        hireNewStaff();    // hire up to 3 of each staff type
        updateInventory();  // buy up to 4 of each type

        // washing - tell the interns to do the washing up
        out("The FNCD interns are washing...");
        ArrayList<Staff> interns = Staff.getStaffByType(staff, Enums.StaffType.Intern);
        for (Staff s:interns) {
            Intern i = (Intern) s;
            i.washVehicles(inventory, eventPublisher);
        }

        // repairing - tell the mechanics to do their repairing
        out("The FNCD mechanics are repairing...");
        ArrayList<Staff> mechanics = Staff.getStaffByType(staff, Enums.StaffType.Mechanic);
        for (Staff s:mechanics) {
            Mechanic m = (Mechanic) s;
            m.repairVehicles(inventory, eventPublisher);
        }

        // selling
        out("The FNCD salespeople are selling...");
        ArrayList<Buyer> buyers = getBuyers(day);
        ArrayList<Staff> salespeople = Staff.getStaffByType(staff, Enums.StaffType.Salesperson);
        // tell a random salesperson to sell each buyer a car - may get bonus
        for(Buyer b: buyers) {
            out("Buyer "+b.name+" wants a "+b.preference+" ("+b.type+")");
            int randomSeller = Utility.rndFromRange(0,salespeople.size()-1);
            Salesperson seller = (Salesperson) salespeople.get(randomSeller);
            Vehicle vSold = seller.sellVehicle(b, inventory, addOns, eventPublisher);
            // What the FNCD needs to do if a car is sold - change budget and inventory
            if (vSold != null) {
                soldVehicles.add(vSold);
                moneyIn(vSold.price);
                inventory.removeIf ( n -> n.name == vSold.name);
            }
        }

        // ending
        // pay all the staff their salaries
        payStaff();
        // anyone quitting? replace with an intern (if not an intern)
        checkForQuitters();
        // daily report
        reportOut();
    }

    // generate buyers
    ArrayList<Buyer> getBuyers(Enums.DayOfWeek day) {
        // 0 to 5 buyers arrive (2-8 on Fri/Sat)
        int buyerMin = 0;  //normal Mon-Thur
        int buyerMax = 5;
        if (day == Enums.DayOfWeek.Fri || day == Enums.DayOfWeek.Sat) {
            buyerMin = 2;
            buyerMax = 8;
        }
        ArrayList<Buyer> buyers = new ArrayList<Buyer>();
        int buyerCount = Utility.rndFromRange(buyerMin,buyerMax);
        for (int i=1; i<=buyerCount; ++i) buyers.add(new Buyer());
        out("The FNCD has "+buyerCount+" buyers today...");
        return buyers;
    }

    // see if we need any new hires
    void hireNewStaff() {
        final int numberInStaff = 3;
        for (Enums.StaffType t : Enums.StaffType.values()) {
            int typeInList = Staff.howManyStaffByType(staff, t);
            int need = numberInStaff - typeInList;
            for (int i = 1; i<=need; ++i) addStaff(t);
        }
    }

    // adding staff
    // smells like we need a factory or something...
    void addStaff(Enums.StaffType t) {
        Staff newStaff = null;
        newStaff = sfactory.createStaff(t);
        out("Hired a new "+newStaff.type+" named "+ newStaff.name);
        staff.add(newStaff);
    }

    // see if we need any vehicles
    void updateInventory() {
        final int numberInInventory = 6;
        for (Enums.VehicleType t : Enums.VehicleType.values()) {
            int typeInList = Vehicle.howManyVehiclesByType(inventory, t);
            int need = numberInInventory - typeInList;
            for (int i = 1; i<=need; ++i) addVehicle(t);
        }

    }

    // add a vehicle of a type to the inventory
    void addVehicle(Enums.VehicleType t) {
        Vehicle v = null;
        v = vfactory.createVehicle(t);
        moneyOut(v.cost);  // pay for the vehicle
        eventPublisher.publishEvent(Double.toString(-(v.cost))+" : FNCD spent on new car", true);
        out ("Bought "+v.name+", a "+v.cleanliness+" "+v.condition+" "+v.type+" for "+Utility.asDollar(v.cost));
        inventory.add(v);
    }

    // pay salary to staff and update days worked
    void payStaff() {
        for (Staff s: staff) {
            moneyOut(s.salary);  // money comes from the FNCD
            eventPublisher.publishEvent(Double.toString(s.salary)+" : Staff expense", true);
            s.salaryEarned += s.salary;  // they get paid
            s.daysWorked += 1; // they worked another day
        }
    }

    // Huh - no one wants to quit my FNCD!
    // I left this as an exercise to the reader...
    void checkForQuitters() {
        // I would check the percentages here
        // Move quitters to the departedStaff list
        // If an intern quits, you're good
        // If a mechanic or a salesperson quits
        // Remove an intern from the staff and use their properties to
        // create the new mechanic or salesperson
        int numquits = 0;
        for (Enums.StaffType t : Enums.StaffType.values()) {
            double chance = Utility.rnd();
            if(chance <= .1){
                numquits++;
                if(t == Enums.StaffType.Intern){
                    ArrayList<Staff> interns = Staff.getStaffByType(staff, Enums.StaffType.Intern);
                    int randomNum = Utility.rndFromRange(0, 2);
                    Intern quitter = (Intern) interns.get(randomNum);
                    out("The intern " + quitter.name + " quit the FNCD!");
                    departedStaff.add(quitter);
                    for(int i = 0; i < staff.size(); i++){
                        if(interns.get(randomNum).name == staff.get(i).name){
                            staff.remove(i);
                        }
                    }
                }
                else if(t == Enums.StaffType.Mechanic){
                    ArrayList<Staff> mechanics = Staff.getStaffByType(staff, Enums.StaffType.Mechanic);
                    int randomNum = Utility.rndFromRange(0, 2);
                    Staff quitter = (Mechanic) mechanics.get(randomNum);
                    out("The mechanic " + quitter.name + " quit the FNCD!");
                    departedStaff.add(quitter);
                    ArrayList<Staff> internstoHire = Staff.getStaffByType(staff, Enums.StaffType.Intern);
                    int randomNumm = Utility.rndFromRange(0, Staff.howManyStaffByType(staff, Enums.StaffType.Intern)-1);
                    Intern toPromote = (Intern) internstoHire.get(randomNumm);
                    out("The intern " + toPromote.name + " will be promoted to Mechanic!");
                    departedStaff.add(toPromote);
                    for(int i = 0; i < staff.size(); i++){
                        if(quitter.name == staff.get(i).name){
                            staff.remove(i);
                        }
                        if(internstoHire.get(randomNumm).name == staff.get(i).name){
                            staff.remove(i);
                        }
                    }
                    Staff newstaff = null;
                    newstaff = new Mechanic(toPromote.name); 
                    staff.add(newstaff);
                }

                else if(t == Enums.StaffType.Salesperson){
                    ArrayList<Staff> salespeople = Staff.getStaffByType(staff, Enums.StaffType.Salesperson);
                    int randomNum = Utility.rndFromRange(0, 2);
                    Staff quitter = (Salesperson) salespeople.get(randomNum);
                    out("The salesperson " + quitter.name + " quit the FNCD!");
                    departedStaff.add(quitter);
                    ArrayList<Staff> internstoHire = Staff.getStaffByType(staff, Enums.StaffType.Intern);
                    int randomNumm = Utility.rndFromRange(0, Staff.howManyStaffByType(staff, Enums.StaffType.Intern)-1);
                    Intern toPromote = (Intern) internstoHire.get(randomNumm);
                    out("The intern " + toPromote.name + " will be promoted to SalesPerson!");
                    departedStaff.add(toPromote);
                    for(int i = 0; i < staff.size(); i++){
                        if(quitter.name == staff.get(i).name){
                            staff.remove(i);
                        }
                        if(internstoHire.get(randomNumm).name == staff.get(i).name){
                            staff.remove(i);
                        }
                    }
                    Staff newstaff = null;
                    newstaff = new Salesperson(toPromote.name); 
                    staff.add(newstaff);
                }
            }
        }
        if(numquits == 0) out("No one quit today!");
    }

    void reportOut() {
        // We're all good here, how are you?
        // Quick little summary of happenings, you could do better

        out("Vehicles in inventory "+inventory.size());
        out("Vehicles sold count "+soldVehicles.size());
        out("Money in the budget "+ Utility.asDollar(getBudget()));
        printStaffSummary();
        out("That's it for the day.");
        for(int i = 0; i < staff.size(); i++){
            out("");
        }
    }

    void race(){
        //get drivers into arraylist
        ArrayList<Vehicle> toRace = new ArrayList<Vehicle>();
        ArrayList<Staff> drivers = Staff.getStaffByType(staff, Enums.StaffType.Driver);
        int carsPart = 0;
        //loop through vehicles, add to be raced if not broken, a car, or electric car
        for(Vehicle v: inventory){
            if(v.condition == Enums.Condition.Broken || v.type == Enums.VehicleType.Car || v.type == Enums.VehicleType.ElectricCar){
                continue;
            }
            toRace.add(v);
            //associate position 0 of arrayList drivers with position 0 of arraylist vehicles toRace
            out("The driver " + drivers.get(carsPart).name +" will be driving a " + v.type + " named " + v.name + " in the race!");
            carsPart++;
            if(carsPart > 2) break;
        }
        if(carsPart < 3) out("The FNCD is only able to put " + carsPart + " in the race.");
        //call the random placing method to determine placing
        ArrayList<Integer> placing = Utility.randomRacePlacing();
        //to access driver and corresponding vehicle
        int numDriving = 0;
        //loop through each vehicle to race
        for(Vehicle v: toRace){
            out(drivers.get(numDriving).name + " placed " + placing.get(numDriving) + " out of 20 driving the " + v.name);
            eventPublisher.publishEvent(drivers.get(numDriving).name + " placed " + placing.get(numDriving) + " out of 20 driving the " + v.name, false);
            //if they win, increment win count on vehicle and driver, and bonus, increase price of car
            if(placing.get(numDriving) <= 3){
                out(drivers.get(numDriving).name + " made podium and won!");
                eventPublisher.publishEvent(drivers.get(numDriving).name + " made podium and won!", false);
                v.incrWinCount();
                drivers.get(numDriving).incrWinCount();
                drivers.get(numDriving).bonusEarned += 200;
                out(drivers.get(numDriving).name + " earned a bonus of $200!");
                eventPublisher.publishEvent(drivers.get(numDriving).name + " earned a bonus of $200!", false);
                v.price = v.price * 1.1;
                out(v.name + " had it's price increased to " + v.price + " for placing podium");
            }
            //if they lose, car becomes broken, and driver has 30% chance of being injured
            if(placing.get(numDriving) > 15){
                out(drivers.get(numDriving).name + " placed bottom 5.");
                eventPublisher.publishEvent(drivers.get(numDriving).name + " placed bottom 5.", false);
                out(v.name + " has become broken as a result of poor placement in the race!");
                eventPublisher.publishEvent(v.name + " has become broken as a result of poor placement in the race!", false);
                v.condition = Enums.Condition.Broken;
                int chanceofInjury = Utility.rndFromRange(1, 10);
                if(chanceofInjury <= 3){
                    out(drivers.get(numDriving).name + " got injured, and is retired.");
                    departedStaff.add(drivers.get(numDriving));
                    staff.remove(drivers.get(numDriving));
                }
            }
            numDriving++;
        }
    }
    void printStaffSummary(){
        //long list of summary code
        ArrayList<Staff> mechanics = Staff.getStaffByType(staff, Enums.StaffType.Mechanic);
        ArrayList<Staff> interns = Staff.getStaffByType(staff, Enums.StaffType.Intern);
        ArrayList<Staff> salespeople = Staff.getStaffByType(staff, Enums.StaffType.Salesperson);
        ArrayList<Staff> drivers = Staff.getStaffByType(staff, Enums.StaffType.Driver);
        out("Current interns: ");
        for(int i = 0; i < interns.size(); i++){
            out(interns.get(i).name + "   salary: " + interns.get(i).salary + "   bonuses: " + interns.get(i).bonusEarned);
        }
        out("Current Mechanics: ");
        for(int i = 0; i < mechanics.size(); i++){
            out(mechanics.get(i).name + "   salary: " + mechanics.get(i).salary + "   bonuses: " + mechanics.get(i).bonusEarned);
        }
        out("Current SalesPeople: ");
        for(int i = 0; i < salespeople.size(); i++){
            out(salespeople.get(i).name+ "   salary: " + salespeople.get(i).salary + "   bonuses: " + salespeople.get(i).bonusEarned);
        }
        out("Current Drivers: ");
        for(int i = 0; i < drivers.size(); i++){
            out(drivers.get(i).name + "   win count: " + drivers.get(i).winCount + "   salary: " + drivers.get(i).salary + "   bonuses" + drivers.get(i).bonusEarned);
        }
        out("Current cars: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.Car)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
        out("Current Pickups: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.Pickup)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
        out("Current Performance Cars: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.PerfCar)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
        out("Current electric cars: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.ElectricCar)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
        out("Current motorcycles: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.MotorCycle)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
        out("Current monster trucks: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.MonsterTruck)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
        out("Current vespas: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.Vespa)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
        out("Current hybrid cars: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.HybridCar)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
        out("Current hydrogen cars: ");
        for(Vehicle v: Vehicle.getVehiclesByType(inventory, Enums.VehicleType.HydrogenCar)){
            out(v.name + "   sales price: " + v.price + "   cleanliness: " + v.cleanliness + "   condition: " + v.condition + "   win count: " + v.winCount);
        }
    }
}
