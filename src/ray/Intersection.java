package ray;

public interface Intersection {
    enum Interaction {
        OBJECT,
        LIGHT,
        SPACE,
        BACK_PLANE
    }

    Interaction intersects();
}
