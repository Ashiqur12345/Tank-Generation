import java.io.*;

/**
 * Created by Ashiqur Rahman on 4/16/2016.
 */
public class FileManagement {

    private File score, game;
    private int topScore;
    private String topScorer;


    //Constructor
    public FileManagement() {
        score = new File("score.save");
        game = new File("game.save");
    }


    //    Score Management
    public void writeScore(String str){

        str = EncryptUtils.base64encode(str);

        if(!score.exists()){
            try {
                score.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(score));
            bw.write(str);
            if(bw != null){
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getTopScore(){
        readScoreFile();
        return topScore;
    }
    public String getTopScorer(){
        readScoreFile();
        return topScorer;
    }
    public void readScoreFile(){

        String str = null;

        if(!score.exists()){
            topScorer = "No One";
            topScore = 0;
            return;
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(score));
            str = br.readLine();
            if(br != null){
                br.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        str = EncryptUtils.base64decode(str);
        topScorer = str.split("_")[0];
        topScore = Integer.parseInt(str.split("_")[1]);
    }


    //    Saved Game Management
    public Object getSavedGame(){
        Object object;
        SaveGame s;
        if(! game.exists()){
            return null;
        }
        try{
            FileInputStream fis = new FileInputStream(game);
            ObjectInputStream ois = new ObjectInputStream(fis);
            object = ois.readObject();
            if(fis != null){
                fis.close();
            }

        }catch (FileNotFoundException e) {
            return null;
        }catch (IOException ex){
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
        s = (SaveGame) object;
        return s;
    }
    public boolean saveGame(SaveGame ob){

        if(!game.exists()){
            try {
                game.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(game);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ob);
            if(fos != null){
                fos.close();
            }
            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }
        catch (IOException ex){
            return false;
        }
    }
    public boolean gameFileExist(){
        if(game.exists()){
            return true;
        }
        else{
            return false;
        }
    }

}
