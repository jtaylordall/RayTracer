package scene;

import org.joml.Vector3f;
import shape.ColorVec;
import shape.Plane;
import shape.Sphere;
import shape.Triangle;

import java.util.List;

public class SceneImport {
    String name;
    int maxRayDepth;

    int fov;
    Vector3f lookAt;
    Vector3f lookFrom;
    Vector3f lookUp;

    int lightDistance;
    Vector3f lightDirection;
    ColorVec lightColor;
    ColorVec ambientColor;
    ColorVec backgroundColor;

    List<Sphere> spheres;
    List<Triangle> triangles;
    List<Plane> planes;
}
