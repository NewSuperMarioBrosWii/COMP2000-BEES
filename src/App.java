import static com.raylib.Raylib.*;

import java.util.ArrayList;

import static com.raylib.Colors.*;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Model;
import com.raylib.Raylib.Ray;
import com.raylib.Raylib.RayCollision;
import com.raylib.Raylib.Rectangle;
import com.raylib.Raylib.Vector3;

public class App {

    static boolean simulating = true;
    static boolean waspButtonEnabled=true;    
    final static float minDistance = 2.0f;
    final static float maxDistance = 40.0f;
    final static float rotationSpeed = 2.0f;
    final static float zoomSpeed = 8.0f;
    
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
        float yaw = 0.0f;
        float pitch = 0.2f;
        //---
        
        // this area im mainly using for initalising the scene so ex. the field, hives and models
        AssetManager assets = new AssetManager();

        Field flowers = new Field(CameraOrigin, 9f, assets);
        for(int i=0;i<100;i++){flowers.SpawnFlower();}//temp flower spawner

        Hive hive = new Hive(new Vector3().x(0.4f).y(2.1f).z(-1f), flowers, assets);
        for(int i=0;i<10;i++){hive.CreateBee();}//temp start bee spawner
        //hive.CreateQueen();

        Object Selected = null;
        Model treeModel = LoadModel("Assets/beetree.obj");

        //---

        // button rectange setup area

        Rectangle AddBeeButton  = new Rectangle();
                AddBeeButton.height(40).width(100);
                AddBeeButton.x(20).y(660);
        
        Rectangle PauseButtonrRectangle  = new Rectangle();
                PauseButtonrRectangle.height(40).width(100);
                PauseButtonrRectangle.x(130).y(660);

        Rectangle WaspButton  = new Rectangle();
                WaspButton.height(40).width(100);
                WaspButton.x(20).y(610);

        

        while (!WindowShouldClose()) {
            float deltaTime = GetFrameTime();

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

            //the ray has to be built after the camera moves, otherwise it points through last frame's view
            Ray MouseRay = GetScreenToWorldRay(GetMousePosition(), camera);

            //don't treat clicks on the UI buttons as clicks on the scene (this was clearing the selection)
            boolean mouseOverUI = CheckCollisionPointRec(GetMousePosition(), AddBeeButton)
                               || CheckCollisionPointRec(GetMousePosition(), PauseButtonrRectangle)
                               || CheckCollisionPointRec(GetMousePosition(), WaspButton);
            //---

            if (simulating) {
                if (hive != null) {
                    Hive.BeeCheck(); 
                    hive.Bees.parallelStream().forEach(bee -> bee.update(deltaTime));
                }
                
                if (flowers != null) {
                    flowers.fieldCheck();
                    flowers.wasps.parallelStream().forEach(wasp -> wasp.update(deltaTime));
                }
            }


            //This is where all the drawing is done
            BeginDrawing();
                ClearBackground(BLUE);
                
                //This is where the 3d is drawn
                BeginMode3D(camera);

                    //this model is the whole tree and grass plain
                    DrawModel(treeModel, CameraOrigin, 1.0f, RAYWHITE);

                    //this is the bee update stuff
                    if(hive!=null){
                        hive.DrawHive();
                        
                        for(Bee bee: hive.Bees){
                            bee.Draw(camera);
                        }
                    }

                    
                    if(flowers!=null){
                        //this might be to chunky
                        ArrayList<Flower> sortedFlowers = new ArrayList<>(flowers.flowerfield);
                        sortedFlowers.sort((f1, f2) -> {
                            float dx1 = f1.Position.x() - x;
                            float dy1 = f1.Position.y() - y;
                            float dz1 = f1.Position.z() - z;
                            float distSq1 = dx1 * dx1 + dy1 * dy1 + dz1 * dz1;

                            float dx2 = f2.Position.x() - x;
                            float dy2 = f2.Position.y() - y;
                            float dz2 = f2.Position.z() - z;
                            float distSq2 = dx2 * dx2 + dy2 * dy2 + dz2 * dz2;

                            return Float.compare(distSq2, distSq1); // Furthest first
                        });
                        
                        for(Flower flower : flowers.flowerfield){
                            flower.DrawFlower(camera);
                        }


                        //wasp stuff
                        for(EnemyWasp Wasp: flowers.wasps){
                            Wasp.Draw(camera);

                            /* if(GetRayCollisionBox(MouseRay, Wasp.Collider).hit() && IsMouseButtonPressed(MOUSE_BUTTON_LEFT)){
                                Selected = Wasp;
                            } */
                        }
                    }
                    

                EndMode3D();

                if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT) && !mouseOverUI) {
                    Object closestHit = null;
                    float closestDistance = Float.MAX_VALUE;

                    // Check Hive
                    if (hive != null) {
                        RayCollision hiveCollision = GetRayCollisionBox(MouseRay, hive.getCollider());
                        
                        if (hiveCollision.hit() && hiveCollision.distance() < closestDistance) {
                            closestDistance = hiveCollision.distance();
                            closestHit = hive;
                        }
                        // Check Bees
                        for (Bee bee : hive.Bees) {
                            RayCollision beeCollision = GetRayCollisionBox(MouseRay, bee.getCollider());
                            if (beeCollision.hit() && beeCollision.distance() < closestDistance) {
                                closestDistance = beeCollision.distance();
                                closestHit = bee;
                            }
                        }
                    }

                    // Check Flowers & Wasps
                    if (flowers != null) {
                        for (Flower flower : flowers.flowerfield) {
                            RayCollision flowerCollision = GetRayCollisionBox(MouseRay, flower.getCollider());
                            if (flowerCollision.hit() && flowerCollision.distance() < closestDistance) {
                                closestDistance = flowerCollision.distance();
                                closestHit = flower;
                            }
                        }

                        for (EnemyWasp wasp : flowers.wasps) {
                            RayCollision waspCollision = GetRayCollisionBox(MouseRay, wasp.getCollider());
                            if (waspCollision.hit() && waspCollision.distance() < closestDistance) {
                                closestDistance = waspCollision.distance();
                                closestHit = wasp;
                            }
                        }
                    }

                    // Update selection (allows clicking empty space to deselect, or updates to new selection)
                    Selected = closestHit;
                }

                if(Selected != null) DrawStats(Selected);
                //this is a example button that spawns a bee
                if(GuiButton(AddBeeButton,"Spawn Bee")==1){
                    hive.CreateBee();
                }

                if(waspButtonEnabled){
                    if(GuiButton(WaspButton,"Spawn Wasp")==1){
                        try{flowers.spawnWasp();
                        }   //wasps can't spawn if there are no bees
                        catch(RuntimeException e){
                            waspButtonEnabled=false;   //if there's no bees, the button is disabled
                            GuiSetState(STATE_DISABLED);
                        }
                    }
                }
                else{
                    GuiSetState(STATE_DISABLED);
                    GuiButton(WaspButton, "Spawn Wasp");
                    GuiSetState(STATE_NORMAL);
                }
                if(Hive.Bees!= null&&!Hive.Bees.isEmpty()){
                    waspButtonEnabled = true;
                }
                else{
                    waspButtonEnabled = false;
                }

                String c_playing = "pause";
                if(simulating){
                    c_playing = "Pause";
                }else{
                    c_playing = "Play";
                }
                
                if(GuiButton(PauseButtonrRectangle,c_playing)==1){
                    if(simulating){
                        simulating = false;
                    }
                    else{
                        simulating = true;
                    }
                }

            EndDrawing();
        }
    }

    void DrawButtons(){
        
    }



    //the text can be made with DrawText
    //its probably good to have the full string of data layed out and then drawing it with one call
    static void DrawStats(Object Item){
        String status = "";

        switch (Item) {
            case Bee bee -> {
                status = "Object: " + bee.toString() + "\n" + "Name: " + bee.Name;
                switch (bee) {
                    case BeeWorker worker -> {status+="\n" + "Nector Count: " + Float.toString(worker.getNectorCount());}
                    case QueenBee Queen -> {status+="\n" + "Is this the real life?";}
                    default->{}
                }
                break;
            }

            case Location place ->{
                status = "Object: " + place.toString() + "\n" + "Name: " + place.Name;
                switch (place) {
                    case Hive hive -> {status+="\n" + "Honey Count: " + Integer.toString(hive.HoneyCapacity)+
                                        "\n" + "Bee Count: " + Integer.toString(hive.Bees.size())+
                                        "\n"+"Total Bees Dead: "+hive.getTotalBeesKilled();}
                    case Flower flower -> {status+="\n" + "Pollen Count: " + Float.toString(flower.Pollen);}
                    default -> {}
                }
                break;
            }
            case Enemy enemy -> {
                status="Object: "+enemy.toString()+"\n"+"Name: "+enemy.name;
                switch (enemy){
                    case EnemyWasp wasp -> {status+="\n"+"Bees Killed: "+Integer.toString(wasp.getBeesKilled())
                                +"\n"+"Honey Stolen: "+Integer.toString(wasp.getHoneyStolen());}
                    default -> {}
                }
                break;
            }
            default->{}
        }

        DrawText(status, 20, 20, 20, BLACK);
    }   
    
}