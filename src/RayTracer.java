import export.FormatWriter;
import export.ImageWriter;
import export.PpmWriter;
import scene.Scene;
import scene.SceneImporter;

/*
    A simple ray tracer by Taylor Dall
 */
public class RayTracer {

    private static final String PREPEND_STRING = "rtx_";

    public static void main(String[] args) {

        String format = "png";
        if (args.length < 3)
            usage();

        String jsonFile = args[0];
        String width = args[1];
        String height = args[2];
        if (args.length > 3)
            format = args[3];

        Dimensions dim = validateDimensions(width, height);
        Scene scene = SceneImporter.importScene(jsonFile);

        String name = PREPEND_STRING + scene.name + "_" + width + "x" + height;
        ImageWriter imageWriter = getImageWriter(format, name, dim);

        new RayTracer().rayTraceScene(scene, dim.width, dim.height, imageWriter);
    }

    public void rayTraceScene(Scene scene, int width, int height, ImageWriter imageWriter) {
        scene.rayTrace(width, height, imageWriter);
    }

    private static ImageWriter getImageWriter(String format, String name, Dimensions dim) {

        ImageWriter imageWriter = null;

        switch (format) {
            case "ppm" -> imageWriter = new PpmWriter(name, dim.width, dim.height);
            case "jpeg" -> imageWriter = new FormatWriter(name, dim.width, dim.height, FormatWriter.Format.JPEG);
            case "png" -> imageWriter = new FormatWriter(name, dim.width, dim.height, FormatWriter.Format.PNG);
            case "gif" -> imageWriter = new FormatWriter(name, dim.width, dim.height, FormatWriter.Format.GIF);
            case "bpm" -> imageWriter = new FormatWriter(name, dim.width, dim.height, FormatWriter.Format.BPM);
            case "wbpm" -> imageWriter = new FormatWriter(name, dim.width, dim.height, FormatWriter.Format.WBPM);
            default -> {
                System.err.printf("%s is an invalid image type\n", format);
                System.exit(-1);
            }
        }
        return imageWriter;
    }


    private static void usage() {
        System.err.println("\nusage: java RayTracer <json_filename> <width> <height> <format>\n" +
                "supported formats: png (default), jpg, gif, ppm, bpm, wbpm\n");
        System.exit(-1);
    }

    private static class Dimensions {
        int width;
        int height;
    }

    private static Dimensions validateDimensions(String w, String h) {
        Dimensions dimensions = new Dimensions();
        try {
            dimensions.width = Integer.parseInt(w);
            dimensions.height = Integer.parseInt(h);
        } catch (NumberFormatException e) {
            System.err.printf("(%s,%s) = invalid number values\n", w, h);
            System.exit(-1);
        }

        if (dimensions.width < 1 || dimensions.height < 1) {
            System.err.printf("(%s,%s) = number must be  values\n", w, h);
            System.exit(-1);
        }
        return dimensions;
    }
}