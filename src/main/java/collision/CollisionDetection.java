package collision;

import game.Transform;
import org.joml.Vector2f;
import utils.Utils;

public class CollisionDetection {

    public static boolean pointInBox(float pointX, float pointY, float boxX, float boxY, float boxWidth, float boxHeight) {
        return (pointX > boxX && pointX < boxX + boxWidth) &&
                (pointY > boxY && pointY < boxY + boxHeight);
    }

    public static boolean pointInBox(Point point, Transform transform){
        return pointInBox(point.getX(), point.getY(), transform.position.x, transform.position.y,
                transform.scale.x, transform.scale.y);
    }
//
    public static boolean pointInBox(Vector2f point, Vector2f p1, Vector2f p2, Vector2f p3, Vector2f p4){
        float tolerance = 0.1f;
        float area_rect = Utils.area_rect(p1, p2,p3,p4);
        float calc_area = Utils.area_triangle(point, p1, p2) + Utils.area_triangle(point, p1, p4)
                + Utils.area_triangle(point, p2, p3) + Utils.area_triangle(point, p3, p4);

        return Math.abs(area_rect - calc_area) <= 0.1f;
    }
//    // center
//    public static boolean pointInBox(float pointX, float pointY, float boxX, float boxY, float boxWidth, float boxHeight,
//                                     double theta) {
//
//    }


    public static boolean boxInBox(Transform box1, Transform box2) {
        return boxInBox(box1.position.x, box1.position.y, box1.scale.x, box1.scale.y,
                box2.position.x, box2.position.y, box2.scale.x, box2.scale.y);
    }

    public static boolean boxInBox(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 & y1 < y2 + h2 && h1 + y1 > y2;
    }
}
