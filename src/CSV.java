import java.io.*;

abstract class CSV {
    static String write(String[] data) throws IOException {
        File csv = new File(data[0].substring(0,data[0].indexOf('.'))+ "_" + java.time.LocalDate.now()+".csv");
        FileWriter fw = new FileWriter(csv);
        fw.write("Filepath,Filename,Algorithm,Manual Threshold,ISF,Gap Fraction");
        fw.append('\n');

        fw.append(data[0].substring(0,data[0].lastIndexOf('/')+1));
        fw.append(',');
        fw.append(data[0].substring(data[0].lastIndexOf('/')+1));
        for (int i = 1; i < 5; i++) {
            fw.append(',');
            fw.append(data[i]);
        }
        fw.close();
        return csv.getAbsolutePath();
    }

    static void writeTo(String path,String[] data) throws IOException {
        FileWriter fw = new FileWriter(path,true);
        fw.write('\n');
        fw.append(data[0].substring(0,data[0].lastIndexOf('/')+1));
        fw.append(',');
        fw.append(data[0].substring(data[0].lastIndexOf('/')+1));
        for (int i = 1; i < 5; i++) {
            fw.append(',');
            fw.append(data[i]);
        }
        fw.close();
    }

}
