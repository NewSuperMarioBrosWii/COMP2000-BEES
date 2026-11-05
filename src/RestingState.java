import java.util.Random;

import com.raylib.Raylib.Vector3;

public class RestingState implements BeeState {
    private static final Random RNG = new Random();   // one shared Random, not one per frame


    float doTime = 0;
    int doTimer = 5;

    Vector3 moveDir = new Vector3();


    @Override
    public void update(BeeWorker bee, float deltaTime) {
        RNG.nextInt(0, 10);

        doTime += deltaTime;

        if (RNG.nextInt(1, 10) < 3 && doTime > doTimer) {
            bee.changeState(new FetchingState());
        }
        else{
            if(doTime > doTimer){
                moveDir = pickDirection();
                doTimer = RNG.nextInt(1, 2);
                doTime = 0;
            }
            else{
                bee.movePosition(bee.Position,moveDir, 2f,deltaTime);
            }
        }
    }

    Vector3 pickDirection(){
        return new Vector3().x(RNG.nextFloat(-1, 1)).y(RNG.nextFloat(-1, 1)).z(RNG.nextFloat(-1, 1));
    }
}
