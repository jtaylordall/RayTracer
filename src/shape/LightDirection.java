package shape;

import org.joml.Vector3f;
import ray.Intersection;
import ray.LightIntersection;
import ray.NullIntersection;
import ray.Ray;

public class LightDirection extends Plane {

    public LightDirection(Vector3f normal, float distance) {
        super(normal, distance);
    }

    @Override
    public Intersection intersection(Ray ray) {
        Intersection intersection = super.intersection(ray);
        if (intersection.intersects() == Intersection.Interaction.OBJECT) {
            return new LightIntersection();
        } else return new NullIntersection();
    }

    public Vector3f getDirectionToLight() {
        Vector3f l = new Vector3f(normal);
        l.mul(-1);
        return l;
    }
}