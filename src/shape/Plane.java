package shape;

import org.joml.Vector3f;
import ray.Intersection;
import ray.IntersectionPoint;
import ray.NullIntersection;
import ray.Ray;

public class Plane implements Intersectable {

    public Vector3f normal;
    public float distance;
    public ColorVec color;

    public Plane(Vector3f normal, float distance) {
        this.normal = normal;
        this.distance = distance;
        this.color = new ColorVec();
    }

    public Plane(Vector3f normal, float distance, ColorVec color) {
        this.normal = normal;
        this.distance = distance;
        this.color = color;
    }

    @Override
    public Intersection intersection(Ray ray) {
        normal.normalize();
        ray.direction.normalize();
        Vector3f p = new Vector3f(normal);
        float vd = p.dot(ray.direction);

//        if (vd >= 0) return new NullIntersection();   // if one sided
        if (vd == 0) return new NullIntersection();

        float vo = -(vd + distance);
        float t = vo / vd;

        if (t < 0) return new NullIntersection();
        if (vd > 0) normal.mul(-1);

        ray.intersectionPoint = new IntersectionPoint(ray, ray.getPointAtT(t), normal, t, this);
        return ray.intersectionPoint;
    }

    @Override
    public ColorVec getColor() {
        return color;
    }

}
