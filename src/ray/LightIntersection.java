package ray;

public class LightIntersection implements Intersection {
    @Override
    public Interaction intersects() {
        return Interaction.LIGHT;
    }
}
