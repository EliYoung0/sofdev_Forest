import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropTest {

    @BeforeEach
    void setUp() {
        Prop.createProperties();
    }

    @Test
    void fileCreated(){
        File temp = new File("config.properties");
        assertTrue(temp.exists());
    }

    @Test
    void fileDeleted(){
        Prop.deleteProperties();
        File temp = new File("config.properties");
        assertFalse(temp.exists());
    }

    @Test
    void propAdded() throws IOException {
        String testExpected="1";
        Prop.addProperty("test",testExpected);
        Properties config = new Properties();
        InputStream input = new FileInputStream("config.properties");
        config.load(input);
        assertEquals(config.getProperty("test"),testExpected);
        input.close();
    }

    @Test
    void filePathAdded() throws IOException {
        String[] path = {"1","2"};
        String pathExpected = "1,2";
        Prop.addFiles(path);

        Properties config = new Properties();
        InputStream input = new FileInputStream("config.properties");
        config.load(input);
        assertEquals(config.getProperty("path"),pathExpected);
        input.close();

    }

    @AfterEach
    void tearDown() {
        Prop.deleteProperties();
    }
}