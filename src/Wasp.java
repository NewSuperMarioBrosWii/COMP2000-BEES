//I don't know what libraries we need - stolen from BeeWorker
import static com.raylib.Raylib.Vector3Distance;
import static com.raylib.Raylib.Vector3Lerp;
import com.raylib.Raylib.Vector3;

public class Wasp extends Enemy {
    //attributes based off UML
    int beesKilled=0;
    int honeyStolen=0;
    Bee target;

    public Wasp(String name) {
        super(name);
        target=pickBeeTarget();
        
    }
    
    public void String(){
        
    }
}
