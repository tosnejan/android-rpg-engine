package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import meletos.rpg_game.Directions;
import meletos.rpg_game.Sprite;

/**
 * Works pretty well :D Also, the code is so much simpler than the one we saw :D
 */
public class JoyStick {

    private int radius;
    private float x, y, baseX, baseY;
    private Bitmap circle, ring;
    private int ringRadius;
    public boolean used = false;
    private double angle;

    public JoyStick(Bitmap circle, Bitmap ring) {
        this.circle = circle;
        this.ring = ring;
        radius = circle.getWidth()/2;
        ringRadius = ring.getWidth()/2;
    }

    public void setBase(float x, float y){
        this.baseX = x;
        this.baseY = y;
    }

    public void setPos(float x, float y){
        this.x = -(baseX-x);
        this.y = baseY-y;
        angle = cal_angle(this.x, this.y);
    }

    public void setUsed(boolean used) {
        this.used = used;
        if (!used){
            baseX = 0;
            baseY = 0;
            x = 0;
            y = 0;
            angle = 0;
        }
    }

    public Directions getDirection() {
        if(angle >= 247.5 && angle < 292.5 ) {
            return Directions.DOWN;
        } else if(angle >= 292.5 && angle < 337.5 ) {
            return Directions.DOWNRIGHT;
        } else if(angle >= 337.5 || angle < 22.5 ) {
            if (angle == 0) return Directions.NONE;
            else return Directions.RIGHT;
        } else if(angle >= 22.5 && angle < 67.5 ) {
            return Directions.UPRIGHT;
        } else if(angle >= 67.5 && angle < 112.5 ) {
            return Directions.UP;
        } else if(angle >= 112.5 && angle < 157.5 ) {
            return Directions.UPLEFT;
        } else if(angle >= 157.5 && angle < 202.5 ) {
            return Directions.LEFT;
        } else if(angle >= 202.5 && angle < 247.5 ) {
            return Directions.DOWNLEFT;
        }
        return Directions.NONE;
    }

    private double cal_angle(float x, float y) {
        if(x > 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if(x == 0 && y > 0)
            return 90;
        else if(x < 0 && y > 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x > 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        else if(x == 0 && y < 0)
            return 270;
        return 0;
    }

    public void draw(Canvas canvas) {
        if (used) {
            canvas.drawBitmap(circle, baseX - radius, baseY - radius, null);
            if (Math.sqrt((Math.pow(x,2)+Math.pow(y,2))) + ringRadius <= radius){
                canvas.drawBitmap(ring, baseX + x - ringRadius, baseY - y - ringRadius, null);
            } else {
                canvas.drawBitmap(ring, (radius - ringRadius) * (float)Math.cos(Math.toRadians(angle)) + baseX - ringRadius, -(radius - ringRadius) * (float)Math.sin(Math.toRadians(angle)) + baseY - ringRadius, null);
            }
        }
    }
}
