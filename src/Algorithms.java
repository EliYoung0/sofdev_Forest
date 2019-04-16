import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.Color.black;
import static java.awt.Color.white;

abstract class Algorithms {

    static BufferedImage nobis(BufferedImage image,boolean[][] mask) throws IOException {
            return nobis(image,140,170,mask);
    }

    private static BufferedImage nobis(BufferedImage image, int liml, int limh, boolean[][] mask) {
        int[] limit = {liml, limh};
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
        ArrayList[] use = new ArrayList[4];
        for (int tr=limit[0];tr<=limit[1];tr++) {
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
        double max = max(zeroes);
        double thresh=max-1;
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
            max= max(zeroes);
            thresh=max-1;
        }
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                Color black = new Color(0,0,0);
                Color white = new Color(255,255,255);
                //Compares pixel to threshold
                if(blue[x][y]>tr) {
                    image.setRGB(x, y, white.getRGB());
                }
                else {
                    image.setRGB(x, y, black.getRGB());
                }
            }
        }
        return image;
    }

    private static double max(double[] a) {
        int max=0;
        for (int i = 1; i < 256; i++) {
            if(a[i]>a[max]){
                max=i;
            }
        }
        return a[max];
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
            for (int j = y0; j <= y1; j++) { b[i-x0][j-y0]=a[i][j]; }
        }
        return b;
    }

    static BufferedImage single(String path) throws IOException {
        return single(ImageIO.read(new File(path)));
    }

    static BufferedImage single(BufferedImage image) {
        int m = image.getWidth();
        int n = image.getHeight();
        double max = 0;
        int maxright;
        int maxleft;
        int L1 = 5;
        int L2 = 55;
        int R1 = 200;
        int R2 = 250;
        double average;
        int maxfrequency;

        //Turn Image into array of blue pixel values
        double[][] blue = toArray(image,m,n);

        double[] DN = new double[256];
        //fill DN array to make "histogram"
        for (int i = 0; i < m ; i++) {
            for (int j = 0; j < n; j++) {
                if((((i - Circle.circleX) * (i - Circle.circleX)) + ((j - Circle.circleY) * (j - Circle.circleY))) <= (Circle.circleR * Circle.circleR)) {
                    DN[(int) blue[i][j]]++;
                    if (DN[(int) blue[i][j]] > max) {
                        max = DN[(int) blue[i][j]];
                    }
                }
            }
        }


        //find maxright and maxleft
        maxleft = findMax(L1, L2, DN);
        maxright = findMax(R1, R2, DN);

        while((L2-maxleft)<10){
            L2 = L2+25;
            maxleft = findMax(L1, L2, DN);
        }

        while ((maxright-R1)<10){
            R1 = R1-25;
            maxright = findMax(R1, R2, DN);
        }

        average = averagePixels(DN);

        if(max > average){
            maxfrequency = (int)average;
        }
        else{
            maxfrequency = (int)max;
        }

        //Find first and last nonempty bin
        int rbin = 255;
        while(DN[rbin]==0){
            rbin--;
        }

        int lbin = 0;
        while(DN[lbin]==0){
            lbin++;
        }

        int slope = (maxfrequency)/(maxright - lbin);
        int uc = upperCorner(DN, slope, maxfrequency, maxleft, maxright);

        slope = (maxfrequency)/(maxleft - rbin);
        int lc = lowerCorner(DN, slope, maxfrequency, maxleft, maxright);


        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                Color black = new Color(0,0,0);
                Color white = new Color(255,255,255);
                //Compares pixel to threshold
                if(blue[x][y] > (lc + (0.5*(uc-lc)))) {
                    image.setRGB(x, y, white.getRGB());
                }
                else {
                    image.setRGB(x, y, black.getRGB());
                }
            }
        }


        return image;
    }





    static BufferedImage dhp(String path) throws IOException {
        return dhp(ImageIO.read(new File(path)));
    }

    static BufferedImage dhp(BufferedImage image){
        int m = image.getWidth();
        int n = image.getHeight();
        double max = 0;
        int maxright;
        int maxleft;
        int L1 = 5;
        int L2 = 55;
        int R1 = 200;
        int R2 = 250;
        double average;
        int maxfrequency;

        //Turn Image into array of blue pixel values
        double[][] blue = toArray(image,m,n);

        //Back-correct gamma of blue channel
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                blue[i][j] = 255.0 * Math.pow((blue[i][j] / 255.0), (1.0/2.2));
            }
        }

        double[] DN = new double[256];
        //fill DN array to make "histogram"
        for (int i = 0; i < m ; i++) {
            for (int j = 0; j < n; j++) {
                if((((i - Circle.circleX) * (i - Circle.circleX)) + ((j - Circle.circleY) * (j - Circle.circleY))) <= (Circle.circleR * Circle.circleR)) {
                    DN[(int) blue[i][j]]++;
                    if (DN[(int) blue[i][j]] > max) {
                        max = DN[(int) blue[i][j]];
                    }
                }
            }
        }


        //find maxright and maxleft
        maxleft = findMax(L1, L2, DN);
        maxright = findMax(R1, R2, DN);

        while((L2-maxleft)<10){
            L2 = L2+25;
            maxleft = findMax(L1, L2, DN);
        }

        while ((maxright-R1)<10){
            R1 = R1-25;
            maxright = findMax(R1, R2, DN);
        }

        average = averagePixels(DN);

        if(max > average){
            maxfrequency = (int)average;
        }
        else{
            maxfrequency = (int)max;
        }

        //Find first and last nonempty bin
        int rbin = 255;
        while(DN[rbin]==0){
            rbin--;
        }

        int lbin = 0;
        while(DN[lbin]==0){
            lbin++;
        }

        int slope = (maxfrequency)/(maxright - lbin);
        int uc = upperCorner(DN, slope, maxfrequency, maxleft, maxright);

        slope = (maxfrequency)/(maxleft - rbin);
        int lc = lowerCorner(DN, slope, maxfrequency, maxleft, maxright);

        double tl = lc + ((uc-lc)*(0.25));
        double th = lc + ((uc-lc)*(0.75));

        double temp;

        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                temp = (blue[x][y] - tl) / (th-tl);
                if(temp > 1.0){
                    image.setRGB(x, y, white.getRGB());
                }

                else if(temp < 0.0){
                    //System.out.println(temp + '\n');
                    image.setRGB(x, y, black.getRGB());
                }
                else{
                    image.setRGB(x, y, (int)temp*255);
                }
            }
        }



        return image;
    }


    private static int findMax(int lo, int hi, double[] DN){
        int max = lo;
        for(int i = lo; i<=hi; i++){
            if(DN[i] > DN[max]){
                max = i;
            }
        }
        return max;
    }

    private static double averagePixels(double[] DN){
        double total = 0;
        for(int i=0; i<=255; i++){
            total+=DN[i];
        }
        return total/255;
    }

    private static int upperCorner(double[] DN, int slope, int maxfrequency, int maxleft, int maxright){
        double maxd=0;
        int maxindex=0;
        double d;
        for(int i=maxleft; i<=maxright; i++){
            if(DN[i] < (slope*i)) {
                d = (Math.abs((slope * i) - DN[i])) / (Math.sqrt((slope * slope) + 1));
                if (d > maxd) {
                    maxd = d;
                    maxindex = i;
                }
            }
        }
        return maxindex;
    }

    private static int lowerCorner(double[] DN, int slope, int maxfrequency, int maxleft, int maxright){
        double maxd = 0;
        int maxindex=0;
        double d;
        for(int i=maxleft; i<=maxright; i++){
            if(DN[i] < ((slope * i) + (maxfrequency - (slope * maxright)))) {
                d = (Math.abs((slope * i) - DN[i] + (maxfrequency - (slope * maxright)))) / (Math.sqrt((slope * slope) + 1));
                if (d > maxd) {
                    maxd = d;
                    maxindex = i;
                }
            }
        }
        return maxindex;

    }


}
