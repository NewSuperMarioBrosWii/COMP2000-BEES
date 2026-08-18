import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

import com.raylib.Raylib.BoundingBox;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

public class Bee {
    String Name = "Bee";
    float Speed = 2f;
    float Scale = 1f;

    Texture VisualTexture = LoadTexture("Assets/Bee.png");

    boolean EnabledCollider = true;
    BoundingBox Collider = new BoundingBox();

    Vector3 Position = new Vector3();
    Vector3 Target = new Vector3();

    Bee(String Name){
        this.Name = Name;
    }
    
    Bee(String Name, String Sprite){
        this.Name = Name;
        VisualTexture = LoadTexture(Sprite);
    }

    //This is where the live functionallity lives this will be called (hopefully) every frame? i think
    void update(float deltaTime){

    }


    void draw(Camera3D Camera){
        DrawBillboard(Camera, VisualTexture, Position, Scale, RAYWHITE);
        if(EnabledCollider){
            Collider.min(new Vector3().x(Position.x() - Scale/2)
                                  .y(Position.y() - Scale/2)
                                  .z(Position.z() - Scale/2))
                .max(new Vector3().x(Position.x() + Scale/2)
                                  .y(Position.y() + Scale/2)
                                  .z(Position.z() + Scale/2));
            DrawBoundingBox(Collider, RED);
        }
    }
}
