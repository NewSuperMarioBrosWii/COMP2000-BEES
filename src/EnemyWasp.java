import static com.raylib.Raylib.Vector3Distance;
import static com.raylib.Raylib.Vector3Lerp;

import java.util.Random;

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
    
    //used when resting
    Vector3 randPos;
    float fieldSize = Field.fieldSize *5;
    float restSpeed = 0.8f;

    public EnemyWasp(String name, Vector3 spawnPos, Texture texture) {
        super(name, spawnPos, texture);
        texture=VisualTexture;
        target=pickTarget();        
    }
    
    public void Sting(){
        //check if bee is already dead, or in the hive
        if(!target.isAlive ||target.InHive){
            state=EnemyState.RESTING;
        }
        else{   //bee is alive --> sting == kill?
            target.isAlive=false;
            honeyStolen+=((BeeWorker)target).nector;
            beesKilled++;
            state=EnemyState.RESTING;
        }
    }

    public Vector3 getNewRandPos(){
        float randX = new Random().nextFloat(-fieldSize,fieldSize);
        float randY = new Random().nextFloat(-fieldSize/5,fieldSize/5);
        float randZ = new Random().nextFloat(-fieldSize,fieldSize);
        return new Vector3().y(randY).x(randX).z(randZ);
    }

    @Override
    public void update (float deltaTime) {
        switch(state){
            case EnemyState.RESTING:
                if (randPos==null){
                    randPos=getNewRandPos();
                }
                //go towards random position
                Position = Vector3Lerp(Position, randPos, deltaTime * restSpeed);
                if (Vector3Distance(Position, randPos) < 0.5f) {    //once the wasp gets close, choose a new randPos
                    randPos = getNewRandPos();
                }
                if(new Random().nextFloat(0, 100) < 50){    //chance to attack
                    state=EnemyState.ATTACKING;
                }
                break;
            case EnemyState.ATTACKING:
                if (target==null||target.InHive) {
                    //target=pickTarget();
                    target=null;
                    try{
                        target=pickTarget();
                    }
                    catch(NoAvailableBeeTarget e){
                        state=EnemyState.RESTING;
                        randPos=getNewRandPos();
                        target=null;
                        break;
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
                Position = Vector3Lerp(Position, offsetPos, deltaTime * speed);
        // if wasp is close enough to bee, sting it
                if (Vector3Distance (Position, offsetPos) < 0.1 / speed){
                    Sting();
                    if(state==EnemyState.RESTING){
                        target=null;
                        randPos=getNewRandPos();
                   }
                }
            }
    
        }
}
