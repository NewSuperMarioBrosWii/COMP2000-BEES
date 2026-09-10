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
                System.out.println("bee removed");
                return;
            }
        }
    }


    public void DrawHive(){
        CreateUniformCollider(Position, 1.0f);
    }
    
}
