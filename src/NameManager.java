import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

//i stole this from my comp1010 project

public class NameManager {
    String path = "text-banks/valid-words.csv"; 
    ArrayList<String> dictonary;
    static public int wordLength;

    public NameManager(int wLength, String filePath){
        wordLength = wLength;
        dictonary = loadDictionary(filePath);
    }

    public static ArrayList<String> loadDictionary(String filePath) {
        ArrayList<String> slimDictionary = new ArrayList<>();
        // attempt to sift through file with BufferedReader to get a dictionary of available words

        if(wordLength == 0 || filePath == null || filePath == ""){
            return slimDictionary;
        }

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

    public String randomWord(){
        if(dictonary.isEmpty()){
            return "";
        }     
        Random r = new Random();
        int size = dictonary.size();
        String newWord = dictonary.get(r.nextInt(size));
        return newWord;
    }

    public boolean validWord(String word){
        if(word.length() != wordLength){
            return false;
        }
        return dictonary.contains(word);
    }
}
