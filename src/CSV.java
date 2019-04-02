import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CSV {
    static void write(String[] data) throws IOException {
        File csv = new File(data[0].substring(0,data[0].indexOf('.'))+ "_" + java.time.LocalDate.now()+".csv");
        FileWriter fw = new FileWriter(csv);
        fw.append("Filename,Algorithm,ISF,Gap Fraction");
        fw.append("\n");
        for (String d:data) {
            fw.append(d);
        }
        fw.flush();
        fw.close();
    }

    static void writeTo(String path,String[] data) throws IOException {
        FileWriter fw = new FileWriter(path);
        for (String d:data) {
            fw.append(d);
        }
        fw.flush();
        fw.close();
    }

    public static void main(String[] args) throws IOException {
        write(new String[]{"name.txt"});
    }
}
