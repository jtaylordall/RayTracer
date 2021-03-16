package ray;

public interface Intersection {
    enum Interaction {
        OBJECT,
        LIGHT,
        SPACE
    }

    Interaction intersects();
}
