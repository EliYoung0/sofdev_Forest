import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Batch {
    static void createProperties(){
        Properties prop = new Properties();
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

    static void addFiles(String[] value){
        Properties prop = new Properties();
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

    static void addProperties(String key,String value){
        Properties prop = new Properties();
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
        StringBuilder path= new StringBuilder();

        prop.setProperty(key, path.toString());
    }
}
