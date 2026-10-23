import com.raylib.Raylib.Camera3D;

// anything that is updated and drawn every frame 
// Bee and Enemy don't share a parent, so an interface lets App treat them the same
public interface Simulated {
    void update(float deltaTime);
    void Draw(Camera3D camera);
}
