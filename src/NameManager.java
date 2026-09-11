import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

//i stole this from my comp1010 project

public class NameManager {
    String path = "text-banks/valid-words.csv"; 
    String chosenWord;
    ArrayList<String> dictonary;
    static public int wordLength;

    public NameManager(int wLength){
        wordLength = wLength;
        dictonary = loadDictionary(path);
        //this.chosenWord = randomWord();
    }

    public NameManager(int wLength, String filePath){
        wordLength = wLength;
        dictonary = loadDictionary(filePath);

        if(dictonary == null || dictonary.isEmpty()){
            System.err.println("cannot get dictionary");
            chosenWord = null;
        }
        else{
            this.chosenWord = randomWord();
        }
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
                //word = word.trim();
                //if (word.length() == wordLength) {
                    //slimDictionary.add(word.toLowerCase());
                //}
                slimDictionary.add(word);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        if(slimDictionary.isEmpty()){
            System.err.println("No words are available");
        }

        return slimDictionary; //returns a arraylist of all words that are at word length
    }

    public String randomWord(){
        if(dictonary.isEmpty()){
            return "";
        }     
        Random r = new Random();
        int size = dictonary.size();
        String newWord = dictonary.get(r.nextInt(size));
        
        this.chosenWord = newWord.toLowerCase();
        return newWord;
    }

    public boolean validWord(String word){
        if(word.length() != wordLength){
            return false;
        }
        
        return dictonary.contains(word);
    }
}
