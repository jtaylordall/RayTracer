package tracer;

import org.joml.Vector2i;
import org.joml.Vector3f;
import ray.Pixel;
import ray.Ray;
import shape.Intersectable;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Intersectable> worldObjects;
    private final List<Pixel> viewPort;

    public Scene() {
        this.worldObjects = new ArrayList<>();
        this.viewPort = new ArrayList<>();
    }

    public void initViewPort(int w, int h) {
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

    public void rayTrace() {
        for (Pixel pixel : viewPort) {
            traceRay(pixel.primaryRay);
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

    private void traceRay(Ray ray) {
        for (Intersectable o : worldObjects)
            if (o.intersection(ray).intersects()) {
                ray.pixel.color = ray.intersectionPoint.object.getColor();
                System.out.println(ray.intersectionPoint.object.getColor());
            }
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
