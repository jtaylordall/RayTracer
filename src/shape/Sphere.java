package shape;

import org.joml.Vector3f;
import ray.*;

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
    public Intersection intersection(Ray ray) {
        ray.direction.normalize();

        //  origin_center: oc = sc - ro
        Vector3f oc = new Vector3f(center).sub(ray.origin);

        //  closest_approach: tca = rd DOT oc
        float tca = ray.direction.dot(oc);

        float oc_len = oc.length();
        boolean roIsInside = oc_len < radius;
        if (tca < 0 && !roIsInside) return new NullIntersection(); //   ray does not intersect sphere

        //  tca dist to surface:    r^2 - ||oc||^2 + tca^2
        float thc_sq = (radius * radius) - (oc_len * oc_len) + (tca * tca);

        if (thc_sq < 0) return new NullIntersection();
        float thc = (float) Math.sqrt(thc_sq);
        float t;
        if (roIsInside)
            t = tca - thc;  //  t = tca - thc
        else
            t = tca + thc;  //  t = tca + thc

        Vector3f rayT = ray.getPointAtT(t);
        Vector3f normal = new Vector3f(rayT);
        normal.sub(center);
        normal.div(radius);

        fudgePoint(rayT, normal);

        TransmissionRay transRay = new TransmissionRay(rayT, ray.direction, normal);

        return new IntersectionPoint(ray, transRay, rayT, normal, t, this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sphere{");
        sb.append("radius=").append(radius);
        sb.append(", center=").append(center);
        sb.append(", od=").append(od);
        sb.append(", os=").append(os);
        sb.append(", ka=").append(ka);
        sb.append(", kd=").append(kd);
        sb.append(", ks=").append(ks);
        sb.append(", kGls=").append(kGls);
        sb.append('}');
        return sb.toString();
    }
}
