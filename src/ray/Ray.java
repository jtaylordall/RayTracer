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
    public static int MAX_DEPTH = 6;

    public Ray(Vector3f origin, Vector3f direction) {
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

    public ColorVec trace(List<Intersectable> objects, int depth) {
        boolean isInShadow = false;
        IntersectionPoint closest = null;
        for (int i = 0; i < objects.size(); i++) {
            Intersectable o = objects.get(i);
            Intersection intersection = o.intersection(this);

            if (intersection.intersects() == Intersection.Interaction.OBJECT) {
                IntersectionPoint point = (IntersectionPoint) intersection;

                if (i == 0) closest = point;

                if (closest == null) {
                    closest = point;
                    isInShadow = isInShadow(point, objects, Scene.lightDirection);

                } else {
                    Vector3f dP = new Vector3f(point.point);
                    dP.sub(origin);
                    Vector3f dC = new Vector3f(closest.point);
                    dC.sub(origin);

                    if (dP.length() < dC.length()) {
                        closest = point;
                        isInShadow = isInShadow(point, objects, Scene.lightDirection);
                    }
                }
            }
        }
        if (closest == null) {
            if (depth > 0 && depth < MAX_DEPTH) {
                Intersection intersection = Scene.lightDirection.intersection(this);
                if (intersection.intersects() == Intersection.Interaction.LIGHT)
//                    return Scene.lightDirection.od;
                    return new ColorVec(0, 0, 0);
            }
            return Scene.backgroundColor;

        } else {

//            if (isInShadow && depth == 0) return Scene.ambientColor;
//            else if (isInShadow && depth > 0) return new ColorVec(0, 0, 0);
            if (isInShadow) return Scene.ambientColor;
            else if (depth == MAX_DEPTH) return new ColorVec(0, 0, 0);

            Vector3f view = new Vector3f(origin);
            view.sub(closest.point);

            ColorVec reflectCol;
            if (closest.object.ks > 0f) {
                reflectCol = closest.transRay.trace(objects, depth + 1);
            } else reflectCol = closest.object.od;

            ColorVec objectCol = closest.object.getColor(view, closest.normal, reflectCol);

            return objectCol;
        }
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
        sb.append('}');
        return sb.toString();
    }
}
