package src;

import java.util.ArrayList;

//this is the strategy pattern implementation. Abstract method created in Wash, concrete algorithms detailed below
public interface Wash{
    public void wash(ArrayList<Vehicle> vlist, Intern washer, EventPublisher eventPublisher);
}

class chemical implements Wash, SysOut{
    public void wash(ArrayList<Vehicle> vList, Intern washer, EventPublisher eventPublisher){
        //each of these washes work in precisely the same manner, so I will only document this first one
        //they just have different probabilities
        out("The chemical method is being used for this wash.");
        //calls the utility function built to return a priority queue, dirty vehicles first
        ArrayList<Vehicle> toWash = Utility.sortForDirty(vList);
        int count = 0;
        Enums.Cleanliness startAs;
        //loops through vehicles until 2 have been washed, or until a sparkling one has been reached
        //(implying that all vehicles are sparkling)
        for(Vehicle v:toWash){
            count++;
            //get the initial cleanliness state
            startAs = v.cleanliness;
            //random function called to determine probability
            double chance = Utility.rnd();
            //checks to see if vehicle is dirty or clean
            if(v.cleanliness == Enums.Cleanliness.Dirty){
                //change vehicle from dirty to clean
                if(chance <= .8){
                    v.cleanliness = Enums.Cleanliness.Clean;
                    out(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness);
                    eventPublisher.publishEvent(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness, false);
                }
                //change vehicle from dirty to sparkling, give bonus to intern
                else if(chance <= .9){
                    v.cleanliness = Enums.Cleanliness.Sparkling;
                    out("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus);
                    eventPublisher.publishEvent("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus, false);
                    washer.bonusEarned += v.wash_bonus;
                }
            }
            //same idea as above, but now with clean
            else if(v.cleanliness == Enums.Cleanliness.Clean){
                if(chance <= .1){
                    v.cleanliness = Enums.Cleanliness.Dirty;
                    out(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness);
                    eventPublisher.publishEvent(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness, false);

                }
                else if(chance <= .3){
                    v.cleanliness = Enums.Cleanliness.Sparkling;
                    out("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus);
                    eventPublisher.publishEvent("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus, false);
                    washer.bonusEarned += v.wash_bonus;
                }
            }
            else if(v.cleanliness == Enums.Cleanliness.Sparkling){
                out(washer.name + " has ran out of clean and dirty cars to wash!");
                return;
            }
            chance = Utility.rnd();
            //chance of breaking, below it has a chance of being made like new
            if(chance <= .1 && v.condition!=Enums.Condition.Broken){
                out("The intern " + washer.name + " accidently made the vehicle broken!");
                eventPublisher.publishEvent("The intern " + washer.name + " accidently made the vehicle broken!", false);

            }
            if(count==2)break;
        }
    }
}

class elbowGrease implements Wash,SysOut{
    //reference above notes
    public void wash(ArrayList<Vehicle> vList, Intern washer, EventPublisher eventPublisher){
        out("The elbow grease method is being used for this wash.");
        ArrayList<Vehicle> toWash = Utility.sortForDirty(vList);
        int count = 0;
        Enums.Cleanliness startAs;
        for(Vehicle v:toWash){
            count++;
            startAs = v.cleanliness;
            double chance = Utility.rnd();
            if(v.cleanliness == Enums.Cleanliness.Dirty){
                if(chance <= .7){
                    v.cleanliness = Enums.Cleanliness.Clean;
                    out(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness);
                    eventPublisher.publishEvent(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness, false);

                }
                else if(chance <= .75){
                    v.cleanliness = Enums.Cleanliness.Sparkling;
                    out("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus);
                    eventPublisher.publishEvent("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus, false);
                    washer.bonusEarned += v.wash_bonus;
                }
            }
            else if(v.cleanliness == Enums.Cleanliness.Clean){
                if(chance <= .15){
                    v.cleanliness = Enums.Cleanliness.Dirty;
                    out(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness);
                    eventPublisher.publishEvent(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness, false);
                }
                else if(chance <= .3){
                    v.cleanliness = Enums.Cleanliness.Sparkling;
                    out("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus);
                    eventPublisher.publishEvent("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus, false);
                    washer.bonusEarned += v.wash_bonus;
                }
            }
            else if(v.cleanliness == Enums.Cleanliness.Sparkling){
                out(washer.name + " has ran out of clean and dirty cars to wash!");
                return;
            }
            chance = Utility.rnd();
            if(chance <= .1 && v.condition!=Enums.Condition.LikeNew){
                out("The intern " + washer.name + " made the vehicle like new!");
                eventPublisher.publishEvent("The intern " + washer.name + " made the vehicle like new!", false);

            }
            if(count==2)break;
        }
    }
}

class detailed implements Wash,SysOut{
    //reference above notes
    public void wash(ArrayList<Vehicle> vList, Intern washer, EventPublisher eventPublisher){
        out("The detailed method is being used for this wash.");
        ArrayList<Vehicle> toWash = Utility.sortForDirty(vList);
        int count = 0;
        Enums.Cleanliness startAs;
        for(Vehicle v:toWash){
            count++;
            startAs = v.cleanliness;
            double chance = Utility.rnd();
            if(v.cleanliness == Enums.Cleanliness.Dirty){
                if(chance <= .6){
                    v.cleanliness = Enums.Cleanliness.Clean;
                    out(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness);
                    eventPublisher.publishEvent(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness, false);

                }
                else if(chance <= .8){
                    v.cleanliness = Enums.Cleanliness.Sparkling;
                    out("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus);
                    eventPublisher.publishEvent("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus, false);
                    washer.bonusEarned += v.wash_bonus;
                }
            }
            else if(v.cleanliness == Enums.Cleanliness.Clean){
                if(chance <= .05){
                    v.cleanliness = Enums.Cleanliness.Dirty;
                    out(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness);
                    eventPublisher.publishEvent(washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness, false);
                }
                else if(chance <= .45){
                    v.cleanliness = Enums.Cleanliness.Sparkling;
                    out("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus);
                    eventPublisher.publishEvent("Intern" + washer.name + " made the " + v.name + " go from " + startAs + " to " + v.cleanliness +". They earned a bonus of: " + v.wash_bonus, false);
                    washer.bonusEarned += v.wash_bonus;
                }
            }
            else if(v.cleanliness == Enums.Cleanliness.Sparkling){
                out(washer.name + " has ran out of clean and dirty cars to wash!");
                return;
            }
            if(count==2)break;
        }
    }
}
