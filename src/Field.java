import java.util.ArrayList;
import java.util.Random;

import com.raylib.Raylib.Vector3;

public class Field extends Location{
    public ArrayList<Flower> flowerfield = new ArrayList<>();
    public float fieldSize = 5;


    Field(Vector3 Location, float fieldSize) {
        super("Field", Location);
        this.fieldSize = fieldSize;
    }

    public void fieldCheck(){
        for(Flower f : flowerfield){
            if(f.Pollen == 0){
                flowerfield.remove(f);
            }
        }
    }

    public void SpawnFlower(){
        float randx = new Random().nextFloat(-fieldSize, fieldSize);
        float randz = new Random().nextFloat(-fieldSize, fieldSize);

        Flower newflower = new Flower("tulip", new Vector3().x(randx).z(randz), 100);
        flowerfield.add(newflower);
    }
}
