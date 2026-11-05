import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;
import com.raylib.Raylib.BoundingBox;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;
import Components.CollisionBox;

//🐝

public class Bee implements CollisionBox, Simulated {
    public String Name = "Bee";
    float Speed = 2f;
    float Scale = 0.5f;
    float Health = 100f;
    float stingInterval=5f;
    float stingTimer=2f;

    Hive myHive;
    boolean InHive = false;
    boolean isAlive = true;
    boolean beingAttacked=false;

    public Texture VisualTexture;

    public boolean EnabledCollider = true;

    public Vector3 Position = new Vector3();

    private final BoundingBox collider = new BoundingBox();
    @Override public BoundingBox getCollider() { return collider; }

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

    public static void movePosition(Vector3 position, Vector3 direction, float speed, float deltaTime) {
        float newX = position.x() + (direction.x() * speed * deltaTime);
        float newY = position.y() + (direction.y() * speed * deltaTime);
        float newZ = position.z() + (direction.z() * speed * deltaTime);
        position.x(newX);
        position.y(newY);
        position.z(newZ);
    }
}
