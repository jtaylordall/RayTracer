package ray;


import org.joml.Vector3f;

public class PrimaryRay extends Ray {
    public Pixel pixel;

    public PrimaryRay(Pixel pixel, Vector3f origin, Vector3f direction) {
        super(origin, direction);
        this.pixel = pixel;
    }

}
