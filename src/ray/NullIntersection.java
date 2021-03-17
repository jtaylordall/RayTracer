package ray;

public class NullIntersection implements Intersection {
    @Override
    public Interaction intersects() {
        return Interaction.SPACE;
    }

    @Override
    public String toString() {
        return "NullIntersection{}";
    }
}
