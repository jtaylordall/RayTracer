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

    public ColorVec trace1(List<Intersectable> objects) {
        Ray tRay = null;
        for (Intersectable o : objects) {
            Intersection intersection = o.intersection(this);
            if (intersection.intersects() == Intersection.Interaction.OBJECT) {
                IntersectionPoint point = (IntersectionPoint) intersection;
                tRay = point.transRay;
                tRay.depth++;
                break;
            } else if (intersection.intersects() == Intersection.Interaction.LIGHT && depth > 0) {
                LightDirection l = (LightDirection) o;
                return l.od;
            }
        }
//        if (tRay != null) {
//            ColorVec colorVec = tRay.trace(objects,);
//            if (colorVec.equals(new ColorVec()))
////                colorVec.black();
//                colorVec = new ColorVec(Scene.ambientColor);
//            return colorVec;
//        }
        return new ColorVec(Scene.backgroundColor);
//        return new ColorVec();
    }

    public ColorVec trace(List<Intersectable> objects) {

        boolean isInShadow = false;
        IntersectionPoint closest = null;
        for (int i = 0; i < objects.size(); i++) {
            Intersectable o = objects.get(i);
            Intersection intersection = o.intersection(this);

            if (intersection.intersects() == Intersection.Interaction.OBJECT) {
                IntersectionPoint point = (IntersectionPoint) intersection;

//                if (i == 0) closest = point;
                if (closest == null || point.t < closest.t) {
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
            if (point.normal.dot(l) > 0)
                if (!o.equals(point.object))
                    if (i.intersects() == Intersection.Interaction.OBJECT)
                        return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Ray{" +
                "origin=" + origin +
                ", direction=" + direction +
                ", intersectionPoint=" + intersectionPoint +
                ", depth=" + depth +
                '}';
    }
}
