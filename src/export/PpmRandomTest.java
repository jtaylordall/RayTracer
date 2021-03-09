package export;

import java.util.Random;

public class PpmRandomTest {

    public static void main(String[] args) {
        int w = 720;
        int h = 512;
        int size = 3 * w * h;
        int[] data = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            data[i] = random.nextInt(255);
        }

        PpmExporter exporter = new PpmExporter();
        exporter.export("random", data, w, h);
    }
}
