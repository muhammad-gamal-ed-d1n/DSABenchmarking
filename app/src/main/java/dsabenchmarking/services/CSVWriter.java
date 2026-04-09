package dsabenchmarking.services;

import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    FileWriter writer;

    public CSVWriter(String filename) throws IOException {
        writer = new FileWriter(filename);
        writer.write("Algorithm, Input Type, Size, Time\n");
    }

    public void write(String algorithm, String inputType, int size, long time) throws IOException {
        writer.write(algorithm + ", " + inputType + ", " + size + ", " + time + "\n");
    }

    public void close() throws IOException {
        writer.close();
    }
}
