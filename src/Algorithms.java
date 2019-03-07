import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Algorithms {

    static BufferedImage nobis(BufferedImage image) {
        int[] limit = {140, 170};
        int m = image.getWidth();
        int n = image.getHeight();

        //Turn Image into array of blue pixel values
        double[][] blue = toArray(image,m,n);

        //Create delta arrays of image
        double[][][] db = new double[4][m - 1][n - 1];
        db[0]=abSubTwoD(getSubArray(blue,0,m-2,0,n-2),getSubArray(blue,1,m-1,0,n-2),m-1,n-1);
        db[1]=abSubTwoD(getSubArray(blue,0,m-2,0,n-2),getSubArray(blue,0,m-2,1,n-1),m-1,n-1);
        db[2]=abSubTwoD(getSubArray(blue,0,m-2,0,n-2),getSubArray(blue,1,m-1,1,n-1),m-1,n-1);
        db[3]=abSubTwoD(getSubArray(blue,1,m-1,0,n-2),getSubArray(blue,0,m-2,1,n-1),m-1,n-1);

        double[][] ones = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ones[i][j]=1;
            }
        }
        double[] zeroes = new double[256];
        double[][][] d1 = new double[4][m-1][n-1];
        ArrayList<int[]>[] use = new ArrayList[4];
        for (int tr:limit) {
            for (int a = 0; a < m; a++) {
                for (int b = 0; b < n; b++) {
                    if(blue[a][b]<=tr){ ones[a][b]=0; }
                }
            }
            d1[0]=abSubTwoD(getSubArray(ones,0,m-2,0,n-2),getSubArray(blue,1,m-1,0,n-2),m-1,n-1);
            d1[1]=abSubTwoD(getSubArray(ones,0,m-2,0,n-2),getSubArray(blue,0,m-2,1,n-1),m-1,n-1);
            d1[2]=abSubTwoD(getSubArray(ones,0,m-2,0,n-2),getSubArray(blue,1,m-1,1,n-1),m-1,n-1);
            d1[3]=abSubTwoD(getSubArray(ones,1,m-1,0,n-2),getSubArray(blue,0,m-2,1,n-1),m-1,n-1);
            for (int e = 0; e < 4; e++) {
                use[e]=new ArrayList<>();
                for (int c = 0; c < m-1; c++) {
                    for (int d = 0; d < n-1; d++) {
                        if(d1[e][c][d]==1){use[e].add(new int[]{c,d});}
                    }
                }
            }
            zeroes[tr+1]=average(db,use);
        }
        int tr=limit[1];
        int max = max(zeroes,256);
        double thresh=zeroes[max]-1;
        while(thresh==limit[1]&&tr<255){
            tr++;
            limit[1]++;
            for(int a = 0; a < m; a++) {
                for (int b = 0; b < n; b++) {
                    if(blue[a][b]<=tr){ ones[a][b]=0; }
                }
            }
            d1[0]=abSubTwoD(getSubArray(ones,0,m-2,0,n-2),getSubArray(blue,1,m-1,0,n-2),m-1,n-1);
            d1[1]=abSubTwoD(getSubArray(ones,0,m-2,0,n-2),getSubArray(blue,0,m-2,1,n-1),m-1,n-1);
            d1[2]=abSubTwoD(getSubArray(ones,0,m-2,0,n-2),getSubArray(blue,1,m-1,1,n-1),m-1,n-1);
            d1[3]=abSubTwoD(getSubArray(ones,1,m-1,0,n-2),getSubArray(blue,0,m-2,1,n-1),m-1,n-1);
            for (int e = 0; e < 4; e++) {
                use[e]=new ArrayList<>();
                for (int c = 0; c < m - 1; c++) {
                    for (int d = 0; d < n - 1; d++) {
                        if(d1[e][c][d]==1){use[e].add(new int[]{c,d});}
                    }
                }
            }
            zeroes[tr+1]=average(db,use);
            max= max(zeroes,256);
            thresh=zeroes[max]-1;
        }
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                Color black = new Color(0,0,0);
                Color white = new Color(255,255,255);
                //Compares pixel to threshold
                if(blue[x][y]>thresh) {
                    image.setRGB(x, y, white.getRGB());
                }
                else {
                    image.setRGB(x, y, black.getRGB());
                }
            }
        }
        return image;
    }

    private static int max(double[] a, int l) {
        int max=0;
        for (int i = 1; i < l; i++) {
            if(a[i]>a[max]){
                max=i;
            }
        }
        return max;
    }

    private static double average(double[][][] db, ArrayList<int[]>[] use) {
        double sum = 0;
        double count = 0;
        for (int i = 0; i < use.length; i++) {
            for (int[] index:use[i]) {
                sum+=db[i][index[0]][index[1]];
                count++;
            }
        }
        return (sum/count);
    }
    private static double[] abSubOneD(double[] a,double[] b, int l){
        double[] c = new double[l];
        for (int i = 0; i < l; i++) {
            c[i]=Math.abs(a[i]-b[i]);
        }
        return c;
    }

    private static double[][] abSubTwoD(double[][] a,double[][] b, int l, int m){
        double[][] c = new double[l][m];
        for (int i = 0; i < l; i++) {
            c[i]=abSubOneD(a[i],b[i],m);
        }
        return c;
    }

    private static double[][] toArray(BufferedImage input, int w, int h){
        double[][] output=new double[w][h];
        ColorModel c = input.getColorModel();
        Object pixel= null;
        Raster raster = input.getRaster();
        for (int i = 0; i < w ; i++) {
            for (int j = 0; j < h; j++) {
                pixel = raster.getDataElements(i, j, pixel);
                output[i][j]=c.getBlue(pixel);
            }
        }
        return output;
    }

    private static double[][] getSubArray(double[][] a, int x0, int x1, int y0, int y1){
        double[][] b = new double[x1-x0+1][y1-y0+1];
        for (int i = x0; i <= x1; i++) {
            for (int j = y0; j <= y1; j++) {
                b[i-x0][j-y0]=a[i][j];
            }
        }
        return b;
    }

    public static void main(String[] args) {
        try {
            String path="/Users/kepler/Desktop/in.jpg";
            BufferedImage in = ImageIO.read(new File(path));
            BufferedImage out = nobis(in);
            File outputFile = new File("/Users/kepler/Desktop/out.jpg");

                ImageIO.write(out, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error");}
    }

}
