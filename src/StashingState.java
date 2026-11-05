import static com.raylib.Raylib.CheckCollisionBoxes;
import static com.raylib.Raylib.Vector3Lerp;

public class StashingState implements BeeState {

    @Override
    public void update(BeeWorker bee, float deltaTime) {
        if (CheckCollisionBoxes(bee.getCollider(), bee.myHive.getCollider())) {
            bee.myHive.HoneyCapacity += Math.round(bee.depositNector());
            bee.InHive = true;
            bee.changeState(new RestingState());
        } else {
            bee.Position = Vector3Lerp(bee.Position, bee.myHive.Position, deltaTime * bee.Speed);
        }
    }

    @Override
    public void exit(BeeWorker bee){
        bee.InHive = false;
    }
}
