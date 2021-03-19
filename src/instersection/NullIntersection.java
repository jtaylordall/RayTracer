package instersection;

public class NullIntersection implements Intersection {
    @Override
    public IntersectType getIntersectType() {
        return IntersectType.SPACE;
    }
}
