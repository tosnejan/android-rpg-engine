package meletos.rpg_game.text;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class Text {

    private Language lang;
    private SparseArray<String> text = new SparseArray<>();
    private SparseArray<String> itemNames = new SparseArray<>();
    private Context context;

    public Text(Context context) {
        this.context = context;
    }

    public Text(Language lang, Context context) {
        this.lang = lang;
        this.context = context;
        load();
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
                    } else {
                        itemNames.append(ID,line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
    }

    public String getText(int ID){
        return text.get(ID, "KeyNotFound");
    }

    public String getItemName(int ID) {
        return itemNames.get(ID, "KeyNotFound");
    }
}
