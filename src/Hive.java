import com.raylib.Raylib.Vector3;

import static com.raylib.Raylib.Vector3Add;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

// when spawning a bee add it to the Bees Array instead of making new ones because they all inherit bee
// so they can all be drawn / update in the existing loop without adding anything to App.java 👍

public class Hive extends Location {

    public int HoneyCapacity = 0;
    public static ArrayList<Bee> Bees = new ArrayList<>();
    public QueenBee myQueen;
    //public static ArrayList<QueenBee> Queens = new ArrayList<>();
    Field field;
    AssetManager assets;
    static NameManager BeeNames = new NameManager("Assets/BeeNames.txt");
    
    Hive(Vector3 Location, Field field, AssetManager aManager) {
        super("Hive", Location);
        this.field = field;
        assets = aManager;
        myQueen = new QueenBee(Name, this, assets.GetTexture("Bee"));
        myQueen.Position = Vector3Add(Position, new Vector3().y(2f)) ;
    }
    
    //this will eventually be the larva spawner
    //we need the function to select a type of bee
    public void CreateBee(){
        BeeWorker NewBee = new BeeWorker(BeeNames.randomName(), this, assets.GetTexture("Bee"));
        Bees.add(NewBee);
    }
    /* 
    public void CreateQueen(){
        QueenBee myQueen = new QueenBee("Beyonce", this , BeeTexture);
        Queens.add(myQueen);
    } */

    public Flower pickFlower(){ // implementing the generic
        boolean anyFree = false;
        for (Flower flower: field.flowerfield){
            if (!flower.occupied){
                anyFree = true;
                break;
            }
            
        }
        if(!anyFree){
            throw new NoAvailableFlowerException("No unoccupied flowers in the field");
        }
       
        Flower f = pickRandom(field.flowerfield);
       if(f.occupied){
        return pickFlower();
       }
       return f;
    }
    
    
    public class NoAvailableFlowerException extends RuntimeException{ //prevents crash
        public NoAvailableFlowerException(String message){
            super(message);
        }
    }

    public static <T> T pickRandom(ArrayList<T> list){ //use this generic for any object to pick randomly
        return list.get(new Random().nextInt(list.size()));
    }

    public static void BeeCheck(){
       Bees.removeIf(bee->!bee.isAlive);
    }


    public void DrawHive(){
        CreateUniformCollider(Position, 1.0f);
    }
    public static int getTotalBeesKilled(){
        return Enemy.totalBeesKilled;
    }
    
}
