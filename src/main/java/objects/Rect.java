package objects;

import game.GameObject;
import org.joml.Vector2f;
import renderer.Sprite;
import utils.ColorUtils;

public class Rect extends RigidBody {

    private float height, width;
    private float direction;

    // x,y are center
    public Rect(float x, float y, float height, float width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;

        this.object = new GameObject(x - width / 2, y - height/2, width, height,
                new Sprite(width, height, ColorUtils.WHITE));
        this.object.direction = direction;
//        this.object = new GameObject(x , y , width, height,
//                new Sprite(width, height, ColorUtils.WHITE));

    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
}
