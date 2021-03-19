package export;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PpmWriter implements ImageWriter {

    private String fileName;
    private FileWriter writer;
    private final int w;
    private final int h;

    public PpmWriter(String fileName, int w, int h) {
        this.fileName = fileName;
        this.w = w;
        this.h = h;
    }

    public void open() {
        try {
            fileName = createFile(
                    fileName + "_" + w + "x" + h);
            writer = new FileWriter(fileName);
            writeHeaders(w, h);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(int[] data) {
        try {
            for (int i = 0; i < data.length; i++) {
                int n = data[i];
                if (n < 0) n = 0;
                else if (n > 255) n -= 255;

                writer.append(String.valueOf(n));
                if (i < data.length - 1) writer.append(' ');
            }
            writer.append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String createFile(String fileName) {
        fileName = checkFileType(fileName);
        return fileName;
    }

    String checkFileType(String fileName) {
        String fileType = ".ppm";
        if (!fileName.endsWith(fileType)) return fileName + fileType;
        else return fileName;
    }

    public String generateRandomString() {
        char[] array = new char[7]; // length is bounded by 7
        for (int i = 0; i < array.length; i++) {
            array[i] = (char) ('a' + new Random().nextInt(26));
        }
        return new String(array);
    }

    private void writeHeaders(int w, int h) {
        try {
            String s = "P3" + '\n' +
                    w + " # image width" + '\n' +
                    h + " # image height" + '\n' +
                    "255 # max value" + '\n';
            writer.write(s);
        } catch (IOException e) {
            System.err.println("An error occurred while writing headers.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void writeEntireFile(String fileName, int[] data, int w, int h) {
        StringBuilder sb = new StringBuilder();
        int colCount = 0;

        try {
            FileWriter writer = new FileWriter(fileName);
            sb.append("P3").append('\n');
            sb.append(w).append(" # image width").append('\n');
            sb.append(h).append(" # image height").append('\n');
            sb.append("255 # max value").append('\n');

            writer.write(sb.toString());
            for (int n : data) {
                if (n < 0) n = 0;
                else if (n > 255) n -= 255;

                writer.append(String.valueOf(n));
                if (colCount >= 3) {
                    colCount = 0;
                    writer.append('\n');
                } else {
                    colCount++;
                    writer.append(' ');
                }

            }
            writer.close();
            System.out.println("Successfully wrote to the file " + fileName + ".");

        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
