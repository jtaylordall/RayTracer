package tracer;

import export.PpmExporter;
import org.joml.Vector3f;
import shape.ColorVec;
import shape.LightDirection;
import shape.Sphere;

public class RayTracer {
    public static void main(String[] args) {
        int w, h;
        w = 1000;
        h = 1000;

        new RayTracer().rayTraceScene(w, h);
    }

    public void rayTraceScene(int w, int h) {
        Scene scene = getScene1();
        scene.initViewPort(w, h);
        scene.rayTrace();

        new PpmExporter().export("rtx_test", scene.getPixelColors(), w, h);
    }

    Scene getScene1() {
        Scene scene = new Scene();
        scene.setCameraLookAt(new Vector3f(0, 0, 0));
        scene.setCameraLookFrom(new Vector3f(0, 0, 1));
        scene.setCameraLookUp(new Vector3f(0, 1, 0));
        scene.setFov(28);

        LightDirection lightDirection = new LightDirection(new Vector3f(-1.f, .0f, 0.f), 50);
        lightDirection.od = new ColorVec();
        lightDirection.od.white();
        scene.setLightDirection(lightDirection);

        ColorVec ambient = ColorVec.toColorVec(new Vector3f(.1f, .1f, .1f));
        scene.setAmbientColor(ambient);

        ColorVec background = ColorVec.toColorVec(new Vector3f(.2f, .2f, .2f));
        scene.setBackgroundColor(background);

        //Sphere Center .35 0 -.1 Radius .05 Kd 0.8 Ks 0.1 Ka 0.1 Od 1 1 1 Os 1 1 1 Kgls 4

        Sphere sphere1 = new Sphere(.05f, new Vector3f(0.35f, 0.f, -.1f));
        sphere1.setColorProperties(
                ColorVec.toColorVec(new Vector3f(1.f, 1.f, 1.f)),
                ColorVec.toColorVec(new Vector3f(1.f, 1.f, 1.f)),
                .8f, .1f, .1f, 4);
        scene.addObject(sphere1);

        //Sphere Center .2 0 -.1 Radius .075 Kd 0.1 Ks 0.8 Ka 0.1 Od 1 0 0 Os .5 1 .5 Kgls 32
        Sphere sphere2 = new Sphere(.075f, new Vector3f(.2f, 0.f, -.1f));
        sphere2.setColorProperties(
                ColorVec.toColorVec(new Vector3f(1.f, 0.f, 0.f)),
                ColorVec.toColorVec(new Vector3f(.5f, 1.f, .5f)),
                .1f, .8f, .1f, 32);
        scene.addObject(sphere2);

        //Sphere Center -.6 0 0 Radius .3 Kd 0.4 Ks 0.5 Ka 0.1 Od 0 1 0 Os .5 1 .5 Kgls 32
        Sphere sphere3 = new Sphere(.3f, new Vector3f(-.6f, 0.f, 0.f));
        sphere3.setColorProperties(
                ColorVec.toColorVec(new Vector3f(0.f, 1.f, 0.f)),
                ColorVec.toColorVec(new Vector3f(.5f, 1.f, .5f)),
                .4f, .5f, .1f, 32);
        scene.addObject(sphere3);
        return scene;
    }


}