package export;

import shape.ColorVec;

public class PpmCoordinateTest {

    public static void main(String[] args) {
        int w = 720;
        int h = 512;
        int size = w * h;
        int[] data = new int[3 * size];

        ColorVec c1 = new ColorVec();
        ColorVec c2 = new ColorVec();
        ColorVec c3 = new ColorVec();
        ColorVec c4 = new ColorVec();

        c1.orange();
        c2.purple();
        c3.yellow();
        c4.blue();

        int x = w / 2;
        int y = h / 2;

        int a = 0;
        int range = 10;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {

                ColorVec color = new ColorVec();

                if (i > x - range && i < x + range && j > y - range && j < y + range) {
                    color.red();
                } else if (i < range * 2 && j < range * 2) {
                    color.black();
                } else if (i > w - range * 2 && j > h - range * 2) {
                    color.white();
                }
                data[a] = color.x;
                data[a + 1] = color.y;
                data[a + 2] = color.z;
                a += 3;
            }
        }

        PpmExporter exporter = new PpmExporter();
        exporter.export("axis", data, w, h);

    }
}