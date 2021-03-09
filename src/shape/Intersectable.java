package shape;

import ray.Intersection;
import ray.Ray;

public interface Intersectable {
    Intersection intersection(Ray ray);

    ColorVec getColor();
}
