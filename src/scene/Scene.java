package scene;

import export.ImageWriter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import ray.Ray;
import shape.ColorVec;
import shape.Intersectable;
import shape.LightPlane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scene {
    public String name;

    public static LightPlane lightPlane;
    private final List<Intersectable> objects;

    public static Vector3f lookFrom;
    private Vector3f lookAt;
    private Vector3f lookUp;
    private float fov;

    public static ColorVec ambientColor;
    public static ColorVec backgroundColor;

    public static int MAX_RAY_DEPTH = 2;

    public Scene() {
        objects = new ArrayList<>();
    }

    public Scene(String name) {
        this.name = name;
        this.objects = new ArrayList<>();
    }

    public void setLightPlane(LightPlane lightDirection) {
        Scene.lightPlane = lightDirection;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxRayDepth(int maxRayDepth) {
        Scene.MAX_RAY_DEPTH = maxRayDepth;
    }

    public void setLookFrom(Vector3f lookFrom) {
        Scene.lookFrom = lookFrom;
    }

    public void setLookAt(Vector3f lookAt) {
        this.lookAt = lookAt;
    }

    public void setLookUp(Vector3f lookUp) {
        this.lookUp = lookUp;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public void setAmbientColor(ColorVec ambientColor) {
        Scene.ambientColor = ambientColor;
    }

    public void setBackgroundColor(ColorVec backgroundColor) {
        Scene.backgroundColor = backgroundColor;
    }

    private static class ViewPlane {
        Matrix4f t;
        Matrix4f m;
    }

    public ViewPlane createViewPlane() {
        lookUp.normalize();

        Vector3f vpn = new Vector3f(lookFrom);
        vpn.sub(lookAt);
        vpn.normalize();

        Vector3f vup = lookUp;
        vup.normalize();

        Vector3f n = new Vector3f(vpn);

        Vector3f u = new Vector3f(vup);
        u.cross(n);

        Vector3f v = new Vector3f(n);
        v.cross(u);

        u.normalize();
        v.normalize();
        n.normalize();

        ViewPlane viewPlane = new ViewPlane();
        viewPlane.m = new Matrix4f(
                1, 0, 0, -lookAt.x,
                0, 1, 0, -lookAt.y,
                0, 0, 1, -lookAt.z,
                0, 0, 0, 1
        );
        viewPlane.t = new Matrix4f(
                u.x, u.y, u.z, 0,
                v.x, v.y, v.z, 0,
                n.x, n.y, n.z, 0,
                0, 0, 0, 1
        );
        return viewPlane;
    }

    private Vector3f calculateOrigin(float x, float y, float z, int w, int h, ViewPlane viewPlane) {
        Vector4f origin = new Vector4f(x, y, z, 1);
        origin.x /= w;
        origin.y /= h;
        origin.mul(viewPlane.t);
        origin.mul(viewPlane.m);
        return new Vector3f(origin.x, origin.y, origin.z);
    }

    private Vector3f calculateDirection(float px, float py, ViewPlane viewPlane) {
        Vector3f direction = new Vector3f(px, py, -1f);
        direction.normalize();
        Vector4f d1 = new Vector4f(direction, 1);
        d1.mul(viewPlane.t);
        d1.mul(viewPlane.m);
        direction = new Vector3f(d1.x, d1.y, d1.z);
        direction.normalize();
        return direction;
    }

    public void addObject(Intersectable o) {
        objects.add(o);
    }

    public void addObjects(Collection<Intersectable> objects) {
        this.objects.addAll(objects);
    }

    public void rayTrace(int w, int h, ImageWriter imageWriter) {
        imageWriter.open();
        ViewPlane viewPlane = createViewPlane();

        int wHalf = w / 2;
        int hHalf = h / 2;

        float tan = (float) Math.tan(fov * Math.PI / 180);
        for (int y = h - 1; y >= 0; y--) {
            for (int x = 0; x < w; x++) {

                float x1 = x - wHalf + .5f;
                float y1 = y - hHalf + .5f;
                float z1 = lookFrom.z;

                float aspectRatio = ((float) w) / ((float) h);
                float px = ((x1 / w)) * tan * aspectRatio;
                float py = ((y1 / h)) * tan;

                Vector3f origin = calculateOrigin(x1, y1, z1, w, h, viewPlane);
                Vector3f direction = calculateDirection(px, py, viewPlane);

                ColorVec color = new Ray(origin, direction).trace(objects, 0);
                int[] data = new int[3];
                data[0] = color.r;
                data[1] = color.g;
                data[2] = color.b;
                imageWriter.write(data);
            }
        }
        imageWriter.close();
    }

}
