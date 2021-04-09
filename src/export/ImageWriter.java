package export;

public interface ImageWriter {
    void write(int[] data);

    void open();

    void close();
}
