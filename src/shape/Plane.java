package shape;

import instersection.BackPlaneIntersection;
import instersection.Intersection;
import instersection.IntersectionPoint;
import instersection.NullIntersection;
import org.joml.Vector3f;
import ray.*;

public class Plane extends Intersectable {

    public Vector3f normal;
    public float distance;

    public Plane(Vector3f normal, float distance) {
        this.normal = normal;
        this.distance = distance;
    }

    @Override
    public Intersection calculateIntersection(Ray ray) {
        normal.normalize();
        Vector3f n = new Vector3f(normal);
        ray.direction.normalize();

        float vd = ray.direction.dot(n);
        if (vd == 0) return new NullIntersection();

        float vo = -(vd + distance);
        float t = vo / vd;

        Vector3f point = ray.getPointAtT(t);

        if (t < 0) return new NullIntersection();
        if (vd > 0) {
            n.mul(-1);
            fudgePoint(point, n);
            return new BackPlaneIntersection(point);
        }


        fudgePoint(point, n);

        TransmissionRay trans = new TransmissionRay(point, ray.direction, n);
        ray.intersectionPoint = new IntersectionPoint(ray, trans, point, n, t, this);
        return ray.intersectionPoint;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Plane{");
        sb.append("normal=").append(normal);
        sb.append(", distance=").append(distance);
        sb.append(", od=").append(diffuseColor);
        sb.append(", os=").append(specularColor);
        sb.append(", ka=").append(ka);
        sb.append(", kd=").append(kd);
        sb.append(", ks=").append(ks);
        sb.append(", kGls=").append(kGls);
        sb.append('}');
        return sb.toString();
    }
}
