import static com.raylib.Raylib.*;
import static com.raylib.Colors.*;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Ray;
import com.raylib.Raylib.Vector3;

public class App {
    public static void main(String[] args) throws Exception {
        InitWindow(1280, 720, "Demo");
        SetTargetFPS(60);

        //--- this is all camera setup releated stuff
        //the current setup is a orbiting camera that is controlled with the left and right arrow keys and a zoom with up and down
        //idk if we should add a way to change the pitch but its not nessesary for it to work
        Vector3 CameraOrigin = new Vector3();
        
        Camera3D camera = new Camera3D()
                .target(new Vector3())
                .up(new Vector3().x(0).y(1).z(0))
                .fovy(45).projection(CAMERA_PERSPECTIVE);
        
        float distance = 10.0f;
        float minDistance = 2.0f;
        float maxDistance = 40.0f;
        float zoomSpeed = 8.0f;
        float yaw = 0.0f;
        float pitch = 0.2f;
        float rotationSpeed = 2.0f;
        //---

        Bee SelectedBee = null;

        while (!WindowShouldClose()) {
            float deltaTime = GetFrameTime();
            //Ray MouseRay = GetScreenToWorldRay(GetMousePosition(), camera);

            //--- this section is for processing the scene camera controls
            if (IsKeyDown(KEY_LEFT))  yaw -= rotationSpeed * deltaTime;
            if (IsKeyDown(KEY_RIGHT)) yaw += rotationSpeed * deltaTime;
            if (IsKeyDown(KEY_UP))   distance -= zoomSpeed * deltaTime;
            if (IsKeyDown(KEY_DOWN)) distance += zoomSpeed * deltaTime;

            distance = Math.max(minDistance, Math.min(maxDistance, distance));

            float x = CameraOrigin.x() + distance * (float) (Math.cos(pitch) * Math.sin(yaw));
            float y = CameraOrigin.y() + distance * (float) Math.sin(pitch);
            float z = CameraOrigin.z() + distance * (float) (Math.cos(pitch) * Math.cos(yaw));

            camera._position(new Vector3().x(x).y(y).z(z));
            //---

            //This is where all the drawing is done
            BeginDrawing();
                ClearBackground(RAYWHITE);

                //This is where the 3d is drawn
                BeginMode3D(camera);
                    DrawGrid(20, 1.0f);
                    //bees go here

                EndMode3D();
                if(SelectedBee != null) {DrawBeeStats(SelectedBee);}
            EndDrawing();
        }
    }

    //the stats can be but on screne with DrawText
    //its probably good to have the full string of data layed out and then drawing it with one call
    static void DrawBeeStats(Bee Bee){
        String status = "Name: " + Bee.toString();
        DrawText(status, 20, 20, 20, BLACK);
    }   
}
