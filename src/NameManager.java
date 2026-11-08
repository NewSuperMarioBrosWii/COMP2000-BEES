import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NameManager {
    ArrayList<String> dictonary;

    public NameManager(String filePath){
        dictonary = loadDictionary(filePath);
    }

    public static ArrayList<String> loadDictionary(String filePath) {
        ArrayList<String> slimDictionary = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String word;

            while ((word = br.readLine()) != null) {
                slimDictionary.add(word);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        if(slimDictionary.isEmpty()){
            System.err.println("No words are available");
        }
        return slimDictionary;
    }

    public String randomName(){
        if(dictonary.isEmpty()){return "";}     
        Random r = new Random();
        int size = dictonary.size();
        String newWord = dictonary.get(r.nextInt(size));
        return newWord;
    }
}
