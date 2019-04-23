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
        /**
         * Caller method for nobis. Calls inner nobis with upper and lower threshold limits
         * Limits should eventually be input however using defaults from matlab atm.
         * @param image original color image input
         * @param mask 2d boolean array defining region of pixels to be used
         * @return returns black & white image created by nobis algorithm
         */
        static BufferedImage nobis (BufferedImage image,boolean[][] mask){

            return nobis(image, 140, 170, mask);
        }

        /**
         * Creates black & white image from image using nobis method
         * @param image color image to be processed
         * @param liml lower threshold limit
         * @param limh upper threshold limit
         * @param mask image mask of image. Currently not used
         * @return black & white image of input image
         */
        private static BufferedImage nobis (BufferedImage image,int liml, int limh, boolean[][] mask){
            int[] limit = {liml, limh};
            int m = image.getWidth();
            int n = image.getHeight();

            //Turn Image into array of blue pixel values
            double[][] blue = toArray(image, m, n);

            //Create delta arrays of image
            double[][][] db = new double[4][m - 1][n - 1];
            db[0] = abSubTwoD(getSubArray(blue, 0, m - 2, 0, n - 2), getSubArray(blue, 1, m - 1, 0, n - 2), m - 1, n - 1);
            db[1] = abSubTwoD(getSubArray(blue, 0, m - 2, 0, n - 2), getSubArray(blue, 0, m - 2, 1, n - 1), m - 1, n - 1);
            db[2] = abSubTwoD(getSubArray(blue, 0, m - 2, 0, n - 2), getSubArray(blue, 1, m - 1, 1, n - 1), m - 1, n - 1);
            db[3] = abSubTwoD(getSubArray(blue, 1, m - 1, 0, n - 2), getSubArray(blue, 0, m - 2, 1, n - 1), m - 1, n - 1);

            double[][] ones = new double[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    ones[i][j] = 1;
                }
            }
            double[] zeroes = new double[256];
            double[][][] d1 = new double[4][m - 1][n - 1];
            ArrayList[] use = new ArrayList[4];
            for (int tr = limit[0]; tr <= limit[1]; tr++) {
                for (int a = 0; a < m; a++) {
                    for (int b = 0; b < n; b++) {
                        if (blue[a][b] <= tr) {
                            ones[a][b] = 0;
                        }
                    }
                }
                d1[0] = abSubTwoD(getSubArray(ones, 0, m - 2, 0, n - 2), getSubArray(blue, 1, m - 1, 0, n - 2), m - 1, n - 1);
                d1[1] = abSubTwoD(getSubArray(ones, 0, m - 2, 0, n - 2), getSubArray(blue, 0, m - 2, 1, n - 1), m - 1, n - 1);
                d1[2] = abSubTwoD(getSubArray(ones, 0, m - 2, 0, n - 2), getSubArray(blue, 1, m - 1, 1, n - 1), m - 1, n - 1);
                d1[3] = abSubTwoD(getSubArray(ones, 1, m - 1, 0, n - 2), getSubArray(blue, 0, m - 2, 1, n - 1), m - 1, n - 1);
                for (int e = 0; e < 4; e++) {
                    use[e] = new ArrayList<>();
                    for (int c = 0; c < m - 1; c++) {
                        for (int d = 0; d < n - 1; d++) {
                            if (d1[e][c][d] == 1) {
                                use[e].add(new int[]{c, d});
                            }
                        }
                    }
                }
                zeroes[tr + 1] = average(db, use);
            }
            int tr = limit[1];
            double max = max(zeroes);
            double thresh = max - 1;
            while (thresh == limit[1] && tr < 255) {
                tr++;
                limit[1]++;
                for (int a = 0; a < m; a++) {
                    for (int b = 0; b < n; b++) {
                        if (blue[a][b] <= tr) {
                            ones[a][b] = 0;
                        }
                    }
                }
                d1[0] = abSubTwoD(getSubArray(ones, 0, m - 2, 0, n - 2), getSubArray(blue, 1, m - 1, 0, n - 2), m - 1, n - 1);
                d1[1] = abSubTwoD(getSubArray(ones, 0, m - 2, 0, n - 2), getSubArray(blue, 0, m - 2, 1, n - 1), m - 1, n - 1);
                d1[2] = abSubTwoD(getSubArray(ones, 0, m - 2, 0, n - 2), getSubArray(blue, 1, m - 1, 1, n - 1), m - 1, n - 1);
                d1[3] = abSubTwoD(getSubArray(ones, 1, m - 1, 0, n - 2), getSubArray(blue, 0, m - 2, 1, n - 1), m - 1, n - 1);
                for (int e = 0; e < 4; e++) {
                    use[e] = new ArrayList<>();
                    for (int c = 0; c < m - 1; c++) {
                        for (int d = 0; d < n - 1; d++) {
                            if (d1[e][c][d] == 1) {
                                use[e].add(new int[]{c, d});
                            }
                        }
                    }
                }
                zeroes[tr + 1] = average(db, use);
                max = max(zeroes);
                thresh = max - 1;
            }
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    Color black = new Color(0, 0, 0);
                    Color white = new Color(255, 255, 255);
                    //Compares pixel to threshold
                    if (blue[x][y] > tr) {
                        image.setRGB(x, y, white.getRGB());
                    } else {
                        image.setRGB(x, y, black.getRGB());
                    }
                }
            }
            return image;
        }

        /**
         * returns the max value in the array
         * @param a array to find the max within
         * @return max array value
         */
        private static double max ( double[] a){
            //Sets max to be first index
            int max = 0;
            //Goes through each index
            for (int i = 1; i < 256; i++) {
                //if contents of index i is greater than contents of max sets max to i
                if (a[i] > a[max]) {
                    max = i;
                }
            }
            //returns contents max
            return a[max];
        }

        /**
         * Calculates the average pixel change in the image based on change in adjacent pixel values
         * @param db array of 2d arrays of change in pixel value of image for each direction
         * @param use array of arraylist of pixels to use from each db array of same index
         * @return the sum of the pixel change in used pixels over the number of pixels used
         */
        private static double average ( double[][][] db, ArrayList<int[]>[]use){
            double sum = 0;
            double count = 0;
            for (int i = 0; i < use.length; i++) {
                for (int[] index : use[i]) {
                    sum += db[i][index[0]][index[1]];
                    count++;
                }
            }
            return (sum / count);
        }

        /**
         * Creates a 1d array of absolute value of differences between 2 1d arrays of same length
         * @param a first 1d array of doubles
         * @param b second 1d array of doubles
         * @param l length of arrays
         * @return 1d array of absolute values of differences between a and b
         */
        private static double[] abSubOneD ( double[] a, double[] b, int l){
            double[] c = new double[l];
            for (int i = 0; i < l; i++) {
                c[i] = Math.abs(a[i] - b[i]);
            }
            return c;
        }

        /**
         * Creates a 2d array of absolute value of differences between 2 2d arrays of same size
         * @param a first 2d array of doubles
         * @param b second 2d array of doubles
         * @param l length of outer array
         * @param m length of inner array
         * @return 2d array of absolute values of differences between a and b
         */
        private static double[][] abSubTwoD ( double[][] a, double[][] b, int l, int m){
            double[][] c = new double[l][m];
            for (int i = 0; i < l; i++) {
                c[i] = abSubOneD(a[i], b[i], m);
            }
            return c;
        }

        /**
         * Turns a bufferedImage into a 2d array of the blue pixel values
         * @param input image to be used
         * @param w width of image
         * @param h height of image
         * @return 2d array of blue pixel values of image
         */
        private static double[][] toArray (BufferedImage input,int w, int h){
            double[][] output = new double[w][h];
            ColorModel c = input.getColorModel();
            Object pixel = null;
            Raster raster = input.getRaster();
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    pixel = raster.getDataElements(i, j, pixel);
                    output[i][j] = c.getBlue(pixel);
                }
            }
            return output;
        }

        /**
         * Gets a sub array of 2d array a
         * @param a array to get subarray of
         * @param x0 starting x for subarray
         * @param x1 ending x for subarray, inclusive
         * @param y0 starting y for subarray
         * @param y1 ending y for subarray, inclusive
         * @return sub array a from (x0,y0) to (x1,y1)
         */
        private static double[][] getSubArray ( double[][] a, int x0, int x1, int y0, int y1){
            double[][] b = new double[x1 - x0 + 1][y1 - y0 + 1];
            for (int i = x0; i <= x1; i++) {
                for (int j = y0; j <= y1; j++) {
                    b[i - x0][j - y0] = a[i][j];
                }
            }
            return b;
        }

        /**
         * Caller for Single Binary algorithm with image path provided
         * @param path filepath to image to be processed
         * @return Black & white image created by single binary algorithm
         * @throws IOException throws exception if file does not exist
         */
        static BufferedImage single (String path) throws IOException {
            return single(ImageIO.read(new File(path)));
        }

        /**
         * Creates Black & white version of image using Single Binary method
         * @param image original image to be processed
         * @return black & white version of image
         */
        static BufferedImage single (BufferedImage image){
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
            double[][] blue = toArray(image, m, n);

            double[] DN = new double[256];
            //fill DN array to make "histogram"
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if ((((i - Circle.circleX) * (i - Circle.circleX)) + ((j - Circle.circleY) * (j - Circle.circleY))) <= (Circle.circleR * Circle.circleR)) {
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

            while ((L2 - maxleft) < 10) {
                L2 = L2 + 25;
                maxleft = findMax(L1, L2, DN);
            }

            while ((maxright - R1) < 10) {
                R1 = R1 - 25;
                maxright = findMax(R1, R2, DN);
            }

            average = averagePixels(DN);

            if (max > average) {
                maxfrequency = (int) average;
            } else {
                maxfrequency = (int) max;
            }

            //Find first and last nonempty bin
            int rbin = 255;
            while (DN[rbin] == 0) {
                rbin--;
            }

            int lbin = 0;
            while (DN[lbin] == 0) {
                lbin++;
            }

            int slope = (maxfrequency) / (maxright - lbin);
            int uc = upperCorner(DN, slope, maxfrequency, maxleft, maxright);

            slope = (maxfrequency) / (maxleft - rbin);
            int lc = lowerCorner(DN, slope, maxfrequency, maxleft, maxright);


            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    Color black = new Color(0, 0, 0);
                    Color white = new Color(255, 255, 255);
                    //Compares pixel to threshold
                    if (blue[x][y] > (lc + (0.5 * (uc - lc)))) {
                        image.setRGB(x, y, white.getRGB());
                    } else {
                        image.setRGB(x, y, black.getRGB());
                    }
                }
            }


            return image;
        }


        static BufferedImage dhp (String path) throws IOException {
            return dhp(ImageIO.read(new File(path)));
        }

        static BufferedImage dhp (BufferedImage image){
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
            double[][] blue = toArray(image, m, n);

            //Back-correct gamma of blue channel
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    blue[i][j] = 255.0 * Math.pow((blue[i][j] / 255.0), (1.0 / 2.2));
                }
            }

            double[] DN = new double[256];
            //fill DN array to make "histogram"
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if ((((i - Circle.circleX) * (i - Circle.circleX)) + ((j - Circle.circleY) * (j - Circle.circleY))) <= (Circle.circleR * Circle.circleR)) {
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

            while ((L2 - maxleft) < 10) {
                L2 = L2 + 25;
                maxleft = findMax(L1, L2, DN);
            }

            while ((maxright - R1) < 10) {
                R1 = R1 - 25;
                maxright = findMax(R1, R2, DN);
            }

            average = averagePixels(DN);

            if (max > average) {
                maxfrequency = (int) average;
            } else {
                maxfrequency = (int) max;
            }

            //Find first and last nonempty bin
            int rbin = 255;
            while (DN[rbin] == 0) {
                rbin--;
            }

            int lbin = 0;
            while (DN[lbin] == 0) {
                lbin++;
            }

            int slope = (maxfrequency) / (maxright - lbin);
            int uc = upperCorner(DN, slope, maxfrequency, maxleft, maxright);

            slope = (maxfrequency) / (maxleft - rbin);
            int lc = lowerCorner(DN, slope, maxfrequency, maxleft, maxright);

            double tl = lc + ((uc - lc) * (0.25));
            double th = lc + ((uc - lc) * (0.75));

            double temp;
            double temp1;
            double temp2 = th - tl;

            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    temp1 = (blue[x][y] - tl);
                    temp = temp1 / temp2;
                    if (temp > 1.0) {
                        image.setRGB(x, y, white.getRGB());
                    } else if (temp < 0.0) {
                        image.setRGB(x, y, black.getRGB());
                    } else {
                        image.setRGB(x, y, (int) (temp * 255));
                        //System.out.println((int)(temp*255));
                    }
                }
            }


            return image;
        }


        private static int findMax ( int lo, int hi, double[] DN){
            int max = lo;
            for (int i = lo; i <= hi; i++) {
                if (DN[i] > DN[max]) {
                    max = i;
                }
            }
            return max;
        }

        private static double averagePixels ( double[] DN){
            double total = 0;
            for (int i = 0; i <= 255; i++) {
                total += DN[i];
            }
            return total / 255;
        }

        private static int upperCorner ( double[] DN, int slope, int maxfrequency, int maxleft, int maxright){
            double maxd = 0;
            int maxindex = 0;
            double d;
            for (int i = maxleft; i <= maxright; i++) {
                if (DN[i] < (slope * i)) {
                    d = (Math.abs((slope * i) - DN[i])) / (Math.sqrt((slope * slope) + 1));
                    if (d > maxd) {
                        maxd = d;
                        maxindex = i;
                    }
                }
            }
            return maxindex;
        }

        private static int lowerCorner ( double[] DN, int slope, int maxfrequency, int maxleft, int maxright){
            double maxd = 0;
            int maxindex = 0;
            double d;
            for (int i = maxleft; i <= maxright; i++) {
                if (DN[i] < ((slope * i) + (maxfrequency - (slope * maxright)))) {
                    d = (Math.abs((slope * i) - DN[i] + (maxfrequency - (slope * maxright)))) / (Math.sqrt((slope * slope) + 1));
                    if (d > maxd) {
                        maxd = d;
                        maxindex = i;
                    }
                }
            }
            return maxindex;

        }


        /**
         * Checks if 2 images are equal based on same pixel values
         * @param img1 first image
         * @param img2 second image
         * @return true if all pixels in same locations have same values, false otherwise.
         */
        static boolean bufferedImagesEqual (BufferedImage img1, BufferedImage img2){
            if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
                for (int x = 0; x < img1.getWidth(); x++) {
                    for (int y = 0; y < img1.getHeight(); y++) {
                        if (img1.getRGB(x, y) != img2.getRGB(x, y))
                            return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
}