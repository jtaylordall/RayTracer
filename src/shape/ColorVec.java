package shape;

import org.joml.Random;
import org.joml.Vector3f;
import org.joml.Vector3i;
import scene.Scene;

public class ColorVec extends Vector3i {

    public ColorVec() {
        background();
    }

    public ColorVec(int r, int g, int b) {
        this.x = r;
        this.y = g;
        this.z = b;
    }

    public ColorVec(ColorVec color) {
        this.x = color.x;
        this.y = color.y;
        this.z = color.z;
    }

    public void background() {
        this.x = 100;
        this.y = 100;
        this.z = 100;
    }

    public void random() {
        Random random = new Random();
        this.x = random.nextInt(255);
        this.y = random.nextInt(255);
        this.z = random.nextInt(255);
    }

    public void black() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public void white() {
        this.x = 255;
        this.y = 255;
        this.z = 255;
    }

    public void red() {
        this.x = 255;
        this.y = 0;
        this.z = 0;
    }

    public void green() {
        this.x = 0;
        this.y = 255;
        this.z = 0;
    }

    public void blue() {
        this.x = 0;
        this.y = 0;
        this.z = 255;
    }

    public void purple() {
        this.x = 196;
        this.y = 0;
        this.z = 255;
    }

    public void yellow() {
        this.x = 255;
        this.y = 255;
        this.z = 0;
    }

    public void orange() {
        this.x = 255;
        this.y = 128;
        this.z = 0;
    }

    public static Vector3f toVector3f(ColorVec color) {
        return new Vector3f(
                ((float) color.x) / 255.f,
                ((float) color.y) / 255.f,
                ((float) color.z) / 255.f
        );

    }

    public static ColorVec toColorVec(Vector3f v) {
        return new ColorVec(
                (int) (v.x * 255),
                (int) (v.y * 255),
                (int) (v.z * 255)
        );
    }

    public static ColorVec calculateColor1(Vector3f d, Vector3f n, ColorVec objDiffCol,
                                           ColorVec objSpecCol, ColorVec refCol,
                                           float kd, float ks, float ka, int kGls) {

        Vector3f l = Scene.lightPlane.getDirectionToLight();
        ColorVec lightCol = Scene.lightPlane.od;

        ColorVec color = new ColorVec(0, 0, 0);

        float ka1 = .4f * ka;
        float kd1 = 7f * kd;
        float ks1 = 1f * ks;

        Vector3f ambi = calculateAmbientTerm(Scene.lightPlane.od, objDiffCol);
//        ambi.mul(ka);
        ambi.mul(ka1);
        ambi.add(toVector3f(Scene.ambientColor));
        color.add(toColorVec(ambi));

        Vector3f diff = calculateDiffuseTerm(n, l, lightCol, objDiffCol);
//        diff.mul(kd);
        diff.mul(kd1);
        color.add(toColorVec(diff));

        Vector3f spec = calculateSpecularTerm(d, n, l, lightCol, objSpecCol, kGls);
//        spec.mul(ks / 2);
        spec.mul(ks1);
        color.add(toColorVec(spec));

        Vector3f ref = toVector3f(refCol);
        spec.mul(ks1);
//        ref.mul(ks / 2);
        color.add(toColorVec(ref));

        ColorVec white = new ColorVec();
        white.white();
        color.clip(Scene.ambientColor, white);
        return color;
    }

    public static ColorVec calculateColor(Vector3f d, Vector3f n, ColorVec objDiffCol,
                                          ColorVec objSpecCol, ColorVec refCol,
                                          float kd, float ks, float ka, int kGls) {

        Vector3f l = Scene.lightPlane.normal;
        ColorVec lightCol = Scene.lightPlane.od;

        ColorVec color = new ColorVec(0, 0, 0);

        float ka1 = .5f;
        float kd1 = 1.5f;
        float ks1 = 1.5f;
        float kr1 = .3f;
        int kGls1 = 1;

        Vector3f ambi = calculateAmbientTerm(Scene.lightPlane.od, objDiffCol);
        ambi.mul(ka * ka1);
        color.add(toColorVec(ambi));

        Vector3f diff = calculateDiffuseTerm(n, Scene.lightPlane.getDirectionToLight(),
                lightCol, objDiffCol);
        diff.mul(kd * kd1);
        color.add(toColorVec(diff));

        Vector3f spec = calculateSpecularTerm(d, n, l, lightCol, objSpecCol, kGls * kGls1);
        spec.mul(ks * ks1);
        color.add(toColorVec(spec));

        Vector3f ref = toVector3f(refCol);
        ref.mul(ks * kr1);
        color.add(toColorVec(ref));

        ColorVec white = new ColorVec();
        white.white();
        color.clip(Scene.ambientColor, white);
        return color;
    }

    public void add(ColorVec v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    private static Vector3f calculateAmbientTerm(ColorVec lightCol, ColorVec od) {
        Vector3f amb = toVector3f(lightCol);
        amb.mul(toVector3f(od));
        return amb;
    }

    private static Vector3f calculateDiffuseTerm(Vector3f n, Vector3f l, ColorVec lightCol, ColorVec od) {
        float nDotL = n.dot(l);
        nDotL = Math.max(nDotL, .0f);

        Vector3f dif = toVector3f(lightCol);
        dif.mul(toVector3f(od));
        dif.mul(nDotL);
        return dif;
    }

    private static Vector3f calculateSpecularTerm(Vector3f d, Vector3f n, Vector3f l, ColorVec lightCol, ColorVec os, int kGls) {
        d.normalize();
        n.normalize();
        l.normalize();
        float nDotL = l.dot(n);

        Vector3f r = new Vector3f(n);
        r.mul(2);
        r.mul(nDotL);
        r.x -= l.x;
        r.y -= l.y;
        r.z -= l.z;
        r.mul(-1);

        Vector3f spec = toVector3f(os);
        spec.mul(toVector3f(lightCol));

        float sDot = r.dot(d);
        sDot = Math.max(0.f, sDot);
        sDot = (float) Math.pow(sDot, kGls);
        spec.mul(sDot);
        return spec;
    }

    public void clip(ColorVec min, ColorVec max) {

        x = Math.min(x, max.x);
        y = Math.min(y, max.y);
        z = Math.min(z, max.z);

        x = Math.max(x, min.x);
        y = Math.max(y, min.y);
        z = Math.max(z, min.z);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
