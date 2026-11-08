import static com.raylib.Colors.RAYWHITE;
import static com.raylib.Raylib.DrawBillboard;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Texture;
import com.raylib.Raylib.Vector3;

public class Flower extends Location{
    public String FlowerName = "";
    public Texture VisualTexture;
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
        CreateCollider(offsetPos, new Vector3().x(Scale/2).y(Scale).z(Scale/2));
        if(Pollen <= 0){
            empty = true;
        }
    }

}
