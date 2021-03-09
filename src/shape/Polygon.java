package shape;

import org.joml.Vector3f;
import ray.Intersection;
import ray.Ray;

import java.util.List;

public class Polygon implements Intersectable {
    Vector3f[] vertices = new Vector3f[3];
    Edge[] edges = new Edge[3];
    Vector3f normal;
    ColorVec color;

    public Polygon(List<Vector3f> vertices, ColorVec color) {
        this.vertices[0] = vertices.get(0);
        this.vertices[1] = vertices.get(1);
        this.vertices[2] = vertices.get(2);
        determineEdges();

        this.normal = calculateNormal(vertices);
        this.color = color;
    }

    private Vector3f calculateNormal(List<Vector3f> vertices) {
        Vector3f i = new Vector3f(vertices.get(0));
        i.cross(vertices.get(1));

        Vector3f j = new Vector3f(vertices.get(0));
        j.cross(vertices.get(2));

        i.cross(j);
        return i;
    }

    private void determineEdges() {
        edges[0] = new Edge(vertices[0], vertices[1]);
        edges[1] = new Edge(vertices[1], vertices[2]);
        edges[2] = new Edge(vertices[2], vertices[0]);
    }

    @Override
    public Intersection intersection(Ray ray) {
        Plane plane = new Plane(
                normal, 1.f, color
        );
        return null;
    }

    @Override
    public ColorVec getColor() {
        return null;
    }

    private class Edge {
        protected Vector3f a;
        protected Vector3f b;

        public Edge(Vector3f a, Vector3f b) {
            this.a = a;
            this.b = b;
        }
    }
}
