package instersection;

public interface Intersection {
    enum IntersectType {
        OBJECT,
        LIGHT,
        SPACE,
        BACK_PLANE
    }

    IntersectType getIntersectType();
}
