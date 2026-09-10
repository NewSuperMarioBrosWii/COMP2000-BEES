import static com.raylib.Raylib.Vector3Distance;
import static com.raylib.Raylib.Vector3Lerp;

import java.util.Random;

import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

public class BeeWorker extends Bee{

    float nector = 0;
    float MaxNector = 50;
    int TakeRate = 10;

    enum BeeState {
        RESTING,
        FETCHING,
        SAPPING,
        STACHING,
        ATTACK;
    }
    BeeState state = BeeState.RESTING;
    Flower Target;

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
                Vector3 offsetPos = new Vector3().y(0.8f).x(Target.Position.x()).z(Target.Position.z());

                if(Vector3Distance(Position, offsetPos) < 0.1 / Speed){
                    state = BeeState.SAPPING;
                }

                else{
                    Position = Vector3Lerp(Position, offsetPos, deltaTime * Speed) ;
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
                break;
            case BeeState.STACHING:
                if(Vector3Distance(Position, myHive.Position) < 0.1 / Speed){
                    myHive.HoneyCapacity += Math.round(nector);
                    nector = 0f;
                    state = BeeState.RESTING;
                }
                else{
                    Position = Vector3Lerp(Position, myHive.Position, deltaTime * Speed);
                }
                break;
            case BeeState.ATTACK:
                
                break;
            default:
                break;
        }
    }


}
