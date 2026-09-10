import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

import Components.CollisionBox;

//🐝

public class Bee implements CollisionBox {
    public String Name = "Bee";
    float Speed = 2f;
    float Scale = 0.5f;

    Hive myHive;
    boolean InHive = false;
    boolean isAlive = true;

    public Texture VisualTexture;// = LoadTexture("Assets/Bee.png");

    public boolean EnabledCollider = true;

    public Vector3 Position = new Vector3();

    Bee(String Name, Hive hive, Texture texture){
        this.Name = Name;
        this.VisualTexture = texture;
    }

    Bee(String Name, String Sprite, Hive hive){
        this.Name = Name;
        VisualTexture = LoadTexture(Sprite);
    }

    //This is where the live functionallity lives this will be called (hopefully) every frame? i think
    public void update(float deltaTime){

    }

    public void Draw(Camera3D Camera){
        DrawBillboard(Camera, VisualTexture, Position, Scale, RAYWHITE);
        if(EnabledCollider){
            CreateUniformCollider(Position, Scale);
        }
    }
}
