import static com.raylib.Raylib.*;
import java.util.ArrayList;
import static com.raylib.Colors.*;
import com.raylib.Raylib.Camera3D;
import com.raylib.Raylib.Model;
import com.raylib.Raylib.Ray;
import com.raylib.Raylib.RayCollision;
import com.raylib.Raylib.Rectangle;
import com.raylib.Raylib.Vector2;
import com.raylib.Raylib.Vector3;
import Components.CollisionBox;

public class App {

    static boolean simulating = true;
    static boolean waspButtonEnabled=true;    
    final static float minDistance = 2.0f;
    final static float maxDistance = 40.0f;
    final static float rotationSpeed = 2.0f;
    final static float zoomSpeed = 8.0f;
    
    public static void main(String[] args) throws Exception {
        InitWindow(1600, 900, "Bee Simulator");
        SetTargetFPS(60);

        //--- this is all camera setup releated stuff
        //the current setup is a orbiting camera that is controlled with the left and right arrow keys and a zoom with up and down
        Vector3 CameraOrigin = new Vector3();
        
        Camera3D camera = new Camera3D()
                .target(new Vector3())
                .up(new Vector3().x(0).y(1).z(0))
                .fovy(45).projection(CAMERA_PERSPECTIVE);
        
        
        float distance = 10.0f;
        float yaw = 0.0f;
        float pitch = 0.2f;
        //---
        
        AssetManager assets = new AssetManager();

        Field flowers = new Field(CameraOrigin, 9f, assets);
        for(int i=0;i<10;i++){flowers.SpawnFlower();}//temp flower spawner

        Hive hive = new Hive(new Vector3().x(0.4f).y(2.1f).z(-1f), flowers, assets);
        //for(int i=0;i<10;i++){hive.CreateBee();}//temp start bee spawner
        hive.CreateBee();
        //hive.CreateQueen();

        Object Selected = null;
        Model treeModel = LoadModel("Assets/beetree.obj");

        //---

        // button rectange setup area

        Rectangle AddBeeButton  = new Rectangle();
                AddBeeButton.height(40).width(100);
                AddBeeButton.x(20).y(840);
        
        Rectangle PauseButtonrRectangle  = new Rectangle();
                PauseButtonrRectangle.height(40).width(100);
                PauseButtonrRectangle.x(130).y(840);

        Rectangle WaspButton  = new Rectangle();
                WaspButton.height(40).width(100);
                WaspButton.x(240).y(840);

        
        // The Running Loop
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

            Vector2 MousePos = GetMousePosition();
            Ray MouseRay = GetScreenToWorldRay(MousePos, camera);
            //---
            
            boolean mouseOverUI = CheckCollisionPointRec(MousePos, AddBeeButton)
                               || CheckCollisionPointRec(MousePos, PauseButtonrRectangle)
                               || CheckCollisionPointRec(MousePos, WaspButton);

            //this is new where the bee's and wasps are updated done using streams
            if (simulating) {
                if (hive != null) {
                    hive.BeeCheck(); 
                    hive.myQueen.update(deltaTime);
                    hive.Bees.parallelStream().forEach(bee -> bee.update(deltaTime));
                }
                
                if (flowers != null) {
                    flowers.fieldCheck(deltaTime);
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

                    if(Selected != null && Selected instanceof CollisionBox box) {
                            box.DrawColliderBox(box.getCollider());
                    }
                    
                    if(flowers!=null){
                        //this might be to chunky but it sorts the flowers by distance so they draw nicer
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

                            return Float.compare(distSq2, distSq1);
                        });
                        
                        for(Flower flower : sortedFlowers){
                            flower.DrawFlower(camera);
                        }

                        //wasp stuff
                        for(EnemyWasp Wasp: flowers.wasps){
                            Wasp.Draw(camera);
                        }
                    }
                    

                EndMode3D();
                
                // object picker
                if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT) && !mouseOverUI) {
                    float closestDistance = Float.MAX_VALUE;

                    // Check Hive
                    if (hive != null) {
                        RayCollision hiveCollision = GetRayCollisionBox(MouseRay, hive.getCollider());
                        
                        if (hiveCollision.hit() && hiveCollision.distance() < closestDistance) {
                            closestDistance = hiveCollision.distance();
                            Selected = hive;
                        }
                        // Check Bees
                        for (Bee bee : hive.Bees) {
                            RayCollision beeCollision = GetRayCollisionBox(MouseRay, bee.getCollider());
                            if (beeCollision.hit() && beeCollision.distance() < closestDistance) {
                                closestDistance = beeCollision.distance();
                                Selected = bee;
                            }
                        }
                    }

                    // Check Flowers & Wasps
                    if (flowers != null) {
                        for (Flower flower : flowers.flowerfield) {
                            RayCollision flowerCollision = GetRayCollisionBox(MouseRay, flower.getCollider());
                            if (flowerCollision.hit() && flowerCollision.distance() < closestDistance) {
                                closestDistance = flowerCollision.distance();
                                Selected = flower;
                            }
                        }

                        for (EnemyWasp wasp : flowers.wasps) {
                            RayCollision waspCollision = GetRayCollisionBox(MouseRay, wasp.getCollider());
                            if (waspCollision.hit() && waspCollision.distance() < closestDistance) {
                                closestDistance = waspCollision.distance();
                                Selected = wasp;
                            }
                        }
                    }
                }
                //---

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


    //the text can be made with DrawText
    //its probably good to have the full string of data layed out and then drawing it with one call
    static void DrawStats(Object Item){
        String status = "";

        switch (Item) {
            case Bee bee -> {
                status = "Object: " + bee.toString() + "\n" + "Name: " + bee.Name;
                switch (bee) {
                    case BeeWorker worker -> {status+="\n" + "Nector Count: " + Float.toString(worker.getNectorCount())
                        +"\n"+"Health: "+Float.toString(worker.Health) + "\n" + "State: " + worker.getCurrentStateName();}
                    //case QueenBee Queen -> {status+="\n" + "Is this the real life?";}
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
                                +"\n"+"Honey Stolen: "+Integer.toString(wasp.getHoneyStolen())+
                                "\n"+"Health: "+Float.toString(wasp.health);}
                    default -> {}
                }
                break;
            }
            default->{}
        }

        DrawText(status, 20, 20, 20, BLACK);
    }   
    
}