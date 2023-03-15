package src;

public class StaffFactory implements SysOut{
    public Staff createStaff(Enums.StaffType t){
        Staff s = null;
        if(t == Enums.StaffType.Intern){
            s = new Intern();
            return s;
        }
        if(t == Enums.StaffType.Mechanic){
            s = new Mechanic();
            return s;
        }
        if(t == Enums.StaffType.Salesperson){
            s = new Salesperson();
            return s;
        }
        if(t == Enums.StaffType.Driver){
            s = new Driver();
            return s;
        }
        out("ERROR, TYPE MISMATCH. RETURNED STAFF IS VOID");
        return s;
    }
}
