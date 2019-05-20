package meletos.rpg_game.menu;

import android.graphics.Bitmap;

import com.google.gson.GsonBuilder;

/**
 * Used for Json representing
 */
class StoryRepresentation {
    public String name;
    public String path;
    public boolean userSave;
}

/**
 * Class used mainly in GUI for representing stories.
 */
public class Story {

    private Bitmap image;
    private String name;
    private String path;
    private boolean userSave;

    public Story(Bitmap image, String name, String path, boolean userSave) {
        this.image = image;
        this.name = name;
        this.path = path;
        this.userSave = userSave;
    }

    public Story(String json, Bitmap image) {
        StoryRepresentation sr = new GsonBuilder().create().fromJson(json, StoryRepresentation.class);
        this.name = sr.name;
        this.image = image;
        this.path = sr.path;
        this.userSave = sr.userSave;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isUserSave() {
        return userSave;
    }
}
