package ray;

import org.joml.Vector3f;
import shape.Intersectable;

public class IntersectionPoint implements Intersection {
    public Ray ray;
    public Vector3f point;
    public Vector3f normal;
    public float t;
    public Intersectable object;

    public IntersectionPoint(Ray ray, Vector3f point, Vector3f normal, float t, Intersectable object) {
        this.point = point;
        this.normal = normal;
        this.ray = ray;
        this.t = t;
        this.object = object;
    }

    @Override
    public boolean intersects() {
        return true;
    }
}
