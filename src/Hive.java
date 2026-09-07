import static com.raylib.Raylib.LoadTexture;

import java.util.ArrayList;
import java.util.Random;

import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

public class Hive extends Location {

    public int HoneyCapacity = 0;
    public static ArrayList<Bee> Bees = new ArrayList<>();
    public static ArrayList<QueenBee> Queens = new ArrayList<>();
    Field field;
    
    Hive(Vector3 Location, Field field) {
        super("Hive", Location);
        this.field = field;
    }
    
    public Texture BeeTexture = LoadTexture("Assets/Bee.png");
    //this will eventually be the larva spawner
    public void CreateBee(){
        BeeWorker NewBee = new BeeWorker("beeson", this, BeeTexture);
        Bees.add(NewBee);
    }

    public void CreateQueen(){
        QueenBee myQueen = new QueenBee("Beyonce", this , BeeTexture);
        Queens.add(myQueen);
    }

    public Flower pickFlower(){
        int randflower = new Random().nextInt(0, field.flowerfield.size());
        if(field.flowerfield.get(randflower).occupied == true){
            pickFlower();
        }
        return field.flowerfield.get(randflower);
    }

    public void DrawHive(){
        CreateUniformCollider(Position, 1.0f);
    }
    
}
