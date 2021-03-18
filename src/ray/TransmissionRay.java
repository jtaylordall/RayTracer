package ray;

import org.joml.Vector3f;

public class TransmissionRay extends Ray {

    public TransmissionRay(Vector3f origin, Vector3f initialDirection, Vector3f normal) {
        super(origin, null);
        direction = calculateReflection(initialDirection, normal);
    }

    public Vector3f calculateReflection(Vector3f d, Vector3f n) {
        Vector3f d1 = new Vector3f(d);
        d1.mul(-1);

        Vector3f n1 = new Vector3f(n);
        float nDotD = n.dot(d1);
        n1.mul(2 * nDotD);
        n1.sub(d1);

        return n1;
    }
}
