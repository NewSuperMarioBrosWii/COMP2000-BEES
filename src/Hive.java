import java.util.ArrayList;

import com.raylib.Raylib.Vector3;

public class Hive extends Location {

    public int Capacity = 0;
    public ArrayList<Bee> Bees = new ArrayList<>();
    
    Hive(Vector3 Location) {
        super("Hive", Location);
    }
    
    //the bees should probably have a refrence to the flower field so that needs do be done
    public void CreateBee(){
        Bee NewBee = new Bee("beeson");
        Bees.add(NewBee);
    }
    
}
