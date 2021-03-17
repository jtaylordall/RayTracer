package ray;

import org.joml.Vector3f;

public class BackPlaneIntersection implements Intersection {

    public Vector3f point = new Vector3f();

    public BackPlaneIntersection(Vector3f point) {
        this.point = point;
    }

    @Override
    public Interaction intersects() {
        return Interaction.BACK_PLANE;
    }
}
