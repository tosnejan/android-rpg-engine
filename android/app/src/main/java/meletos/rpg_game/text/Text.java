package meletos.rpg_game.text;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Text {

    private Language lang;
    private String[] text = new String[4];
    private Context context;

    public Text(Context context) {
        this(Language.ENG, context);
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
                    new InputStreamReader(context.getAssets().open(lang.filename)));
            String line;
            int ID = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    if (line.startsWith("ID:")) {
                        ID = Integer.parseInt(line.substring(4));
                    } else {
                        text[ID] = line;
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
    }

    public String getText(int ID){
        return text[ID];
    }
}
