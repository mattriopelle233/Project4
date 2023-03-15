package src;

public class VehicleFactory implements SysOut{
    public Vehicle createVehicle(Enums.VehicleType type){
        Vehicle v = null;
        if(type == Enums.VehicleType.Car){
            v = new Car();
            return v;
        }
        if(type == Enums.VehicleType.PerfCar){
            v = new PerfCar();
            return v;
        }
        if(type == Enums.VehicleType.Pickup){
            v = new Pickup();
            return v;
        }
        if(type == Enums.VehicleType.ElectricCar){
            v = new ElectricCar();
            return v;
        }
        if(type == Enums.VehicleType.MotorCycle){
            v = new MotorCycle();
            return v;
        }
        if(type == Enums.VehicleType.MonsterTruck){
            v = new MonsterTruck();
            return v;
        }
        if(type == Enums.VehicleType.Vespa){
            v = new Vespa();
            return v;
        }
        if(type == Enums.VehicleType.HydrogenCar){
            v = new HydrogenCar();
            return v;
        }
        if(type == Enums.VehicleType.HybridCar){
            v = new HybridCar();
            return v;
        }
        out("ERROR. ENUM TYPE MISMATCH. NULL VEHICLE RETURNED");
        return v;
    }
}
