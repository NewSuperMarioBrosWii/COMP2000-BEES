import com.raylib.Raylib.Camera3D;

public interface Simulated {
    void update(float deltaTime);
    void Draw(Camera3D camera);
}
