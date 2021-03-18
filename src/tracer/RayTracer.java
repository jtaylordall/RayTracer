package tracer;

import export.PpmExporter;
import org.joml.Vector3f;
import shape.*;

public class RayTracer {
    public static void main(String[] args) {
        int w, h;
        w = 2000;
        h = 2000;

        new RayTracer().rayTraceScene(w, h);
    }

    public void rayTraceScene(int w, int h) {
        Scene scene;
//        scene = getExampleScene1();
//        scene = getExampleScene2();
        scene = getCustomScene();
        scene.initViewPort(w, h);
        scene.rayTrace();

        new PpmExporter().export("rtx_" + scene.name + "_", scene.getPixelColors(), w, h);
    }

    Scene getExampleScene1() {
        Scene scene = new Scene("diffuse");
        scene.setCameraLookAt(new Vector3f(0, 0, 0));
        scene.setCameraLookFrom(new Vector3f(0, 0, 1));
        scene.setCameraLookUp(new Vector3f(0, 1, 0));
        scene.setFov(28);

        LightDirection lightDirection = new LightDirection(new Vector3f(-1.f, .0f, 0f), 50);
        lightDirection.od = new ColorVec();
        lightDirection.od.white();
        scene.setLightDirection(lightDirection);

        ColorVec ambient = ColorVec.toColorVec(new Vector3f(.1f, .1f, .1f));
        scene.setAmbientColor(ambient);

        ColorVec background = ColorVec.toColorVec(new Vector3f(.2f, .2f, .2f));
        scene.setBackgroundColor(background);

        //Sphere Center .35 0 -.1 Radius .05
        //Kd 0.8 Ks 0.1 Ka 0.1 Od 1 1 1 Os 1 1 1 Kgls 4
        Sphere sphere1 = new Sphere(.05f, new Vector3f(0.35f, 0.f, -.1f));
        sphere1.setColorProperties(
                ColorVec.toColorVec(new Vector3f(1.f, 1.f, 1.f)),
                ColorVec.toColorVec(new Vector3f(1.f, 1.f, 1.f)),
                .8f, .1f, .1f, 4);
        scene.addObject(sphere1);

        //Sphere Center .2 0 -.1 Radius .075
        //Kd 0.1 Ks 0.8 Ka 0.1 Od 1 0 0 Os .5 1 .5 Kgls 32
        Sphere sphere2 = new Sphere(.075f, new Vector3f(.2f, 0.f, -.1f));
        sphere2.setColorProperties(
                ColorVec.toColorVec(new Vector3f(1.f, 0.f, 0.f)),
                ColorVec.toColorVec(new Vector3f(.5f, 1.f, .5f)),
                .1f, .8f, .1f, 32);
        scene.addObject(sphere2);

        //Sphere Center -.6 0 0 Radius .3
        //Kd 0.4 Ks 0.5 Ka 0.1 Od 0 1 0 Os .5 1 .5 Kgls 32
        Sphere sphere3 = new Sphere(.3f, new Vector3f(-.6f, 0.f, 0.0f));
        sphere3.setColorProperties(
                ColorVec.toColorVec(new Vector3f(0.f, 1.f, 0.f)),
                ColorVec.toColorVec(new Vector3f(.5f, 1.f, .5f)),
                .4f, .5f, .1f, 32);
        scene.addObject(sphere3);

        //Triangle .3 -.3 -.4 | 0 .3 -.1 | -.3 -.3 .2
        //Kd 0.7 Ks 0.2 Ka 0.1 Od 0 0 1 Os 1 1 1 Kgls 32
        Triangle polygon1 = new Triangle(
                new Vector3f(.3f, -.3f, -.4f),
                new Vector3f(0.f, .3f, -.1f),
                new Vector3f(-.3f, -.3f, .2f)
        );
        polygon1.setColorProperties(
                ColorVec.toColorVec(new Vector3f(0f, 0f, 1f)),
                ColorVec.toColorVec(new Vector3f(1f, 1f, 1f)),
                .7f, .2f, .1f, 32);
        scene.addObject(polygon1);

        //Triangle  -.2 .1 .1   -.2 -.5 .2   -.2 .1 -.3
        //Kd 0.9 Ks 0.0 Ka 0.1 Od 1 1 0 Os 1 1 1 Kgls 4
        Triangle triangle2 = new Triangle(
                new Vector3f(-.2f, .1f, .1f),
                new Vector3f(-.2f, -.5f, .2f),
                new Vector3f(-.2f, .1f, -.3f)
        );
        triangle2.setColorProperties(
                ColorVec.toColorVec(new Vector3f(1f, 1f, 0f)),
                ColorVec.toColorVec(new Vector3f(1f, 1f, 1f)),
                .9f, .0f, .1f, 4);
        scene.addObject(triangle2);

        return scene;
    }

    Scene getExampleScene2() {
        Scene scene = new Scene("reflection");
        scene.setCameraLookAt(new Vector3f(0, 0, 0));
        scene.setCameraLookFrom(new Vector3f(0, 0, 1.2f));
        scene.setCameraLookUp(new Vector3f(0, 1, 0));
        scene.setFov(55);

        LightDirection lightDirection = new LightDirection(new Vector3f(0f, -1f, 0f), 50);
        lightDirection.od = new ColorVec();
        lightDirection.od.white();
        scene.setLightDirection(lightDirection);

        ColorVec ambient = ColorVec.toColorVec(new Vector3f(0f, 0f, 0f));
        scene.setAmbientColor(ambient);

        ColorVec background = ColorVec.toColorVec(new Vector3f(.2f, .2f, .2f));
        scene.setBackgroundColor(background);

        //Sphere Center 0 .3 0 Radius .2
        // Kd 0.0 Ks 0.9 Ka 0.1 Od .75 .75 .75 Os 1.0 1.0 1.0 Kgls 10
        Sphere sphere1 = new Sphere(.2f, new Vector3f(0f, .3f, 0f));
        sphere1.setColorProperties(
                ColorVec.toColorVec(new Vector3f(.75f, .75f, .75f)),
                ColorVec.toColorVec(new Vector3f(1.f, 1.f, 1.f)),
                .0f, .9f, .1f, 10);
        scene.addObject(sphere1);

        //Triangle  0 -.5 .5     1 .5 0     0 -.5 -.5
        //Kd 0.9 Ks 0.0 Ka 0.1 Od 0 0 1 Os 1.0 1.0 1.0 Kgls 4
        Triangle triangle1 = new Triangle(
                new Vector3f(0f, -.5f, .5f),
                new Vector3f(1f, .5f, 0f),
                new Vector3f(0f, -.5f, -.5f)
        );
        triangle1.setColorProperties(
                ColorVec.toColorVec(new Vector3f(0f, 0f, 1f)),
                ColorVec.toColorVec(new Vector3f(1f, 1f, 1f)),
                .9f, .0f, .1f, 4);
        scene.addObject(triangle1);

        //Triangle  0 -.5 .5     0 -.5 -.5     -1 .5 0
        // Kd 0.9 Ks 0.0 Ka 0.1 Od 1 1 0 Os 1.0 1.0 1.0 Kgls 4
        Triangle triangle2 = new Triangle(
                new Vector3f(0f, -.5f, .5f),
                new Vector3f(0f, -.5f, -.5f),
                new Vector3f(-1f, .5f, 0f)
        );
        triangle2.setColorProperties(
                ColorVec.toColorVec(new Vector3f(1f, 1f, 0f)),
                ColorVec.toColorVec(new Vector3f(1f, 1f, 1f)),
                .9f, .0f, .1f, 4);
        scene.addObject(triangle2);

        return scene;
    }

    Scene getCustomScene() {

        Scene scene = new Scene("custom");

        scene.setCameraLookAt(new Vector3f(0, 0, 0));
        scene.setCameraLookFrom(new Vector3f(.2f, .5f, 1));
        scene.setCameraLookUp(new Vector3f(0, 1, 0));
        scene.setFov(40);

        LightDirection lightDirection = new LightDirection(new Vector3f(.0f, -1.f, 0f), 50);
        lightDirection.od = new ColorVec(255, 255, 255);
        scene.setLightDirection(lightDirection);
        scene.setAmbientColor(new ColorVec(20, 20, 20));
        scene.setBackgroundColor(new ColorVec(50, 50, 50));

        Sphere sphere1 = new Sphere(.1f, new Vector3f(.6f, -.4f, -.3f));
        sphere1.setColorProperties(
                new ColorVec(50, 255, 40),
                new ColorVec(255, 255, 255),
                .9f, .1f, .1f, 60);
        scene.addObject(sphere1);

        Sphere sphere2 = new Sphere(.3f, new Vector3f(0f, -.5f, -1f));
        sphere2.setColorProperties(
                new ColorVec(150, 50, 50),
                new ColorVec(255, 255, 255),
                .6f, .8f, .2f, 40);
        scene.addObject(sphere2);

        Triangle triangle1 = new Triangle(
                new Vector3f(-.7f, -.2f, 0),
                new Vector3f(-.5f, .2f, -.2f),
                new Vector3f(-.3f, -.2f, 0)
        );
        triangle1.setColorProperties(
                new ColorVec(0, 70, 240),
                new ColorVec(255, 255, 255),
                1f, 0f, 1f, 4);
        scene.addObject(triangle1);

        Plane plane = new Plane(new Vector3f(0f, 0f, 1f), 4f);
        plane.setColorProperties(
                new ColorVec(255, 255, 255),
                new ColorVec(1, 1, 1),
                1f, 2f, 0f, 32);
        scene.addObject(plane);

        Plane plane2 = new Plane(new Vector3f(0f, 1f, 0f), 2f);
        plane2.setColorProperties(
                new ColorVec(150, 150, 150),
                new ColorVec(1, 1, 1),
                1f, 0f, .5f, 32);
        scene.addObject(plane2);


        return scene;
    }


}