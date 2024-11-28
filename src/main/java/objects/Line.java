package objects;

import game.GameObject;
import game.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Sprite;

public class Line extends GameObject {
    public Vector2f start, end;
    public float thickness = 2;

    public Line(float startX, float startY, float endX, float endY){
//        this.start = new Vector2f(startX, startY);
//        float length =;
//        this.end = new Vector2f(endX, endY);
//        Vector2f center = new Vector2f(this.start).add(this.end).div(2);
//        float slope = ;
//        this.direction = Math.atan(slope);


//        this.setTransform(new Transform(center.x - thickness / 2, center.y - length/2, thickness, length));
        this((startX + endX) /2, (startY + endY) / 2, Math.atan((endY - startY) / (endX - startX)),
                (float) Math.sqrt(Math.pow(startY - endY, 2) + Math.pow(startX - endX, 2)));
    }

    public Line(float centerX, float centerY, double angle, float length){
        this.zIndex = 10;
        this.sprite = new Sprite(new Vector4f(1,1,1,1));
        this.setTransform(new Transform(centerX - thickness /2, centerY - length / 2, thickness, length));
        this.direction = angle;


        this.markDirty(true);
        this.sprite.width = transform.scale.x;
        this.sprite.height = transform.scale.y;
    }
}
