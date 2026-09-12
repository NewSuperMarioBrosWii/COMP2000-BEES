import java.util.ArrayList;
import java.util.Random;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;
import static com.raylib.Raylib.LoadTexture;

public class Field extends Location{
    public ArrayList<Flower> flowerfield = new ArrayList<>();
    public ArrayList<EnemyWasp> wasps = new ArrayList<>();
    public static float fieldSize = 10;

    public Texture FlowerTexture = LoadTexture("Assets/flower.png");
    public Texture WaspTexture = LoadTexture("Assets/wasp.png");

    Field(Vector3 Location, float fieldSize) {
        super("Field", Location);
        this.fieldSize = fieldSize;
    }

    public void fieldCheck(){
        for(Flower f : this.flowerfield){
            if(f != null && f.empty){
                flowerfield.remove(f);
                SpawnFlower();
                return;
            }
        }
    }

    public void SpawnFlower(){
        Flower newflower = new Flower("tulip", RandomFloorPos(fieldSize), 100,FlowerTexture);
        flowerfield.add(newflower);
    }

    public void spawnWasp(){
        float randx = new Random().nextFloat(-fieldSize, fieldSize);
        EnemyWasp newWasp = new EnemyWasp("Gwesped",new Vector3().x(randx).y(2.1f).z(-1f),WaspTexture);
        wasps.add(newWasp);
    }

    public Vector3 RandomFloorPos(float fieldSize){
        float randx = new Random().nextFloat(-fieldSize, fieldSize);
        float randz = new Random().nextFloat(-fieldSize, fieldSize);
        return new Vector3().x(randx).y(0.0f).z(randz);
    }
}
