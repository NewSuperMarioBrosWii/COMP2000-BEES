import static com.raylib.Raylib.Vector3Distance;
import static com.raylib.Raylib.Vector3Lerp;

import java.util.Random;

import com.raylib.Raylib.Vector3;

public class BeeWorker extends Bee{

    int nector = 0;
    int MaxNector = 50;
    int TakeRate = 1;

    enum BeeState {
        RESTING,
        FETCHING,
        SAPPING,
        STACHING,
        ATTACK;
    }
    BeeState state = BeeState.RESTING;
    Flower Target;

    BeeWorker(String Name, Hive hive) {
        super(Name, hive);
        myHive = hive;
    }

    @Override
    public void update(float deltaTime){
        if(state == BeeState.RESTING){
            if (new Random().nextFloat(0, 100) < 50){
                state = BeeState.FETCHING;
            }
        }

        else if(state == BeeState.FETCHING){

            if(Target == null){
                Target = myHive.pickFlower();
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
        }

        else if(state == BeeState.SAPPING){
            if(Target != null && nector < MaxNector){
                Target.Pollen -= TakeRate;
                nector += TakeRate;
            }
            else{
                state = BeeState.STACHING;
                Target = null;
            }
        }

        else if (state == BeeState.STACHING){
            if(Vector3Distance(Position, myHive.Position) < 0.1 / Speed){
                myHive.HoneyCapacity += nector;
                nector = 0;
                state = BeeState.RESTING;
            }
            else{
                Position = Vector3Lerp(Position, myHive.Position, deltaTime * Speed);
            }
        }

        else if(state == BeeState.ATTACK){

        }
    }


}
