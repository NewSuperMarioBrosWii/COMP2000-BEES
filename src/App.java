import static com.raylib.Raylib.*;
import static com.raylib.Colors.*;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Ray;
import com.raylib.Raylib.Vector3;

public class App {
    public static void main(String[] args) throws Exception {
        InitWindow(1280, 720, "Bee Simulator");
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
        

        Field flowers = new Field(CameraOrigin, 5f);

        for(int i=0;i<20;i++){flowers.SpawnFlower();}

        Hive hive = new Hive(CameraOrigin, flowers);
        hive.CreateBee();

        Object Selected = null;

        while (!WindowShouldClose()) {
            float deltaTime = GetFrameTime();
            Ray MouseRay = GetScreenToWorldRay(GetMousePosition(), camera);

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

                    //this is the bee update stuff
                    if(hive!=null){
                        for(Bee bee: hive.Bees){
                            bee.draw(camera);
                            bee.update(deltaTime);

                            if(GetRayCollisionBox(MouseRay, bee.Collider).hit() && IsMouseButtonPressed(MOUSE_BUTTON_LEFT)){
                                Selected = bee;
                            }
                        }
                    }

                    //this is the flowerfield update stuff
                    if(flowers!=null){
                        for(Flower flower : flowers.flowerfield){
                            flower.DrawFlower(camera);

                            if(GetRayCollisionBox(MouseRay, flower.Collider).hit() && IsMouseButtonPressed(MOUSE_BUTTON_LEFT)){
                                Selected = flower;
                            }
                        }
                    }

                EndMode3D();

                if(Selected != null) DrawStats(Selected);
            EndDrawing();
        }
    }



    //the text can be made with DrawText
    //its probably good to have the full string of data layed out and then drawing it with one call
    static void DrawStats(Object Item){
        String status = "";

        switch (Item) {
            case Bee bee:
                status = "Object: " + bee.toString() + "\n" + "Name: " + bee.Name;
                break;

            case Location place:
                status = "Object: " + place.toString() + "\n" + "Name: " + place.Name;
                switch (place) {
                    case Hive hive:
                        status = status + "\n" + "Honey Count: " + Integer.toString(hive.HoneyCapacity);
                        break;
                    default:
                        break;
                }
            default:
                break;
        }

        DrawText(status, 20, 20, 20, BLACK);
    }   
}
