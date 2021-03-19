package instersection;

public class LightIntersection implements Intersection {
    @Override
    public IntersectType getIntersectType() {
        return IntersectType.LIGHT;
    }
}
