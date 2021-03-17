package ray;

public class LightIntersection implements Intersection {
    @Override
    public Interaction intersects() {
        return Interaction.LIGHT;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LightIntersection{");
        sb.append('}');
        return sb.toString();
    }
}
