package objects;

import game.GameObject;
import org.joml.Vector2f;

public class RigidBody {
    public float x,y;
    public float angular_vel = 1f;
    public float mass = 1f;

    public Vector2f velocity = new Vector2f();
    public Vector2f direction = new Vector2f();
    public Vector2f acceleration = new Vector2f();


    public float Cd = -0.01f;
    public GameObject object;
    private float crossSecArea; // In direction of velocity
    public float gravity = 100f;

    public void tick(float dt){
        this.acceleration.add(0, -gravity);

        Vector2f diag1 = new Vector2f(this.object.vertices[0]).sub(this.object.vertices[2]);
        Vector2f diag2 = new Vector2f(this.object.vertices[1]).sub(this.object.vertices[3]);
        Vector2f perpVelocity = new Vector2f(direction).perpendicular().normalize();

        this.crossSecArea = Math.abs((Math.abs(diag1.dot(direction)) >= Math.abs(diag2.dot(direction)) ? diag2 : diag1).dot(perpVelocity));

        if(!this.velocity.equals(0,0)){
            this.acceleration.add((new Vector2f(velocity).normalize()).mul(0.5f * crossSecArea * Cd * dt * velocity.dot(velocity) / mass));
        }

        velocity.y += acceleration.y * dt;
        velocity.x += acceleration.x * dt;
        velocity.normalize(this.direction);

        this.addDist(new Vector2f(velocity).mul(dt));
        this.rotate(angular_vel * dt);

        this.acceleration.zero();
    }

    public void addVel(Vector2f v){
        this.velocity.add(v);
    }

    public void addForce(Vector2f force){
        this.acceleration.add(force.div(mass));
    }

    public void addDist(Vector2f dist){
        this.y += dist.y;
        this.x += dist.x;
        this.object.addPosition(dist.x, dist.y);
    }

    public void rotate(double angle){
        this.object.addAngle(angle);
        this.object.markDirty(true);
    }

}
