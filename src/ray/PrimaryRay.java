package ray;


import org.joml.Vector3f;

public class PrimaryRay extends Ray {
    public Ray transmissionRay;
    public Ray shadowRay;

    public PrimaryRay(Pixel pixel, Vector3f origin, Vector3f direction) {
        super(pixel, origin, direction);
    }
}
