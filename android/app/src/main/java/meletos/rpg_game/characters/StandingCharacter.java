package meletos.rpg_game.characters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.dialog.DialogSwitcher;
import meletos.rpg_game.file_io.CharacterRepresentation;

public class StandingCharacter extends FatherCharacter {

    public StandingCharacter(int x, int y, Context context, String imagePath, int[][] dialogs, int actualDialog, boolean played, DialogSwitcher[] dialogSwitchers) {
        super(x, y, context, false);
        this.assetsFolder = imagePath;
        this.dialogs = dialogs;
        this.actualDialog = actualDialog;
        this.played = played;
        this.dialogSwitchers = dialogSwitchers;
        loadImage(imagePath, true);
    }

    public StandingCharacter(int x, int y, Context context, int[][] dialogs, int actualDialog, boolean played, DialogSwitcher[] dialogSwitchers) {
        super(x, y, context, false);
        this.dialogs = dialogs;
        this.actualDialog = actualDialog;
        this.played = played;
        this.dialogSwitchers = dialogSwitchers;
    }

    public StandingCharacter(int x, int y, String assetsFolder, Context context, String battleImageFolder, HashMap<String, Integer> stats) {
        super(x, y, assetsFolder, context, true, battleImageFolder, stats);
    }

    public StandingCharacter(int x, int y, Context context, HashMap<String,Integer> stats) {
        super(x, y, context, true, stats);
    }

    public void loadImage(String imagePath, boolean assets) {
        if (assets) {
            AssetManager am = context.getAssets();
            try {
                image = BitmapFactory.decodeStream(am.open(imagePath));
                image = Bitmap.createScaledBitmap(image, 96, 108, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File file = Environment.getExternalStorageDirectory();
            image = BitmapFactory.decodeFile(file.getAbsolutePath() + imagePath);
            image = Bitmap.createScaledBitmap(image, 96, 108, false);
        }
        imgHeigth = image.getHeight();
        imgWidth = image.getWidth();
        positionInformation = new PositionInformation(x, y, image.getHeight(), image.getWidth());
    }

    public void loadImage(String imagePath, boolean assets, String battleImagePath) {
        if (assets) {
            AssetManager am = context.getAssets();
            try {
                image = BitmapFactory.decodeStream(am.open(imagePath));
                image = Bitmap.createScaledBitmap(image, 96, 108, false);

                if (battleImagePath != null){
                    Bitmap temp = BitmapFactory.decodeStream(am.open(battleImagePath));
                    double ratio = temp.getHeight()/(double)temp.getWidth();
                    int w, h;
                    if (ratio >= 1){
                        h = screenHeight - 50;
                        w = (int) (h / ratio);
                    } else {
                        w = screenWidth / 4;
                        h = (int) (w * ratio);
                    }
                    characterImage = Bitmap.createScaledBitmap(temp, w, h, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File file = Environment.getExternalStorageDirectory();
            image = BitmapFactory.decodeFile(file.getAbsolutePath() + imagePath);
            image = Bitmap.createScaledBitmap(image, 96, 108, false);
            if (battleImagePath != null){
                Bitmap temp = BitmapFactory.decodeFile(file.getAbsolutePath() + battleImagePath);
                double ratio = temp.getHeight()/(double)temp.getWidth();
                int w, h;
                if (ratio >= 1){
                    h = screenHeight - 50;
                    w = (int) (h / ratio);
                } else {
                    w = screenWidth / 4;
                    h = (int) (w * ratio);
                }
                characterImage = Bitmap.createScaledBitmap(temp, w, h, false);
            }
        }
        imgHeigth = image.getHeight();
        imgWidth = image.getWidth();
        positionInformation = new PositionInformation(x, y, image.getHeight(), image.getWidth());
    }

    @Override
    public void update() {
        if (enemy){

        }else {
            if (dialogSwitchers.length > actualDialog) {
                if (dialogSwitchers[actualDialog].check(gameHandler, this)) {
                    actualDialog++;
                    played = false;
                }
            }
        }
    }
}
