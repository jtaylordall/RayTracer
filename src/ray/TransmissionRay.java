package ray;

import org.joml.Vector3f;

public class TransmissionRay extends Ray {
    public TransmissionRay(Vector3f origin, Vector3f initialDirection, Vector3f normal) {
        super(origin, null);
        direction = calculateReflection(initialDirection, normal);
    }

    public Vector3f calculateReflection(Vector3f d, Vector3f n) {

//        Vector3f r = new Vector3f(0, 0, 0);
        Vector3f d1 = new Vector3f(d);

        Vector3f n1 = new Vector3f(n);
        Vector3f n2 = new Vector3f(n);
        float dDotN = n2.dot(d1);
        n1.mul(2);
        n1.mul(dDotN);
        d1.sub(n1);

        d1.normalize();
        return d1;
    }
}
