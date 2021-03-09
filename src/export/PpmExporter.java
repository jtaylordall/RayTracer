package export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PpmExporter {

    public void export(String fileName, int[] data, int w, int h) {
        fileName = createFile(
                fileName + "_" + w + "_" + h);
//        + "_" + generateRandomString());
        writeToFile(fileName, data, w, h);
    }

    public String createFile(String fileName) {

        fileName = checkFileType(fileName);
        try {
            System.out.println(fileName);
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            return fileName;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    String checkFileType(String fileName) {
        fileName = "./images/" + fileName;
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

    public void writeToFile(String fileName, int[] data, int w, int h) {
        StringBuilder sb = new StringBuilder();
        int colCount = 0;

        try {
            FileWriter myWriter = new FileWriter(fileName);

            sb.append("P3").append('\n');
            sb.append(w).append(" # image width").append('\n');
            sb.append(h).append(" # image height").append('\n');
            sb.append("255 # max value").append('\n');

            myWriter.write(sb.toString());
            for (int n : data) {
                if (n < 0) n = 0;
                else if (n > 255) n -= 255;

                myWriter.append(String.valueOf(n));
                if (colCount >= 3) {
                    colCount = 0;
                    myWriter.append('\n');
                } else {
                    colCount++;
                    myWriter.append(' ');
                }

            }
            myWriter.close();
            System.out.println("Successfully wrote to the file " + fileName + ".");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
