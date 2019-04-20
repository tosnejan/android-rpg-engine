package meletos.rpg_game.menu;

import android.graphics.Bitmap;

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

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isUserSave() {
        return userSave;
    }
}
