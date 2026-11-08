import java.util.Random;

import com.raylib.Raylib.Texture;
public class QueenBee extends Bee {

/*     enum QueenState {
        LAYING,
        RESTING;
    } // can be one of these states

    QueenState state = QueenState.RESTING;  */

    float eggTimer = 0f;
    float eggInterval = 5f; //seconds between eggs

    int eatAmount = 200;

    QueenBee (String Name, Hive hive, Texture texture ) {
        super(Name, hive, texture); //inherets constructor from parent (Bee)
        myHive = hive; 
    }
    

    @Override 
    public void update(float deltaTime){
/*         switch (state){
            case QueenState.RESTING:
                eggTimer += deltaTime;
                if (eggTimer >= eggInterval) { // after interval start laying
                    state = QueenState.LAYING;
                }
                break;
            case QueenState.LAYING:
                eggTimer = 0; //reset timer
                myHive.CreateBee(); // call o
                state = QueenState.RESTING;
                break;

        } */
       eggTimer += deltaTime;
       if (myHive.HoneyCapacity >= eatAmount && eggTimer > eggInterval){
            System.out.println("laying");
            eggTimer = 0f;
            myHive.CreateBee();
            myHive.HoneyCapacity -= eatAmount;
            eggInterval = new Random().nextFloat(5f, 20f);
       }
    }
}