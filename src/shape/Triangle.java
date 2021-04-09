package shape;

import instersection.Intersection;
import instersection.IntersectionPoint;
import instersection.NullIntersection;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ray.Ray;
import ray.TransmissionRay;

public class Triangle extends Intersectable {

    public Vector3f[] vertices = new Vector3f[3];
    public Vector3f normal;

    public Triangle(Vector3f v0, Vector3f v1, Vector3f v2) {
        this.vertices[0] = v0;
        this.vertices[1] = v1;
        this.vertices[2] = v2;

        this.normal = calculateNormal(vertices);
        normal.normalize();
    }

    private Vector3f calculateNormal(Vector3f[] vertices) {
        Vector3f i = new Vector3f(vertices[0]);
        i.sub(vertices[1]);

        Vector3f j = new Vector3f(vertices[0]);
        j.sub(vertices[2]);

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
    public Intersection calculateIntersection(Ray ray) {
        if (normal == null)
            normal = calculateNormal(this.vertices);
        normal.normalize();

        Vector3f n = new Vector3f(normal);
        ray.direction.normalize();
        float vd = ray.direction.dot(n);
        if (vd == 0) return new NullIntersection();

        Vector3f vo = new Vector3f(vertices[0]);
        vo.sub(ray.origin);

        float t = vo.dot(n) / vd;

        Vector3f point = ray.getPointAtT(t);

        if (t < 0) return new NullIntersection();
        if (vd > 0) n.mul(-1);

        fudgePoint(point, n);

        Vector2f[] pVertices = getPlaneProjection(point);
        int numCrossings = numCrossings(pVertices);
        if (numCrossings <= 0 || numCrossings % 2 == 0) return new NullIntersection();
        TransmissionRay transRay = new TransmissionRay(point, ray.direction, normal);

        return new IntersectionPoint(ray, transRay, point, normal, t, this);
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
        i = Math.abs(normal.x);
        j = Math.abs(normal.y);
        k = Math.abs(normal.z);

        float max = Math.max(Math.max(i, j), k);

        Vector2f[] pVertices = new Vector2f[4];

        if (k == max) {
            pVertices[0] = new Vector2f(point.x, point.y);
            pVertices[1] = new Vector2f(vertices[0].x, vertices[0].y);
            pVertices[2] = new Vector2f(vertices[1].x, vertices[1].y);
            pVertices[3] = new Vector2f(vertices[2].x, vertices[2].y);
        } else if (j == max) {
            pVertices[0] = new Vector2f(point.x, point.z);
            pVertices[1] = new Vector2f(vertices[0].x, vertices[0].z);
            pVertices[2] = new Vector2f(vertices[1].x, vertices[1].z);
            pVertices[3] = new Vector2f(vertices[2].x, vertices[2].z);
        } else if (i == max) {
            pVertices[0] = new Vector2f(point.y, point.z);
            pVertices[1] = new Vector2f(vertices[0].y, vertices[0].z);
            pVertices[2] = new Vector2f(vertices[1].y, vertices[1].z);
            pVertices[3] = new Vector2f(vertices[2].y, vertices[2].z);
        } else {
            pVertices[0] = new Vector2f(point.x, point.y);
            pVertices[1] = new Vector2f(vertices[0].x, vertices[0].y);
            pVertices[2] = new Vector2f(vertices[1].x, vertices[1].y);
            pVertices[3] = new Vector2f(vertices[2].x, vertices[2].y);
        }
        return pVertices;
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
