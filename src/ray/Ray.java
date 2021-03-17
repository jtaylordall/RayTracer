package ray;

import org.joml.Vector3f;
import shape.ColorVec;
import shape.Intersectable;
import shape.LightDirection;
import tracer.Scene;

import java.util.List;

public class Ray {
    public Vector3f origin;
    public Vector3f direction;
    public IntersectionPoint intersectionPoint;
    public int depth;

    public Ray(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
        depth = 0;
    }

    public Vector3f getPointAtT(float t) {
        direction.normalize();
        Vector3f v = new Vector3f(0, 0, 0);
        v.add(direction);
        v.mul(t);
        v.add(origin);
        return v;
    }

    public ColorVec trace(List<Intersectable> objects, Pixel pixel) {

        if (pixel.screenCoordinate.x == 500 && pixel.screenCoordinate.y == 500) {
            System.out.println();
//            ColorVec colorVec = new ColorVec();
//            colorVec.red();
//            return colorVec;
        }

        boolean isInShadow = false;
        IntersectionPoint closest = null;
        for (int i = 0; i < objects.size(); i++) {
            Intersectable o = objects.get(i);
            Intersection intersection = o.intersection(this);

            if (intersection.intersects() == Intersection.Interaction.OBJECT) {
                IntersectionPoint point = (IntersectionPoint) intersection;

                if (i == 0) closest = point;
                if (closest == null || point.point.z > closest.point.z) {
                    closest = point;

                    isInShadow = isInShadow(point, objects, Scene.lightDirection);
                }
            }
        }
        if (closest != null) {
            if (isInShadow) return Scene.ambientColor;
            Vector3f view = new Vector3f(origin);
            view.sub(closest.point);
            return closest.object.getColor(view, closest.normal,
                    Scene.lightDirection.getDirectionToLight(), Scene.lightDirection.od);
        } else return Scene.backgroundColor;
    }

    private boolean isInShadow(IntersectionPoint point,
                               List<Intersectable> objects,
                               LightDirection lightDirection) {

        Vector3f l = lightDirection.getDirectionToLight();

        for (Intersectable o : objects) {
            Intersection i = o.intersection(new Ray(point.point, l));
            if (point.normal.dot(l) > 0) {
                if (i.intersects() == Intersection.Interaction.OBJECT ||
                        i.intersects() == Intersection.Interaction.BACK_PLANE) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ray{");
        sb.append("origin=").append(origin);
        sb.append(", direction=").append(direction);
        sb.append(", intersectionPoint=").append(intersectionPoint);
        sb.append(", depth=").append(depth);
        sb.append('}');
        return sb.toString();
    }
}
