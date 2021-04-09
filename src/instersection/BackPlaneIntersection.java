package instersection;

import org.joml.Vector3f;

public class BackPlaneIntersection implements Intersection {

    public Vector3f point;

    public BackPlaneIntersection(Vector3f point) {
        this.point = point;
    }

    @Override
    public IntersectType getIntersectType() {
        return IntersectType.BACK_PLANE;
    }
}
