package shape;

import org.joml.Vector3f;
import ray.*;

public class Plane extends Intersectable {

    public Vector3f normal;
    public float distance;

    public Plane(Vector3f normal, float distance) {
        this.normal = normal;
        this.distance = distance;
    }

    public void setColorProperties(ColorVec od, ColorVec os, float ka, float kd, float ks, int kGls) {
        this.od = od;
        this.os = os;
        this.ka = ka;
        this.kd = kd;
        this.ks = ks;
        this.kGls = kGls;
    }

    @Override
    public Intersection intersection(Ray ray) {
        normal.normalize();
        ray.direction.normalize();
        Vector3f p = new Vector3f(normal);
        float vd = p.dot(ray.direction);

        if (vd >= 0) return new NullIntersection();   // if one sided
        if (vd == 0) return new NullIntersection();

        float vo = -(vd + distance);
        float t = vo / vd;

        if (t < 0) return new NullIntersection();
        if (vd > 0) normal.mul(-1);
        Vector3f point = ray.getPointAtT(t);

        fudgePoint(point, normal);

        TransmissionRay trans = new TransmissionRay(point, ray.direction, normal);
        ray.intersectionPoint = new IntersectionPoint(ray, trans, point, normal, t, this);
        return ray.intersectionPoint;
    }
}
