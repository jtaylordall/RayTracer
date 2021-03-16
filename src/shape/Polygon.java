package shape;

import org.joml.Vector2f;
import org.joml.Vector3f;
import ray.*;

public class Polygon extends Intersectable {
    public Vector3f[] vertices = new Vector3f[3];
    public Vector3f normal;

    public Polygon(Vector3f v0, Vector3f v1, Vector3f v2) {
        this.vertices[0] = v0;
        this.vertices[1] = v1;
        this.vertices[2] = v2;

        this.normal = calculateNormal(vertices);
    }

    private Vector3f calculateNormal(Vector3f[] vertices) {
        Vector3f i = new Vector3f(vertices[2]);
        i.sub(vertices[0]);

        Vector3f j = new Vector3f(vertices[1]);
        j.sub(vertices[0]);

        i.cross(j);
        i.normalize();
        return i;
    }

    private Edge2D[] determine2DEdges(Vector2f[] vertices) {
        Edge2D[] edges = new Edge2D[3];
        edges[0] = new Edge2D(vertices[1], vertices[2]);
        edges[1] = new Edge2D(vertices[2], vertices[3]);
        edges[2] = new Edge2D(vertices[3], vertices[1]);
        return edges;
    }

    @Override
    public Intersection intersection(Ray ray) {
        normal.normalize();
        Plane plane = new Plane(normal, calculateDistance());
        Intersection intersection = plane.intersection(ray);

        if (intersection.intersects() == Intersection.Interaction.SPACE) return new NullIntersection();
        IntersectionPoint intersectionPoint = (IntersectionPoint) intersection;

        Vector3f point = intersectionPoint.point;
//        if (ray.pixel.screenCoordinate.x == 150 && ray.pixel.screenCoordinate.y == 150) {
//            System.out.println();
//        }

        Vector2f[] pVertices = getPlaneProjection(point);
        int numCrossings = numCrossings(pVertices);
        if (numCrossings <= 0 || numCrossings % 2 == 0) return new NullIntersection();

        TransmissionRay transRay = new TransmissionRay(point, ray.direction, normal);

        return new IntersectionPoint(ray, transRay, point, normal, intersectionPoint.t, this);
    }

    private int numCrossings(Vector2f[] p) {
        Vector2f origin = p[0];
        int num = 0;
        Edge2D[] edges = determine2DEdges(p);
        for (int i = 0; i < 3; i++) {
            if (crosses(edges[i], origin)) num++;
        }
        return num;
    }

    private boolean crosses(Edge2D edge2D, Vector2f origin) {
        Vector2f a = new Vector2f(edge2D.a);
        Vector2f b = new Vector2f(edge2D.b);
        a.sub(origin);
        b.sub(origin);

        if (a.y <= 0.f && b.y >= 0.f || a.y >= 0.f && b.y <= 0.f) {
            float xIntercept = getXIntercept(getSlope(a, b), a.x, a.y);
            return xIntercept < 0.f;
        } else return false;
    }

    private float getSlope(Vector2f a, Vector2f b) {
        return (b.y - a.y) / (b.x - a.x);
    }


    private float getXIntercept(float m, float x1, float y1) {
        return x1 - (y1 / m);
    }

    private Vector2f[] getPlaneProjection(Vector3f point) {
        float i, j, k;
        i = Math.abs(point.x);
        j = Math.abs(point.y);
        k = Math.abs(point.z);

        Vector2f[] pVertices = new Vector2f[4];

//        pVertices[0] = new Vector2f(point.x, point.y);
//        pVertices[1] = new Vector2f(vertices[0].x, vertices[0].y);
//        pVertices[2] = new Vector2f(vertices[1].x, vertices[1].y);
//        pVertices[3] = new Vector2f(vertices[2].x, vertices[2].y);

        if (i >= j && i >= k) {
            pVertices[0] = new Vector2f(point.y, point.z);
            pVertices[1] = new Vector2f(vertices[0].y, vertices[0].z);
            pVertices[2] = new Vector2f(vertices[1].y, vertices[1].z);
            pVertices[3] = new Vector2f(vertices[2].y, vertices[2].z);
        } else if (j >= i && j >= k) {
            pVertices[0] = new Vector2f(point.x, point.z);
            pVertices[1] = new Vector2f(vertices[0].x, vertices[0].z);
            pVertices[2] = new Vector2f(vertices[1].x, vertices[1].z);
            pVertices[3] = new Vector2f(vertices[2].x, vertices[2].z);
        } else {
            pVertices[0] = new Vector2f(point.x, point.y);
            pVertices[1] = new Vector2f(vertices[0].x, vertices[0].y);
            pVertices[2] = new Vector2f(vertices[1].x, vertices[1].y);
            pVertices[3] = new Vector2f(vertices[2].x, vertices[2].y);
        }
        return pVertices;
    }

    private float calculateDistance() {
        Vector3f mid = new Vector3f(vertices[0]);
        mid.add(vertices[1]);
        mid.add(vertices[2]);
        mid.div(3);
        return mid.length();
    }

    private static class Edge2D {
        protected Vector2f a;
        protected Vector2f b;

        public Edge2D(Vector2f a, Vector2f b) {
            this.a = a;
            this.b = b;
        }
    }
}
