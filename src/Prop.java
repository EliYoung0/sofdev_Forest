import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Prop {

    private static Properties prop;

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

    static void addProperties(String key,String value){
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

    static void deleteProperties(){
        File f1 = new File("./config.properties");
        boolean success = f1.delete();
        if (!success) {
            System.out.println("Failed to delete");
        }
    }

}
