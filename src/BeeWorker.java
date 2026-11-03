import static com.raylib.Raylib.CheckCollisionBoxes;
import static com.raylib.Raylib.Vector3Distance;
import static com.raylib.Raylib.Vector3Lerp;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

import java.util.ArrayList;
import java.util.Random;

public class BeeWorker extends Bee{

    private float nector = 0;
    private float MaxNector = 50;
    private int TakeRate = 10;

    // free marks if we use the state pattern
    private enum BeeState {
        RESTING,
        FETCHING,
        SAPPING,
        STACHING,
        ATTACK;
    }

    BeeState state = BeeState.RESTING;
    Flower Target;
    Enemy predator;

    BeeWorker(String Name, Hive hive, Texture texture) {
        super(Name, hive, texture);
        myHive = hive;
    }

    @Override
    public void update(float deltaTime){
        switch (state) {
            case BeeState.RESTING:
                if (new Random().nextFloat(0, 100) < 50){
                state = BeeState.FETCHING;
                }
                break;
            

            case BeeState.FETCHING:
                if(Target == null){
                    try{
                        Target = myHive.pickFlower();
                    }catch (Hive.NoAvailableFlowerException e){
                        //no flower free right now - try again next frame
                    }
                    return;
                }
                Target.occupied = true;
                InHive=false;
                Vector3 offsetPos = new Vector3().y(0.8f).x(Target.Position.x()).z(Target.Position.z());

                if(CheckCollisionBoxes(getCollider(), Target.getCollider())){
                    state = BeeState.SAPPING;
                }

                else{
                    Position = Vector3Lerp(Position, offsetPos, deltaTime * Speed) ;
                }
                if(beingAttacked){
                    state=BeeState.ATTACK;
                }
                break;
            

            case BeeState.SAPPING:
                if(Target != null && nector < MaxNector && Target.empty == false){
                    Target.Pollen -= TakeRate * deltaTime;
                    nector += TakeRate * deltaTime;
                }
                else{
                    state = BeeState.STACHING;
                    Target = null;
                }
                if(beingAttacked){
                    state=BeeState.ATTACK;
                }
                break;
                

            case BeeState.STACHING:
                if(CheckCollisionBoxes(getCollider(), myHive.getCollider())){
                    myHive.HoneyCapacity += Math.round(nector);
                    nector = 0f;
                    InHive=true;
                    state = BeeState.RESTING;
                }
                else{
                    Position = Vector3Lerp(Position, myHive.Position, deltaTime * Speed);
                }
                break;

            
            case BeeState.ATTACK:
                //find wasp attacking
                ArrayList<EnemyWasp>w=Field.getWasps();
                if(predator==null){
                    predator=w.stream()
                        .filter(p->p.target==this)
                        .findFirst()
                        .orElse(null);
                }
                if(predator!=null&&(!predator.isAlive||predator.health<=0)){   //wasp dead
                    predator.isAlive=false;
                    predator=null;  //reset predator
                    state=BeeState.RESTING; //resting or go back to flower?
                }
                if(!this.isAlive||Health<=0){  //bee dead
                    Target.occupied = true;
                    Target=null;
                }
                //sting wasp
                if(predator!=null){
                    stingTimer+=deltaTime;
                    if(stingTimer>=stingInterval){
                        predator.health-=20f;
                        stingTimer=0f;
                    }
                }
                break;
            default:
                break;
        }
    }
    public float getNectorCount(){
        return this.nector;
    }

}
