package shape;

import org.joml.Vector3f;
import instersection.Intersection;
import ray.Ray;

public abstract class Intersectable {

    public ColorVec od;
    public ColorVec os;
    public float ka;
    public float kd;
    public float ks;
    public int kGls;

    public Intersectable() {
    }

    public Intersectable(ColorVec od, ColorVec os, float kd, float ks, float ka, int kGls) {
        this.od = od;
        this.os = os;
        this.ka = ka;
        this.kd = kd;
        this.ks = ks;
        this.kGls = kGls;
    }

    public void setColorProperties(ColorVec od, ColorVec os, float kd, float ks, float ka, int kGls) {
        this.od = od;
        this.os = os;
        this.kd = kd;
        this.ks = ks;
        this.ka = ka;
        this.kGls = kGls;
    }

    public void setColorProperties(Vector3f od, Vector3f os, float kd, float ks, float ka, int kGls) {
        this.od = ColorVec.toColorVec(od);
        this.os = ColorVec.toColorVec(os);
        this.kd = kd;
        this.ks = ks;
        this.ka = ka;
        this.kGls = kGls;
    }


    public ColorVec getColor(Vector3f direction, Vector3f normal, ColorVec reflectColor) {
        return ColorVec.calculateColor(direction, normal,
                od, os, reflectColor, kd, ks, ka, kGls);
    }

    public abstract Intersection calculateIntersection(Ray ray);

    protected void fudgePoint(Vector3f point, Vector3f normal) {
        float fudgeAmount = .001f;
        Vector3f n = new Vector3f(normal);
        n.normalize();
        n.mul(fudgeAmount);
        point.add(n);
    }


}
