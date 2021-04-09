package export;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FormatWriter implements ImageWriter {

    private final String fileName;
    private final int w;
    private final int h;
    private int i;
    private int[] imageData;
    private final Format format;

    public enum Format {PNG, JPEG, GIF, BPM, WBPM}

    public FormatWriter(String fileName, int w, int h, Format format) {
        this.w = w;
        this.h = h;
        this.fileName = fileName;
        this.format = format;
    }

    @Override
    public void write(int[] data) {
        imageData[i] = data[0];
        imageData[i + 1] = data[1];
        imageData[i + 2] = data[2];
        i += 3;
    }

    @Override
    public void open() {
        i = 0;
        imageData = new int[w * h * 3];
    }

    @Override
    public void close() {
        BufferedImage image = ppm(w, h, 255, imageData);
        saveImage(fileName, image, format);
    }

    private void saveImage(String fileName, BufferedImage image, Format format) {
        String formatName;
        switch (format) {
            case BPM -> formatName = "bpm";
            case WBPM -> formatName = "wbpm";
            case PNG -> formatName = "png";
            case GIF -> formatName = "gif";
            default -> formatName = "jpeg";
        }
        try {
            File outputFile = new File(fileName + "." + formatName);
            ImageIO.write(image, formatName, outputFile);
        } catch (IOException e) {
            System.err.println("Error writing image");
            System.exit(-1);
        }
    }

    static public BufferedImage ppm(int width, int height, int maxColVal, int[] data) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        if (maxColVal < 256) {
            int r, g, b, k = 0, pixel;
            if (maxColVal == 255) {     // don't scale
                for (int y = 0; y < height; y++) {
                    for (int x = 0; (x < width) && ((k + 3) < data.length); x++) {
                        r = data[k++] & 0xFF;
                        g = data[k++] & 0xFF;
                        b = data[k++] & 0xFF;
                        pixel = 0xFF000000 + (r << 16) + (g << 8) + b;
                        image.setRGB(x, y, pixel);
                    }
                }
            } else {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; (x < width) && ((k + 3) < data.length); x++) {
                        r = data[k++] & 0xFF;
                        r = ((r * 255) + (maxColVal >> 1)) / maxColVal;  // scale to 0..255 range
                        g = data[k++] & 0xFF;
                        g = ((g * 255) + (maxColVal >> 1)) / maxColVal;
                        b = data[k++] & 0xFF;
                        b = ((b * 255) + (maxColVal >> 1)) / maxColVal;
                        pixel = 0xFF000000 + (r << 16) + (g << 8) + b;
                        image.setRGB(x, y, pixel);
                    }
                }
            }
        } else {


            int r, g, b, k = 0, pixel;
            for (int y = 0; y < height; y++) {
                for (int x = 0; (x < width) && ((k + 6) < data.length); x++) {
                    r = (data[k++] & 0xFF) | ((data[k++] & 0xFF) << 8);
                    r = ((r * 255) + (maxColVal >> 1)) / maxColVal;  // scale to 0..255 range
                    g = (data[k++] & 0xFF) | ((data[k++] & 0xFF) << 8);
                    g = ((g * 255) + (maxColVal >> 1)) / maxColVal;
                    b = (data[k++] & 0xFF) | ((data[k++] & 0xFF) << 8);
                    b = ((b * 255) + (maxColVal >> 1)) / maxColVal;
                    pixel = 0xFF000000 + (r << 16) + (g << 8) + b;
                    image.setRGB(x, y, pixel);
                }
            }
        }
        return image;
    }
}
