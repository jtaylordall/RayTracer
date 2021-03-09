package ray;

import org.joml.Vector3f;

public class Ray {
    public Pixel pixel;
    public Vector3f origin;
    public Vector3f direction;
    public IntersectionPoint intersectionPoint;

    public Ray(Pixel pixel, Vector3f origin, Vector3f direction) {
        this.pixel = pixel;
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3f getPointAtT(float t) {
        direction.normalize();
        Vector3f v = new Vector3f(0, 0, 0);
        v.add(direction);
        v.mul(t);
        v.add(origin);
        return v;
    }
}
