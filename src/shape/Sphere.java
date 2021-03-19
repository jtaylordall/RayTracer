package shape;

import instersection.Intersection;
import instersection.IntersectionPoint;
import instersection.NullIntersection;
import org.joml.Vector3f;
import ray.Ray;
import ray.TransmissionRay;

public class Sphere extends Intersectable {

    public float radius;
    public Vector3f center;

    public Sphere(float radius) {
        this.radius = radius;
        this.center = new Vector3f(0, 0, 0);
    }

    public Sphere(float radius, Vector3f center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public Intersection calculateIntersection(Ray ray) {
        ray.direction.normalize();

        //  origin_center: oc = sc - ro
        Vector3f oc = new Vector3f(center).sub(ray.origin);

        //  closest_approach: tca = rd DOT oc
        float tca = ray.direction.dot(oc);

        float oc_len = oc.length();
        boolean roIsInside = oc_len < radius;
        if (tca < 0 && !roIsInside) return new NullIntersection(); //   ray does not intersect sphere

        //  tca dist to surface:    r^2 - ||oc||^2 + tca^2
        float thc_sq = (float) (Math.pow(radius, 2) - Math.pow(oc_len, 2) + Math.pow(tca, 2));

        //  no intersection
        if (thc_sq < 0) return new NullIntersection();
        float thc = (float) Math.sqrt(thc_sq);

        float t;
        if (roIsInside)
            t = tca + thc;  //  t = tca + thc
        else
            t = tca - thc;  //  t = tca - thc

        Vector3f point = ray.getPointAtT(t);
        Vector3f normal = calculateNormal(point);
        fudgePoint(point, normal);

        TransmissionRay transRay = new TransmissionRay(point, ray.direction, normal);

        return new IntersectionPoint(ray, transRay, point, normal, t, this);
    }

    Vector3f calculateNormal(Vector3f point) {
        Vector3f normal = new Vector3f(point);
        normal.sub(center);
        normal.div(radius);
        return normal;
    }

}
