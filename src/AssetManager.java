import static com.raylib.Raylib.LoadTexture;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import com.raylib.Raylib.Texture;

public class AssetManager {
    public Map<String, Texture> TextureMap = new HashMap<>();

    AssetManager(){
        TextureMap = loadPngTextures("Assets");
    }

    Texture GetTexture(String asset){
        return TextureMap.get(asset);
    }

    public static Map<String, Texture> loadPngTextures(String folderPath) {
        Map<String, Texture> textureMap = new HashMap<>();
        Path dir = Paths.get(folderPath);

        // Verify that the path exists and is a directory
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return textureMap;
        }

        try (Stream<Path> stream = Files.list(dir)) {
            stream.filter(file -> !Files.isDirectory(file))
                  .filter(file -> {
                      String fileName = file.getFileName().toString().toLowerCase();
                      return fileName.endsWith(".png");
                  })
                  .forEach(file -> {
                      String fullPath = file.toAbsolutePath().toString();
                      
                      // this gets the texutre and makes the name without the extension the key
                      String rawFileName = file.getFileName().toString();
                      int dotIndex = rawFileName.lastIndexOf('.');
                      String key = (dotIndex == -1) ? rawFileName : rawFileName.substring(0, dotIndex);
                      
                      Texture texture = LoadTexture(fullPath);
                      
                      if (texture.id() > 0) {
                          textureMap.put(key, texture);
                      }
                  });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return textureMap;
    }
}
