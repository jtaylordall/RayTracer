package scene;

import com.google.gson.Gson;
import shape.Intersectable;
import shape.LightPlane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SceneImporter {

    public static Scene importScene(String jsonFile) {
        String json = readFile(jsonFile);
        Gson gson = new Gson();
        SceneImport s = gson.fromJson(json, SceneImport.class);
        return initScene(s);
    }

    private static Scene initScene(SceneImport s) {
        Scene scene = new Scene();

        scene.setName(s.name);
        scene.setMaxRayDepth(s.maxRayDepth);
        scene.setLookAt(s.lookAt);
        scene.setLookUp(s.lookUp);
        scene.setLookFrom(s.lookFrom);
        scene.setFov(s.fov);
        scene.setLightPlane(new LightPlane(s.lightDirection,
                s.lightDistance, s.lightColor));
        scene.setAmbientColor(s.ambientColor);
        scene.setBackgroundColor(s.backgroundColor);
        List<Intersectable> objects = new ArrayList<>();
        objects.addAll(s.spheres);
        objects.addAll(s.triangles);
        objects.addAll(s.planes);
        scene.addObjects(objects);
        return scene;
    }

    private static String readFile(String jsonFile) {
        File file = new File(jsonFile);

        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.printf("file \"%s\" does not exist\n", jsonFile);
            System.exit(-1);
        }
        return sb.toString();
    }
}

