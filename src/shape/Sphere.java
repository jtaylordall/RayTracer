package shape;

import org.joml.Vector3f;
import ray.Intersection;
import ray.IntersectionPoint;
import ray.NullIntersection;
import ray.Ray;

import java.util.Random;

public class Sphere implements Intersectable {

    public float radius;
    public Vector3f center;
    public ColorVec color;

    public Sphere(float radius) {
        this.radius = radius;
        this.center = new Vector3f(0, 0, 0);
        this.color = new ColorVec();
    }

    public Sphere(float radius, Vector3f center) {
        this.radius = radius;
        this.center = center;
        this.color = new ColorVec();
    }

    public Sphere(float radius, Vector3f center, ColorVec color) {
        this.radius = radius;
        this.center = center;
        this.color = color;
    }

    public Sphere(float radius, float x, float y, float z, int r, int g, int b) {
        this.radius = radius;
        this.center = new Vector3f(x, y, z);
        this.color = new ColorVec(r, g, b);
    }

    public static Sphere newRandomSphere(int r_bound, int x_bound, int y_bound, int z_bound) {
        Random random = new Random();

        ColorVec colorVec = new ColorVec();
        colorVec.random();

        Sphere sphere = new Sphere(
                random.nextInt(r_bound),
                new Vector3f(
                        random.nextInt(x_bound),
                        random.nextInt(y_bound),
                        random.nextInt(z_bound)),
                colorVec);

        return sphere;
    }

    @Override
    public Intersection intersection(Ray ray) {
        ray.direction.normalize();
        if (ray.pixel.screenCoordinate.x == 0 && ray.pixel.screenCoordinate.y == 0) {
            System.out.println("--");
        }
        //  origin_center: oc = sc - ro
        Vector3f oc = new Vector3f(center).sub(ray.origin);

        //  closest_approach: tca = rd DOT oc
        Vector3f temp = new Vector3f(ray.direction);
        float tca = temp.dot(oc);

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
        t -= .2f;

        Vector3f rayT = ray.getPointAtT(t);
        Vector3f normal = new Vector3f(rayT);
        normal.sub(center);
        normal.div(radius);

        return new IntersectionPoint(ray, rayT, normal, t, this);
    }

    @Override
    public ColorVec getColor() {
        return color;
    }
}
