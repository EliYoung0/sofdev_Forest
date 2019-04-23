import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

class Prop {

    private static Properties prop; //Properties file that has been created

    /**
     * Creates a new properties file to save properties for batch processing
     */
    static void createProperties(){
        prop = new Properties();
        OutputStream output = null;
        try{
            output = new FileOutputStream("./config.properties");
            prop.store(output,null);
        }catch (IOException e){ e.printStackTrace(); }
        finally {
            if(output!=null){
                try{
                    output.close();
                }catch (IOException e){e.printStackTrace();}
            }
        }
    }

    /**
     * Adds file path of all files to be processed to existing properties file
     * @param value string array of paths to be processed
     */
    static void addFiles(String[] value){
        OutputStream output = null;
        try{
            output = new FileOutputStream("./config.properties");
            StringBuilder path= new StringBuilder();
            for(int i=0; i<value.length;i++){
                path.append(value[i]);
                if(i<value.length-1){
                    path.append(",");
                }
            }
            prop.setProperty("path", path.toString());
            prop.store(output,null);
        }catch (IOException e){ e.printStackTrace(); }
        finally {
            if(output!=null){
                try{
                    output.close();
                }catch (IOException e){e.printStackTrace();}
            }
        }
    }

    /**
     * Adds a property to existing property file
     * @param key name of property to be saved
     * @param value value of property being saved
     */
    static void addProperty(String key, String value){
        OutputStream output = null;
        try{
            output = new FileOutputStream("./config.properties");
            prop.setProperty(key,value);
            prop.store(output,null);
        }catch (IOException e){ e.printStackTrace(); }
        finally {
            if(output!=null){
                try{
                    output.close();
                }catch (IOException e){e.printStackTrace();}
            }
        }
    }

    /**
     * Deletes config.properties file if it exists.
     */
    static void deleteProperties(){
        File f = new File("./config.properties");
        if(f.exists()) {
            boolean success = f.delete();
            if (!success) {
                System.out.println("Failed to delete config.properties.");
            }
        }
    }

}
