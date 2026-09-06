//i don't actually know what libraries Enemy needs, but I took this from the Bee class
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

import java.util.Random;
import Components.CollisionBox;


public class Enemy implements CollisionBox {
    String name;    //do they need names??
    float speed = 2f; //same speed as bees?

    //I also took this from the bee class
    public boolean EnabledCollider = true;
    public Vector3 Position = new Vector3();

    public Enemy(String name){
        this.name=name;
    }

    public Bee pickBeeTarget(){ //same logic as Bee picking target -- from Hive
        int randomIndex=new Random().nextInt(Hive.Bees.size());
        return Hive.Bees.get(randomIndex);
    }
}
