import static com.raylib.Raylib.CheckCollisionBoxes;
import static com.raylib.Raylib.Vector3Lerp;
import com.raylib.Raylib.Vector3;

public class FetchingState implements BeeState {

    @Override
    public void update(BeeWorker bee, float deltaTime) {
        if (bee.Target == null) {
            try {
                bee.Target = bee.myHive.pickFlower();
                bee.Target.occupied = true;
            } catch (Hive.NoAvailableFlowerException e) {
                // no flower free right now - try again next frame
                bee.changeState(new RestingState());
            }
            return;
        }

        Vector3 offsetPos = new Vector3().y(0.8f).x(bee.Target.Position.x()).z(bee.Target.Position.z());

        if (CheckCollisionBoxes(bee.getCollider(), bee.Target.getCollider())) {
            bee.changeState(new SappingState());
        } else {
            bee.Position = Vector3Lerp(bee.Position, offsetPos, deltaTime * bee.Speed);
        }

        if (bee.beingAttacked) {
            bee.changeState(new AttackState());
        }
    }
}
