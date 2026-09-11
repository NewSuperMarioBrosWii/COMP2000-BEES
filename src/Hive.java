import static com.raylib.Raylib.LoadTexture;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;
import java.util.ArrayList;
import java.util.Random;

// when spawning a bee add it to the Bees Array instead of making new ones because they all inherit bee
// so they can all be drawn / update in the existing loop without adding anything to App.java 👍

public class Hive extends Location {

    public int HoneyCapacity = 0;
    public static ArrayList<Bee> Bees = new ArrayList<>();
    //public static ArrayList<QueenBee> Queens = new ArrayList<>();
    Field field;
    
    Hive(Vector3 Location, Field field) {
        super("Hive", Location);
        this.field = field;
    }
    
    public Texture BeeTexture = LoadTexture("Assets/Bee.png");
    //this will eventually be the larva spawner
    //we need the function to select a type of bee
    public void CreateBee(){
        BeeWorker NewBee = new BeeWorker("beeson", this, BeeTexture);
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
        for(Bee b : Bees){
            if(!b.isAlive){
                Bees.remove(b);
                return;
            }
        }
    }


    public void DrawHive(){
        CreateUniformCollider(Position, 1.0f);
    }
    
}
