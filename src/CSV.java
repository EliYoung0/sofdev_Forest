import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CSV {
    static String write(String[] data) throws IOException {
        File csv = new File(data[0].substring(0,data[0].indexOf('.'))+ "_" + java.time.LocalDate.now()+".csv");
        FileWriter fw = new FileWriter(csv);
        fw.write("Filename,Algorithm,Manual Threshold,ISF,Gap Fraction");
        fw.append('\n');
        for (String d:data) {
            fw.append(d);
            fw.append(',');
        }
        fw.close();
        return csv.getAbsolutePath();
    }

    static void writeTo(String path,String[] data) throws IOException {
        FileWriter fw = new FileWriter(path,true);
        fw.write('\n');
        for (String d:data) {
            fw.append(d);
            fw.append(',');
        }
        fw.close();
    }

}
