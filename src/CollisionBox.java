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
    public boolean DrawCollider = true;
    public BoundingBox Collider = new BoundingBox();

    public default void CreateUniformCollider(Vector3 Position, float Scale){
        Collider.min(new Vector3().x(Position.x() - Scale/2)
                                  .y(Position.y() - Scale/2)
                                  .z(Position.z() - Scale/2))
                .max(new Vector3().x(Position.x() + Scale/2)
                                  .y(Position.y() + Scale/2)
                                  .z(Position.z() + Scale/2));
        if(DrawCollider){DrawBoundingBox(Collider, RED);}
    }

    public default void CreateCollider(Vector3 Position, Vector3 Scale){
        Collider.min(new Vector3().x(Position.x() - Scale.x()/2)
                                  .y(Position.y() - Scale.y()/2)
                                  .z(Position.z() - Scale.z()/2))
                .max(new Vector3().x(Position.x() + Scale.x()/2)
                                  .y(Position.y() + Scale.y()/2)
                                  .z(Position.z() + Scale.z()/2));
        if(DrawCollider){DrawBoundingBox(Collider, RED);}
    }
}
