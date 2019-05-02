import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CSVTest {
    private String out;
    private String path;

    @BeforeEach
    void setUp() throws IOException {
        String[] data = {"1.csv","Manual","1","N/A",".5"};
        path = "/Users/kepler/IdeaProjects/sofdev-sp19-forest/test.csv";
        out = CSV.write(data,path);

    }


    @Test
    void fileCreated(){
        File f = new File(path);
        assertTrue(f.exists());
        assertTrue(out.endsWith(".csv"));
    }
    @Test
    void correctLength() throws IOException {
        ArrayList<String[]> csvdata = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(out));
        String line;
        while((line = br.readLine())!=null){
            String[] temp = line.split(",");
            csvdata.add(temp);
        }
        assertEquals(2, csvdata.size());
    }
    @Test
    void headersCorrect() throws IOException {
        String[] headers = {"Filepath","Filename","Algorithm","Manual Threshold","ISF","Gap Fraction"};
        ArrayList<String[]> csvdata = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(out));
        String line;
        while((line = br.readLine())!=null){
            String[] temp = line.split(",");
            csvdata.add(temp);
        }
        for (int i = 0; i <headers.length ; i++) {
            assertEquals(headers[i],csvdata.get(0)[i]);
        }
    }

    @Test
    void writesCorrectly() throws IOException {
        String[] data = {"1.csv","Manual","1","N/A",".5"};
        ArrayList<String[]> csvdata = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(out));
        String line;
        while((line = br.readLine())!=null){
            String[] temp = line.split(",");
            csvdata.add(temp);
        }
        for (int i = 0; i <data.length ; i++) {
            assertEquals(data[i],csvdata.get(1)[i+1]);
        }
    }

    @Test
    void outputsCorrectPath(){
        assertEquals(out, path);
    }

    @Test
    void writeTo() throws IOException {
        String[] data = {"2.csv","Manual","1","N/A",".5"};
        CSV.writeTo(path,data);
        ArrayList<String[]> csvdata = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(out));
        String line;
        while((line = br.readLine())!=null){
            String[] temp = line.split(",");
            csvdata.add(temp);
        }
        for (int i = 0; i <data.length ; i++) {
            assertEquals(data[i],csvdata.get(2)[i+1]);
        }

    }
    @AfterEach
    void tearDown() {
        File f = new File(out);
        if(f.exists()) {
            boolean success = f.delete();
            if (!success) {
                System.out.println("Failed to delete test.csv.");
            }
        }
    }
}