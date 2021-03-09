package shape;

import org.joml.Random;
import org.joml.Vector3i;

public class ColorVec extends Vector3i {

    public ColorVec() {
        background();
    }

    public ColorVec(int r, int g, int b) {
        this.x = r;
        this.y = g;
        this.z = b;
    }


    public void background() {
        this.x = 100;
        this.y = 100;
        this.z = 100;
    }

    public void random() {
        Random random = new Random();
        this.x = random.nextInt(255);
        this.y = random.nextInt(255);
        this.z = random.nextInt(255);
    }

    public void black() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public void white() {
        this.x = 255;
        this.y = 255;
        this.z = 255;
    }

    public void red() {
        this.x = 255;
        this.y = 0;
        this.z = 0;
    }

    public void green() {
        this.x = 0;
        this.y = 255;
        this.z = 0;
    }

    public void blue() {
        this.x = 0;
        this.y = 0;
        this.z = 255;
    }

    public void purple() {
        this.x = 196;
        this.y = 0;
        this.z = 255;
    }

    public void yellow() {
        this.x = 255;
        this.y = 255;
        this.z = 0;
    }

    public void orange() {
        this.x = 255;
        this.y = 128;
        this.z = 0;
    }
}
