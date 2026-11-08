import com.raylib.Raylib.Texture;

public class BeeWorker extends Bee {

    private float nector = 0;
    private final float maxNector = 50;
    final int takeRate = 10;

    Flower Target;

    private BeeState currentState;

    BeeWorker(String Name, Hive hive, Texture texture) {
        super(Name, hive, texture);
        myHive = hive;
        changeState(new RestingState());
    }

    @Override
    public void update(float deltaTime) {
        currentState.update(this, deltaTime);
    }

    public void changeState(BeeState next) {
        if (currentState != null) currentState.exit(this);
        currentState = next;
        currentState.enter(this);
    }

    public float getNectorCount()      { return nector; }
    public boolean isNectorFull()      { return nector >= maxNector; }
    public void addNector(float n)     { nector += n; }

    public float depositNector() {
        float carried = nector;
        nector = 0f;
        return carried;
    }

    public void releaseFlower() {
        if (Target != null) {
            Target.occupied = false;
            Target = null;
        }
    }

    public String getCurrentStateName(){
        return currentState.getClass().toString();
    }
}