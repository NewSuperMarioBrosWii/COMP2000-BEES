import com.raylib.Raylib.BoundingBox;
import com.raylib.Raylib.Vector3;
import Components.CollisionBox;
abstract class Location implements CollisionBox{
    public String Name;
    public Vector3 Position = new Vector3();

    private final BoundingBox collider = new BoundingBox();
    @Override public BoundingBox getCollider() { return collider; }
    
    Location(String Name ,Vector3 Location){
        this.Name = Name;
        this.Position = Location;
    }
}
//this should probably be entity rather than location
//could also potentially be a interface rather than a abstract class