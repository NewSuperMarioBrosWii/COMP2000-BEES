import static com.raylib.Raylib.LoadTexture;

import java.util.ArrayList;
import java.util.Random;

import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

public class Field extends Location{
    public ArrayList<Flower> flowerfield = new ArrayList<>();
    public float fieldSize = 10;

    public Texture FlowerTexture = LoadTexture("Assets/flower.png");

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
        float randx = new Random().nextFloat(-fieldSize, fieldSize);
        float randz = new Random().nextFloat(-fieldSize, fieldSize);

        Flower newflower = new Flower("tulip", new Vector3().x(randx).z(randz), 100,FlowerTexture);
        flowerfield.add(newflower);
    }
}
