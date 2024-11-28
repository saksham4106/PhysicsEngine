package scenes;


import callback.KeyEventListener;
import callback.MouseEventListener;
import collision.CollisionDetection;
import events.Event;
import events.EventBus;
import events.MouseClickEvent;
import game.Camera;
import objects.Line;
import objects.Rect;
import objects.RigidBody;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;


public class StartingMenuScene extends Scene {

    public StartingMenuScene() {
        super();
        this.camera = new Camera(new Vector2f(10, 10));
        this.sceneName = "starting_menu";
        EventBus.addListener(this);
    }

    public void init() {

//        ButtonObject enter_game = new ButtonObject(570, 250, "",
//                () -> {},
//                new Sprite(300, 100, Assets.getTexture("assets/scene/starting_menu/enter_game.png")));
//
//
//        GameObject heading1 = new GameObject(new Transform(0, 0, 1000, 100),
//                new Sprite(100, 100, ColorUtils.WHITE), 100);
//
//        addGameObjectToScene(heading1);
//        addGameObjectToScene(enter_game);

//        this.setBackground(Assets.getTexture("assets/textures/Scene1.png"));
        this.addGameObjectToScene(new Line(0,0, Math.PI / 2, 100));

    }
    int buffer = 20;
    boolean buttonPressed = false;
    Vector2f initPos = new Vector2f();
    Vector2f finalPos = new Vector2f();
    RigidBody selectedRect;

    @Override
    public void tick(float dt) {
        super.tick(dt);
        if((buffer < 0 || buffer == 20) && MouseEventListener.isMouseButtonPressed(GLFW_MOUSE_BUTTON_2)) {
            Vector2f center = MouseEventListener.getWorldCoord();
            System.out.println(center.x + ", " + center.y);
            Rect rect = new Rect(center.x, center.y, 40, 40);
            this.addPhysicsObjectToScene(rect);
            buffer = 20;
        }
        buffer--;
    }

    @Event.EventHandler
    public void mouseEvent(final MouseClickEvent event){
        if(event.getButton() == GLFW_MOUSE_BUTTON_1) {
            if(event.getAction() == GLFW_PRESS){

                if(!buttonPressed){
                    initPos = MouseEventListener.getWorldCoord();
                    buttonPressed = true;
                }

                for (RigidBody b : this.rigidBodies) {
                    if(CollisionDetection.pointInBox(MouseEventListener.getWorldCoord(), b.object.vertices[0], b.object.vertices[1],
                            b.object.vertices[2], b.object.vertices[3])){
                        this.selectedRect = b;
                        break;
                    }
                }
            }

            if(event.getAction() == GLFW_RELEASE && buttonPressed && this.selectedRect != null) {
                finalPos = MouseEventListener.getWorldCoord();
                buttonPressed = false;
                Vector2f vel = new Vector2f(finalPos).sub(initPos);
                this.selectedRect.addVel(vel);
            }
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        Vector2f velocity = new Vector2f();

        if(KeyEventListener.isKeyPressed(GLFW_KEY_W)){
            velocity.y += 1;

        }else if(KeyEventListener.isKeyPressed(GLFW_KEY_S)){
            velocity.y -= 1;

        }else if(KeyEventListener.isKeyPressed(GLFW_KEY_A)){
            velocity.x -= 1;

        }else if(KeyEventListener.isKeyPressed(GLFW_KEY_D)){
            velocity.x += 1;
        }

        if(!velocity.equals(0, 0)){
            velocity = velocity.normalize().mul(100 * dt);
            this.camera.position.add(velocity);

        }
    }
}
