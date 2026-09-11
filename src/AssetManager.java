
import static com.raylib.Raylib.LoadTexture;
import java.util.HashMap;
import com.raylib.Raylib.Texture;

//this class may be unused but it can potentially hold the functionality of loading the textures so each class doesn't need to do it sepereately so its easier to manage in memory

public class AssetManager {
    public HashMap<String, Texture> TextureMap = new HashMap<>();
    HashMap<String, String> TPL = new HashMap<>(); // Texture Path lookup

    AssetManager(){
        TPL.put("Bee", "Assets/Bee.png");
        TPL.put("Flower", "Assets/flower.png");
        LoadAllTextures(TPL);
    }

    void LoadAllTextures(HashMap<String, String> Paths){
        for(String i : Paths.keySet()){
            Texture n = LoadTexture(Paths.get(i));
            TextureMap.put(i, n);
        }
    }
}
