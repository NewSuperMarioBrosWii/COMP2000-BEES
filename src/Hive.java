import java.util.ArrayList;
import java.util.Random;

import com.raylib.Raylib.Vector3;

public class Hive extends Location {

    public int HoneyCapacity = 0;
    public ArrayList<Bee> Bees = new ArrayList<>();
    Field field;
    
    Hive(Vector3 Location, Field field) {
        super("Hive", Location);
        this.field = field;
    }
    
    //this will eventually be the larva spawner
    public void CreateBee(){
        Bee NewBee = new Bee("beeson", this);
        Bees.add(NewBee);
    }

    public Flower pickFlower(){
        int randflower = new Random().nextInt(0, field.flowerfield.size());
        return field.flowerfield.get(randflower);
    }

    public void DrawHive(){
        CreateUniformCollider(Position, 1.0f);
    }
    
}
