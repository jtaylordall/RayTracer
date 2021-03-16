package tracer;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import ray.Pixel;
import shape.ColorVec;
import shape.Intersectable;
import shape.LightDirection;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    public static LightDirection lightDirection;
    private final List<Intersectable> worldObjects;
    private final List<Pixel> viewPort;

    public static Vector3f cameraLookFrom;
    private Vector3f cameraLookAt;
    private Vector3f cameraLookUp;
    private float fov;

    public static ColorVec ambientColor;
    public static ColorVec backgroundColor;

    public Scene() {
        this.worldObjects = new ArrayList<>();
        this.viewPort = new ArrayList<>();
    }

    public void setLightDirection(LightDirection lightDirection) {
        this.lightDirection = lightDirection;
    }

    public void setCameraLookFrom(Vector3f cameraLookFrom) {
        this.cameraLookFrom = cameraLookFrom;
    }

    public void setCameraLookAt(Vector3f cameraLookAt) {
        this.cameraLookAt = cameraLookAt;
    }

    public void setCameraLookUp(Vector3f cameraLookUp) {
        this.cameraLookUp = cameraLookUp;
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

    public void initViewPort1(int w, int h) {
        Vector3f lookFrom;

        float z = 0.f;
        lookFrom = new Vector3f(0.f, 0.f, z);

        int xMin, yMax;
        xMin = -w / 2;
        yMax = h / 2;
        float zDirection = (float) -(w + h) / 2;

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {

                int x1 = x + xMin;
                int y1 = yMax - y;

                float xRatio = (float) (x1) / (-xMin);
                float yRatio = (float) (y1) / (yMax);

                float xDirection = (-xMin) * xRatio;
                float yDirection = (yMax) * yRatio;


                Vector3f d = new Vector3f(xDirection, yDirection, zDirection);
                d.normalize();
                viewPort.add(new Pixel(new Vector2i(x, y),
                        new Vector3f(lookFrom), d));
            }
    }

    public void initViewPort(int w, int h) {
//        cameraLookAt.normalize();
//        cameraLookFrom.normalize();
        cameraLookUp.normalize();

        Vector3f vpn = new Vector3f(cameraLookFrom);
        vpn.sub(cameraLookAt);
        vpn.normalize();

        Vector3f vup = cameraLookUp;
        vup.normalize();

        Vector3f n = new Vector3f(vpn);

        Vector3f u = new Vector3f(vup);
        u.cross(n);

        Vector3f v = new Vector3f(n);
        v.cross(u);

        u.normalize();
        v.normalize();
        n.normalize();


        Matrix4f m = new Matrix4f(
                1, 0, 0, -cameraLookAt.x,
                0, 1, 0, -cameraLookAt.y,
                0, 0, 1, -cameraLookAt.z,
                0, 0, 0, 1
        );
        Matrix4f t = new Matrix4f(
                u.x, u.y, u.z, 0,
                v.x, v.y, v.z, 0,
                n.x, n.y, n.z, 0,
                0, 0, 0, 1
        );


        int wHalf = w / 2;
        int hHalf = h / 2;

        float tan = (float) Math.tan(fov / 2 * Math.PI / 180);
        for (int y = h - 1; y >= 0; y--) {
            for (int x = 0; x < w; x++) {

                float x1 = x - wHalf + .5f;
                float y1 = y - hHalf + .5f;
                int z1 = (int) cameraLookFrom.z;

                Vector3f origin = new Vector3f(x1, y1, z1);

                float aspectRatio = ((float) w) / ((float) h);
                float px = ((2 * x1 / w)) * tan * aspectRatio;
                float py = ((2 * y1 / h)) * tan;

//                System.out.printf("%f , %f :\t\t %f , %f\n", x1, y1, px, py);

                Vector3f direction = new Vector3f(px, py, -2f);
//                Vector3f direction = new Vector3f(vpn);
//                direction.mul(-1);
                direction.normalize();
//                System.out.printf("%f , %f :\t\t %s\n", x1, y1, direction);

                Vector4f o1 = new Vector4f(origin, 1);
                o1.x /= w;
                o1.y /= h;
                o1.mul(t);
                o1.mul(m);
                origin = new Vector3f(o1.x, o1.y, o1.z);

                Vector4f d1 = new Vector4f(direction, 1);
                d1.mul(t);
                d1.mul(m);
                direction = new Vector3f(d1.x, d1.y, d1.z);
                direction.normalize();

                Pixel p = new Pixel(new Vector2i(x, y), origin, direction);
//                System.out.println(p);
//                System.out.println();
                viewPort.add(p);
//                System.out.printf("%s :\t\t %s\n", origin, direction);
            }
        }
    }

    public void rayTrace() {
        for (Pixel pixel : viewPort) {
            if (pixel.screenCoordinate.x == 30 && pixel.screenCoordinate.y == 25)
                System.out.println();
            pixel.color = pixel.primaryRay.trace(worldObjects);
        }
    }

    void printPixel(Pixel pixel) {
        System.out.printf("vp(%d,%d)\t\tdir(%f,%f,%f)\t\tc(%d,%d,%d)\n",
                pixel.screenCoordinate.x,
                pixel.screenCoordinate.y,
                pixel.primaryRay.direction.x,
                pixel.primaryRay.direction.y,
                pixel.primaryRay.direction.z,
                pixel.color.x,
                pixel.color.y,
                pixel.color.z
        );
    }

    public void addObject(Intersectable o) {
        worldObjects.add(o);
    }

    public int[] getPixelColors() {
        int[] colors = new int[3 * viewPort.size()];

        int i = 0;
        for (Pixel pixel : viewPort) {
            colors[i] = pixel.color.x;
            colors[i + 1] = pixel.color.y;
            colors[i + 2] = pixel.color.z;
            i += 3;
            if (i >= colors.length) break;
        }

        return colors;
    }

}
