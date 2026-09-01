import com.raylib.Raylib.Vector3;
import Components.CollisionBox;
abstract class Location implements CollisionBox{
    public String Name;
    public Vector3 Position = new Vector3();
    
    Location(String Name ,Vector3 Location){
        this.Name = Name;
        this.Position = Location;
    }
}
