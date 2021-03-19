package ray;

import instersection.Intersection;
import instersection.IntersectionPoint;
import org.joml.Vector3f;
import scene.Scene;
import shape.ColorVec;
import shape.Intersectable;
import shape.LightPlane;

import java.util.List;

public class Ray {
    public Vector3f origin;
    public Vector3f direction;
    public IntersectionPoint intersectionPoint;

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
            Intersection intersection = o.calculateIntersection(this);

            if (intersection.getIntersectType() == Intersection.IntersectType.OBJECT) {
                IntersectionPoint point = (IntersectionPoint) intersection;

                if (i == 0) closest = point;

                if (closest == null) {
                    closest = point;
                    isInShadow = isInShadow(point, objects, Scene.lightPlane);

                } else {
                    Vector3f dP = new Vector3f(point.point);
                    dP.sub(origin);
                    Vector3f dC = new Vector3f(closest.point);
                    dC.sub(origin);

                    if (dP.length() < dC.length()) {
                        closest = point;
                        isInShadow = isInShadow(point, objects, Scene.lightPlane);
                    }
                }
            }
        }
        if (closest == null) {
            if (depth > 0 && depth < Scene.MAX_RAY_DEPTH) {
                Intersection intersection = Scene.lightPlane.calculateIntersection(this);
                if (intersection.getIntersectType() == Intersection.IntersectType.LIGHT)
                    return new ColorVec(0, 0, 0);
            }
            return Scene.backgroundColor;

        } else {

            if (isInShadow) return Scene.ambientColor;
            else if (depth == Scene.MAX_RAY_DEPTH) return new ColorVec(0, 0, 0);

            Vector3f view = new Vector3f(origin);
            view.sub(closest.point);

            ColorVec reflectCol;
            if (closest.object.ks > 0f) {
                reflectCol = closest.transRay.trace(objects, depth + 1);
            } else reflectCol = closest.object.od;

            return closest.object.getColor(view, closest.normal, reflectCol);
        }
    }

    private boolean isInShadow(IntersectionPoint point,
                               List<Intersectable> objects,
                               LightPlane lightDirection) {

        Vector3f l = lightDirection.getDirectionToLight();

        for (Intersectable o : objects) {
            Intersection i = o.calculateIntersection(new Ray(point.point, l));
            if (point.normal.dot(l) > 0) {
                if (i.getIntersectType() == Intersection.IntersectType.OBJECT ||
                        i.getIntersectType() == Intersection.IntersectType.BACK_PLANE) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "Ray{" + "origin=" + origin +
                ", direction=" + direction +
                ", intersectionPoint=" + intersectionPoint +
                '}';
    }
}
