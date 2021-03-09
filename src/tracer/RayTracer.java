package tracer;

import export.PpmExporter;
import org.joml.Vector3f;
import shape.Plane;
import shape.Sphere;

public class RayTracer {
    public static void main(String[] args) {
        int w, h;
        w = 300;
        h = 300;
        new RayTracer().rayTraceScene(w, h);
    }

    public void rayTraceScene(int w, int h) {
        Scene scene = new Scene();
        scene.initViewPort(w, h);

//
//        Sphere sphere1 = new Sphere(.5f, new Vector3f(-1.0f, -1.0f, -5.0f));
//        sphere1.color.purple();
//        scene.addObject(sphere1);
        Plane plane = new Plane(new Vector3f(0, 0, -1), 2.f);
        plane.color.blue();
        scene.addObject(plane);

        Sphere sphere = new Sphere(.5f, new Vector3f(3.f, 3.f, .0f));
        sphere.color.white();
        scene.addObject(sphere);

        scene.rayTrace();

        new PpmExporter().export("rtx_test", scene.getPixelColors(), w, h);
    }


}
