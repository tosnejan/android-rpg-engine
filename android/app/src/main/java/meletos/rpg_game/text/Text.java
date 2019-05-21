package meletos.rpg_game.text;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import meletos.rpg_game.GameView;


public class Text {

    private Language lang;
    private final SparseArray<String> text = new SparseArray<>();
    private final SparseArray<String> itemNames = new SparseArray<>();
    private final SparseArray<String> itemDescription = new SparseArray<>();
    private final SparseArray<String> dialogs = new SparseArray<>();
    private final Context context;
    private GameView gameView;

    public Text(Context context) {
        this.context = context;
    }

    public Text(Language lang, Context context) {
        this.lang = lang;
        this.context = context;
        load();
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    private void load() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(lang.getGui()), StandardCharsets.UTF_8));
            String line;
            int ID = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\uFEFF"))line = line.substring(1); //skip first character
                if (!line.equals("")) {
                    if (line.startsWith("ID:")) {
                        ID = Integer.parseInt(line.substring(4));
                    } else {
                        text.append(ID,line);
                    }
                }
            }
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(lang.getItems()), StandardCharsets.UTF_8));
            ID = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\uFEFF"))line = line.substring(1); //skip first character
                if (!line.equals("")) {
                    if (line.startsWith("ID:")) {
                        ID = Integer.parseInt(line.substring(4));
                    } else if (line.startsWith("NAME:")){
                        itemNames.append(ID,line.substring(6));
                    }else /*if (line.startsWith("DESCRIPTION:"))*/{
                        itemDescription.append(ID,line.substring(13));
                    }
                }
            }
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage());
                }
            }
        }
    }

    public void setLang(Language lang){
        this.lang = lang;
        load();
    }

    public Language getLang() {
        return lang;
    }

    public void changeLang(Language lang){
        this.lang = lang;
        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("language", lang.getID());
        editor.apply();
        load();
        if (gameView.hasGameHandler()){
            loadDialogs();
        }
    }

    public String getText(int ID){
        return text.get(ID, "TextNotFound");
    }

    public String getItemName(int ID) {
        return itemNames.get(ID, "NameNotFound");
    }

    public String getItemDescription(int ID) {
        return itemDescription.get(ID, "DescriptionNotFound");
    }

    public String getDialog(int ID){
        return dialogs.get(ID, "DialogNotFound");
    }

    public void loadDialogs(){
        BufferedReader reader = null;
        try {
            if (new File(gameView.getFileManager().getRootDirPath() + "/" + lang.getDialog()).exists()) {
                reader = new BufferedReader(new FileReader(gameView.getFileManager().getRootDirPath() + "/" + lang.getDialog()));
            } else {
                reader = new BufferedReader(new FileReader(gameView.getFileManager().getRootDirPath() + "/" + Language.ENG.getDialog()));
            }
            String line;
            int ID = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1); //skip first character
                if (!line.equals("")) {
                    if (line.startsWith("ID:")) {
                        ID = Integer.parseInt(line.substring(4));
                    } else {
                        dialogs.append(ID, line);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage());
                }
            }
        }
    }

}
