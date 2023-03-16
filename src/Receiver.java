package src;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Receiver {
    private FNCD fncd;
    private final FNCD fncd1;
    private final FNCD fncd2;
    private ArrayList<Staff> salespeople;
    private int currSalesmanIndex;

    public Receiver(FNCD fncd1, FNCD fncd2){
        this.fncd = fncd1;
        this.fncd1 = fncd1;
        this.fncd2 = fncd2;
        this.salespeople = Staff.getStaffByType(fncd.staff, Enums.StaffType.Salesperson);
        this.currSalesmanIndex = 0;
    }
    public void getSalespersonName(){
        System.out.println("Your current salesperson is: "+this.salespeople.get(this.currSalesmanIndex).name);
    }
    public void selectFNCD(){
        System.out.println("Input 1 to select fncd1 or 2 to select fncd2");
        Scanner scanner = new Scanner(System.in);
        try{
            int input = scanner.nextInt();
            if(input != 1 && input != 2){
                System.out.println("Input out of bounds");
            }
            else{
                if(input == 1){
                    this.fncd = this.fncd1;
                }
                else if(input == 2){
                    this.fncd = this.fncd2;
                }
            }
        }
        catch(Exception err){
            System.out.println(err);
        }
    }
    public void getTime(){
        System.out.println("Current Time: "+ LocalTime.now());
    }
    public void getNewSalesperson(){
        Random random = new Random();
        int numSalespeople = this.salespeople.size();
        this.currSalesmanIndex = random.nextInt(numSalespeople);
        System.out.println("Your new salesperson is: "+this.salespeople.get(this.currSalesmanIndex).name);
    }
    public void getInventory(){
        System.out.println("The inventory is as shown below!");
        System.out.println();
        String name;
        for(int i=0; i<this.fncd.inventory.size(); i++){
            name = this.fncd.inventory.get(i).name;
            System.out.println(i+": "+ name);
        }
        System.out.println();
    }
    public void getItemDetails(){
        int numItems = this.fncd.inventory.size();
        System.out.println("Enter a number >= 0 and <= "+(numItems-1)+" to select a vehicle");
        System.out.println("To see a list of vehicles, ask to be shown inventory on main menu.");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        try{
            int input = scanner.nextInt();
            if(input < 0 || input > numItems - 1){
                System.out.println("Input out of bounds");
            }
            else{
                System.out.println("Vehicle Name: "+this.fncd.inventory.get(input).name);
                System.out.println("Vehicle Cleanliness: "+this.fncd.inventory.get(input).cleanliness);
                System.out.println("Vehicle Condition: "+this.fncd.inventory.get(input).condition);
                System.out.println("Vehicle Price: "+this.fncd.inventory.get(input).price);
                System.out.println();
            }
        }
        catch(Exception err){
            System.out.println(err);
        }
    }
    public void buyItem(){
        int numItems = this.fncd.inventory.size();
        System.out.println("Enter a number >= 0 and <= "+(numItems-1)+" to select a vehicle");
        System.out.println("To see a list of vehicles, ask to be shown inventory on main menu.");
        System.out.println("To see vehicle details, ask to be shown vehicle details on main menu.");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        try{
            int input = scanner.nextInt();
            if(input < 0 || input > numItems - 1){
                System.out.println("Input out of bounds");
            }
            else{
                System.out.println("You selected vehicle: "+this.fncd.inventory.get(input).name);
                this.fncd.moneyIn(this.fncd.inventory.get(input).price);
                double bonus = this.fncd.inventory.get(input).sale_bonus;
                this.salespeople.get(currSalesmanIndex).bonusEarned += bonus;
                this.fncd.inventory.remove(input);
            }
        }
        catch(Exception err){
            System.out.println(err);
        }
    }
}
