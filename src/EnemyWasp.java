import static com.raylib.Raylib.Vector3Distance;
import static com.raylib.Raylib.Vector3Lerp;

import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

public class EnemyWasp extends Enemy {
    //attributes based off UML
    int beesKilled=0;
    int honeyStolen=0;

    public Texture VisualTexture;
    boolean isAlive;
    Bee target;
    float huntTimer=0f; //time since last target pos update
    float huntInterval = 5f;    //update target pos after 5s

    public EnemyWasp(String name, Vector3 spawnPos, Texture texture) {
        super(name, spawnPos, texture);
        texture=VisualTexture;
        target=pickTarget();
        
    }
    
    public void Sting(){
        //check if bee is already dead, or in the hive
        if(!target.isAlive ||target.InHive){
            throw new RuntimeException();
        }
        else{   //bee is alive --> sting == kill?
            target.isAlive=false;
            honeyStolen+=((BeeWorker)target).nector;
            beesKilled++;
        }
    }

    @Override
    public void update (float deltaTime) {
        if (target==null) {
            //target=pickTarget();
            try{
                target=pickTarget();
            }
            catch(NoAvailableBeeTarget e){
                target=null;
            }
        }
        //update target position - assuming it doesn't update automatically
    Vector3 offsetPos = new Vector3().y (0.8f).x(target.Position.x()).z(target.Position.z());
    huntTimer+=deltaTime;
        if (huntTimer>=huntInterval) {
            //update target pos after 5s
            offsetPos = new Vector3().y(0.8f).x(target.Position.x()).z(target.Position.z());
            huntTimer=0f;
        }
        Position = Vector3Lerp(Position, offsetPos, deltaTime * speed) ;
        // if its close enough to the bee, sting it
        if (Vector3Distance (Position, offsetPos) < 0.1 / speed){
            try {
                Sting();
            } catch (Exception e) { //bee is already dead or in hive
                target=pickTarget();    //get a new target
            }
        }
    }
}
