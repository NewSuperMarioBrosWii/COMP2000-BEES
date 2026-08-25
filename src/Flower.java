import static com.raylib.Colors.RAYWHITE;
import static com.raylib.Raylib.DrawBillboard;
import static com.raylib.Raylib.LoadTexture;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

public class Flower extends Location{
    public String FlowerName = "";
    public Texture VisualTexture = LoadTexture("Assets/flower.png");
    float Scale = 1f;
    int Pollen = 100;
    boolean occupied = false;

    Flower(String Name, Vector3 Location, int Pollen, String SpritePath) {
        super(Name, Location);
        this.Pollen = Pollen;
    }
    Flower(String Name, Vector3 Location, int Pollen) {
        super(Name, Location);
        this.Pollen = Pollen;
    }

    public void DrawFlower(Camera3D camera){
        Vector3 offsetPos = new Vector3().y(Scale/2).x(Position.x()).z(Position.z());
        DrawBillboard(camera, VisualTexture, offsetPos, Scale, RAYWHITE);
        CreateUniformCollider(offsetPos, Scale);
    }


}
