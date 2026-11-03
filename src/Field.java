import java.util.ArrayList;
import java.util.Random;
import com.raylib.Raylib.Vector3;


public class Field extends Location{
    public ArrayList<Flower> flowerfield = new ArrayList<>();
    public static float fieldSize = 10;

    AssetManager assets;

    Field(Vector3 Location, float fieldSize, AssetManager aManager) {
        super("Field", Location);
        this.fieldSize = fieldSize;
        assets = aManager;
    }

    public void fieldCheck(){
        boolean removed = this.flowerfield.removeIf(f -> f != null && f.empty);
        if (removed) {
            SpawnFlower();
        }
    }

    public void SpawnFlower(){
        Flower newflower = new Flower("tulip", RandomFloorPos(fieldSize), 100,assets.GetTexture("flower"));
        flowerfield.add(newflower);
    }

    public Vector3 RandomFloorPos(float fieldSize){
        float randx = new Random().nextFloat(-fieldSize, fieldSize);
        float randz = new Random().nextFloat(-fieldSize, fieldSize);
        return new Vector3().x(randx).y(0.0f).z(randz);
    }

    // Wasp Stuff

    //public Texture WaspTexture = LoadTexture("Assets/wasp.png");
    public static ArrayList<EnemyWasp> wasps = new ArrayList<>();

    public void spawnWasp(){
        float randx = new Random().nextFloat(-fieldSize, fieldSize);
        EnemyWasp newWasp = new EnemyWasp("Gwesped",new Vector3().x(randx).y(2.1f).z(-1f),assets.GetTexture("wasp"));
        wasps.add(newWasp);
    }

    public static ArrayList<EnemyWasp> getWasps(){
        return wasps;
    }
}
