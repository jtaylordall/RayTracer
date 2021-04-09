package instersection;

import org.joml.Vector3f;
import ray.Ray;
import ray.TransmissionRay;
import shape.Intersectable;

public class IntersectionPoint implements Intersection {
    public Ray srcRay;
    public TransmissionRay transRay;
    public Vector3f point;
    public Vector3f normal;
    public float t;
    public Intersectable object;

    public IntersectionPoint(Ray ray, TransmissionRay transRay, Vector3f point, Vector3f normal, float t, Intersectable object) {
        this.srcRay = ray;
        this.transRay = transRay;
        this.point = point;
        this.normal = normal;
        this.t = t;
        this.object = object;
    }

    @Override
    public IntersectType getIntersectType() {
        return IntersectType.OBJECT;
    }

}
