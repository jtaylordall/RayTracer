package shape;

import org.joml.Vector3f;
import instersection.Intersection;
import instersection.LightIntersection;
import instersection.NullIntersection;
import ray.Ray;

public class LightPlane extends Plane {

    public LightPlane(Vector3f normal, float distance) {
        super(normal, distance);
    }

    public LightPlane(Vector3f normal, float distance, ColorVec colorVec) {
        super(normal, distance);
        this.diffuseColor = colorVec;
    }

    @Override
    public Intersection calculateIntersection(Ray ray) {
        Intersection intersection = super.calculateIntersection(ray);
        if (intersection.getIntersectType() == Intersection.IntersectType.OBJECT) {
            return new LightIntersection();
        } else return new NullIntersection();
    }

    public Vector3f getDirectionToLight() {
        Vector3f l = new Vector3f(normal);
        l.mul(-1);
        return l;
    }
}