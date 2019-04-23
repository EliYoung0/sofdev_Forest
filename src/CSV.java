import java.io.*;

abstract class CSV {

    /**
     * Creates and adds first line of data to a CSV file at the given filepath
     * @param data String array of first data entries to be saved
     * @param path String of filepath to save csv at
     * @return String of csv filepath
     * @throws IOException throws exception if file fails to be created
     */
    static String write(String[] data,String path) throws IOException {
        //Creates new csv file at given path
        File csv = new File(path);
        FileWriter fw = new FileWriter(csv);
        //Writes column headers to csv
        fw.write("Filepath,Filename,Algorithm,Manual Threshold,ISF,Gap Fraction");
        fw.append('\n');

        //Writes file path of first image to csv
        fw.append(data[0].substring(0,data[0].lastIndexOf('/')+1));
        fw.append(',');

        //Writes file name of first image to csv
        fw.append(data[0].substring(data[0].lastIndexOf('/')+1));
        //Writes other data of first image to csv
        for (int i = 1; i < 5; i++) {
            fw.append(',');
            fw.append(data[i]);
        }
        fw.close();
        //returns filepath
        return csv.getAbsolutePath();
    }

    /**
     * Writes line of data to existing csv file
     * @param path filepath of csv file to write to
     * @param data String array of data values for given line
     * @throws IOException throws exception if file fails to open or be written to
     */
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
