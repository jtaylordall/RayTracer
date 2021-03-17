package ray;

import org.joml.Vector2i;
import org.joml.Vector3f;
import shape.ColorVec;

public class Pixel {
    public Vector2i screenCoordinate;
    public PrimaryRay primaryRay;
    public ColorVec color;

    public Pixel(Vector2i screenCoordinate, Vector3f origin, Vector3f direction) {
        this.screenCoordinate = screenCoordinate;
        this.color = new ColorVec();
        this.primaryRay = new PrimaryRay(this,
                new Vector3f(origin),
                new Vector3f(direction));
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "screenCoordinate=" + screenCoordinate +
                ", primaryRay=" + primaryRay +
                ", color=" + color +
                '}';
    }
}
