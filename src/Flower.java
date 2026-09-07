import static com.raylib.Colors.RAYWHITE;
import static com.raylib.Raylib.DrawBillboard;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

public class Flower extends Location{
    public String FlowerName = "";
    public Texture VisualTexture;// = LoadTexture("Assets/flower.png");
    float Scale = 1f;
    float Pollen = 100;
    boolean occupied = false;
    boolean empty = false;

    Flower(String Name, Vector3 Location, int Pollen, Texture Texture) {
        super(Name, Location);
        this.Pollen = Pollen;
        this.VisualTexture = Texture;
    }
    Flower(String Name, Vector3 Location, int Pollen) {
        super(Name, Location);
        this.Pollen = Pollen;
    }

    public void DrawFlower(Camera3D camera){
        Vector3 offsetPos = new Vector3().y(Scale/2).x(Position.x()).z(Position.z());
        DrawBillboard(camera, VisualTexture, offsetPos, Scale, RAYWHITE);
        CreateUniformCollider(offsetPos, Scale);
        if(Pollen <= 0){
            empty = true;
        }
    }


}
