package src;

//this is the decorator pattern!!
public interface AddOn {
    abstract String getName();
    abstract double getChanceBuying();
    abstract double getPriceIncrease();
}

class ExtendedWarranty implements AddOn {
    String name = "Extended Warranty";
    double chanceBuying = 0.25;
    double priceIncrease = 0.2;
    @Override
    public String getName() {
        return name;
    }
    @Override
    public double getPriceIncrease() {
        return priceIncrease;
    }
    @Override
    public double getChanceBuying() {
        return chanceBuying;
    }
}

class Undercoating implements AddOn {
    String name = "src.Undercoating";
    double priceIncrease = 0.05;
    double chanceBuying = 0.1;

    @Override
    public String getName() {
        return name;
    }
    @Override
    public double getPriceIncrease() {
        return priceIncrease;
    }
    @Override
    public double getChanceBuying() {
        return chanceBuying;
    }

}

class RoadRescueCoverage implements AddOn {

    String name = "Road Rescue Coverage";
    double priceIncrease = 0.02;
    double chanceBuying = 0.05;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPriceIncrease() {
        return priceIncrease;
    }

    @Override
    public double getChanceBuying() {
        return chanceBuying;
    }
}
class SatelliteRadio implements AddOn {

    String name = "Satellite Radio";
    double priceIncrease = 0.05;
    double chanceBuying = 0.4;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPriceIncrease() {
        return priceIncrease;
    }

    @Override
    public double getChanceBuying() {
        return chanceBuying;
    }
}