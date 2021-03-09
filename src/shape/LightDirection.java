package shape;

import org.joml.Vector3f;

public class LightDirection extends Plane {

    public LightDirection(Vector3f normal, float distance) {
        super(normal, distance, new ColorVec());
        this.color.white();
    }

    public LightDirection(Vector3f normal, float distance, ColorVec color) {
        super(normal, distance, color);
    }
}
