package ray;

public class NullIntersection implements Intersection {
    @Override
    public boolean intersects() {
        return false;
    }
}
