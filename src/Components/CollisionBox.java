package Components;
import static com.raylib.Colors.RED;
import static com.raylib.Raylib.*;
import com.raylib.Raylib.BoundingBox;
import com.raylib.Raylib.Vector3;

/*
    This interface is to make a box collider around a object in 3d space
    it might be better for this to be a class and for a bee or something else can use this inside it
    i know you shouldn't have functionallity in a interface but i want to try this becuase i haven't used interfaces before 🙂
*/

public interface CollisionBox {
    boolean ShowColliders = true;
    BoundingBox getCollider(); // use this to get the box
 
    default void CreateUniformCollider(Vector3 Position, float Scale){
        CreateCollider(Position, new Vector3().x(Scale).y(Scale).z(Scale));
    }

    default void CreateCollider(Vector3 Position, Vector3 Scale){
        getCollider().min(new Vector3().x(Position.x() - Scale.x()/2)
                                       .y(Position.y() - Scale.y()/2)
                                       .z(Position.z() - Scale.z()/2))
                     .max(new Vector3().x(Position.x() + Scale.x()/2)
                                       .y(Position.y() + Scale.y()/2)
                                       .z(Position.z() + Scale.z()/2));
    }
    
    default void DrawColliderBox(BoundingBox collider){
        if(ShowColliders){DrawBoundingBox(getCollider(), RED);}
    }
}