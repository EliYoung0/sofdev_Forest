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
    private static int m;
    private static int n;
    private static double max;
    private static BufferedImage image;
    private static double[] DN;
    private static double[][] blue;
    private static int maxright;
    private static int maxleft;
    private static double average;
    private static int maxfrequency;
    private static int uc;
    private static int lc;
    static double[][] gapmask;

    /**
     * Caller method for nobis. Calls inner nobis with upper and lower threshold limits
     * Limits should eventually be input (lc and uc) however using defaults from matlab atm.
     *
     * @param image original color image input
     * @param mask  2d boolean array defining region of pixels to be used
     * @return returns black & white image created by nobis algorithm
     */
    static BufferedImage nobis(BufferedImage image, boolean[][] mask) {

        return nobis(image, 140, 170, mask);
    }

    /**
     * Creates black & white image from image using nobis method
     *
     * @param image color image to be processed
     * @param liml  lower threshold limit
     * @param limh  upper threshold limit
     * @param mask  image mask of image. Currently not used
     * @return black & white image of input image
     */
    private static BufferedImage nobis(BufferedImage image, int liml, int limh, boolean[][] mask) {
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

        gapmask = new double[m][n];

        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                Color black = new Color(0, 0, 0);
                Color white = new Color(255, 255, 255);
                //Compares pixel to threshold
                if (blue[x][y] > tr) {
                    image.setRGB(x, y, white.getRGB());
                    gapmask[x][y] = 1;
                } else {
                    image.setRGB(x, y, black.getRGB());
                    gapmask[x][y] = 0;
                }
            }
        }
        return image;
    }

    /**
     * returns the max value in the array
     *
     * @param a array to find the max within
     * @return max array value
     */
    private static double max(double[] a) {
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
     *
     * @param db  array of 2d arrays of change in pixel value of image for each direction
     * @param use array of arraylist of pixels to use from each db array of same index
     * @return the sum of the pixel change in used pixels over the number of pixels used
     */
    private static double average(double[][][] db, ArrayList<int[]>[] use) {
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
     *
     * @param a first 1d array of doubles
     * @param b second 1d array of doubles
     * @param l length of arrays
     * @return 1d array of absolute values of differences between a and b
     */
    private static double[] abSubOneD(double[] a, double[] b, int l) {
        double[] c = new double[l];
        for (int i = 0; i < l; i++) {
            c[i] = Math.abs(a[i] - b[i]);
        }
        return c;
    }

    /**
     * Creates a 2d array of absolute value of differences between 2 2d arrays of same size
     *
     * @param a first 2d array of doubles
     * @param b second 2d array of doubles
     * @param l length of outer array
     * @param m length of inner array
     * @return 2d array of absolute values of differences between a and b
     */
    private static double[][] abSubTwoD(double[][] a, double[][] b, int l, int m) {
        double[][] c = new double[l][m];
        for (int i = 0; i < l; i++) {
            c[i] = abSubOneD(a[i], b[i], m);
        }
        return c;
    }

    /**
     * Turns a bufferedImage into a 2d array of the blue pixel values
     *
     * @param input image to be used
     * @param w     width of image
     * @param h     height of image
     * @return 2d array of blue pixel values of image
     */
    private static double[][] toArray(BufferedImage input, int w, int h) {
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
     *
     * @param a  array to get subarray of
     * @param x0 starting x for subarray
     * @param x1 ending x for subarray, inclusive
     * @param y0 starting y for subarray
     * @param y1 ending y for subarray, inclusive
     * @return sub array a from (x0,y0) to (x1,y1)
     */
    private static double[][] getSubArray(double[][] a, int x0, int x1, int y0, int y1) {
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
     *
     * @param path filepath to image to be processed
     * @return Black & white image created by single binary algorithm
     * @throws IOException throws exception if file does not exist
     */
    static BufferedImage single(String path) throws IOException {
        return single(image);
    }

    /**
     * Creates Black & white version of image using Single Binary method
     *
     * @param image original image to be processed
     * @return black & white version of image
     */
    static BufferedImage single(BufferedImage image) {

        gapmask = new double[m][n];

        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                Color black = new Color(0, 0, 0);
                Color white = new Color(255, 255, 255);
                //Compares pixel to threshold
                if (blue[x][y] > (lc + (0.5 * (uc - lc)))) {
                    image.setRGB(x, y, white.getRGB());
                    gapmask[x][y] = 1;
                } else {
                    image.setRGB(x, y, black.getRGB());
                    gapmask[x][y] = 0;
                }
            }
        }

        return image;
    }

    /**
     * Caller for DHP algorithm with image path provided
     *
     * @param path filepath to image to be processed
     * @return Black & white image created by DHP algorithm
     * @throws IOException throws exception if file does not exist
     */
    static BufferedImage dhp(String path) throws IOException {
        return dhp(image);
    }

    /**
     * Creates Black & white version of image using DHP method
     *
     * @param image original image to be processed
     * @return black & white version of image
     */
    private static BufferedImage dhp(BufferedImage image) {

        double tl = lc + ((uc - lc) * (0.25));
        double th = lc + ((uc - lc) * (0.75));

        double temp;
        double temp1;
        double temp2 = th - tl;

        gapmask = new double[m][n];

        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                temp1 = (blue[x][y] - tl);
                temp = temp1 / temp2;
                if (temp > 1.0) {
                    image.setRGB(x, y, white.getRGB());
                    gapmask[x][y] = 1;
                } else if (temp < 0.0) {
                    image.setRGB(x, y, black.getRGB());
                    gapmask[x][y] = 0;
                } else {
                    image.setRGB(x, y, (int) (temp) * 255);
                    gapmask[x][y] = temp;
                }
            }
        }

        return image;
    }

    /**
     * Calculates maxleft, maxright, average pixels, max frequency, upper corner and lower corner
     * Called from Thresholder to make values available to all algorithms
     */
    static void calcValues() {

        {
            try {
                image = ImageIO.read(new File(Thresholder.path));
                m = image.getWidth();
                n = image.getHeight();

                //Turn Image into array of blue pixel values
                blue = toArray(image, image.getWidth(), image.getHeight());
                DN = new double[256];

                //fill DN array to make "histogram"
                makeHistogram(DN, blue, image);

                int L1 = 5;
                int L2 = 55;
                int R1 = 200;
                int R2 = 250;

                //find maxright and maxleft
                maxleft = findMax(L1, L2, DN);
                maxright = findMax(R1, R2, DN);

                while((L2 -maxleft) < 10){
                    L2 = L2 + 25;
                    maxleft = findMax(L1, L2, DN);
                }

                while((maxright -R1) < 10) {
                    R1 = R1 - 25;
                    maxright = findMax(R1, R2, DN);
                }

                average = averagePixels(DN);

                if(max >average) {
                    maxfrequency = (int) average;
                }else {
                    maxfrequency = (int) max;
                }

                //Find first and last nonempty bin
                int rbin = 255;
                while(DN[rbin]==0) {
                    rbin--;
                }

                int lbin = 0;
                while(DN[lbin]==0){
                    lbin++;
                }

                int slope = (maxfrequency) / (maxright - lbin);
                uc = upperCorner(DN, slope, maxfrequency, maxleft, maxright);

                slope =(maxfrequency)/(maxleft -rbin);
                lc = lowerCorner(DN, slope, maxfrequency, maxleft, maxright);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Finds the DN (digital number) that corresponds to the most pixels in the image
     *
     * @param lo low DN cutoff of window to be analyzed
     * @param hi high DN cutoff of window to be analyzed
     * @param DN array of DNs of all pixels in the image
     * @return int value of DN corresponding to the most pixels
     */
    private static int findMax(int lo, int hi, double[] DN) {
        int max = lo;
        for (int i = lo; i <= hi; i++) {
            if (DN[i] > DN[max]) {
                max = i;
            }
        }
        return max;
    }

    /**
     * Calculates the average DN of the image's pixels
     *
     * @param DN array of DNs of image's pixels
     * @return double average DN
     */
    private static double averagePixels(double[] DN) {
        double total = 0;
        for (int i = 0; i <= 255; i++) {
            total += DN[i];
        }
        return total / 255;
    }

    /**
     * Calculates upper corner value needed to calculate various thresholds
     *
     * @param DN           array of DN values of image's pixels
     * @param slope        from DN of maximum frequency to highest or lowest non-empty bin (DN value)
     * @param maxfrequency DN value corresponding to the most pixels
     * @param maxleft      DN with the most pixels within a given window on the left of the histogram
     * @param maxright     DN with the most pixels within a given window on the right of the histogram
     * @return int DN upper corner calculated using equation from MacFarlane 2011
     */
    private static int upperCorner(double[] DN, int slope, int maxfrequency, int maxleft, int maxright) {
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

    /**
     * Calculates lower corner value needed to calculate various thresholds
     *
     * @param DN           array of DN values of image's pixels
     * @param slope        from DN of maximum frequency to highest or lowest non-empty bin (DN value)
     * @param maxfrequency DN value corresponding to the most pixels
     * @param maxleft      DN with the most pixels within a given window on the left of the histogram
     * @param maxright     DN with the most pixels within a given window on the right of the histogram
     * @return int DN lower corner calculated using equation from MacFarlane 2011
     */
    private static int lowerCorner(double[] DN, int slope, int maxfrequency, int maxleft, int maxright) {
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
     *
     * @param img1 first image
     * @param img2 second image
     * @return true if all pixels in same locations have same values, false otherwise.
     */
    static boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
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

    /**
     * Makes histogram of DN (digital number) values of each pixel in the image
     * @param DN array of DN values
     * @param blue array of blue channel of image
     * @param image image to be processed
     */
    private static void makeHistogram(double[] DN, double[][] blue, BufferedImage image) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if ((((i - Circle.circleX) * (i - Circle.circleX)) + ((j - Circle.circleY) * (j - Circle.circleY))) <= (Circle.circleR * Circle.circleR)) {
                    DN[(int) blue[i][j]]++;
                    if (DN[(int) blue[i][j]] > max) {
                        max = DN[(int)blue[i][j]];
                    }
                }
            }
        }
    }
    /**
     * Caller for local algorithm with image path provided
     *
     * @param path filepath to image to be processed
     * @return Black & white image created by the local algorithm
     * @throws IOException throws exception if file does not exist
     */
    static BufferedImage local(String path, boolean[][] mask) throws IOException {
        return local(ImageIO.read(new File(path)), mask);
    }

    /**
     * Creates Black & white version of image using the local method
     *
     * @param image original image to be processed
     * @return black & white version of image
     */
    static BufferedImage local(BufferedImage image, boolean[][] mask) {
        int m = image.getWidth();
        int n = image.getHeight();
        gapmask = new double[m][n];

        //Turn Image into array of blue pixel values
        double[][] blue = toArray(image, m, n);

        //for every row
        for(int x1 = 0; x1 < n; x1++) {
            //for every column
            for (int y1 = 0; y1 < m; y1++) {
                //if not in the mask (false outside image, true inside)
                if (mask[x1][y1]) {
                    //if it is below the lower corner, turn it black
                    if (blue[x1][y1] <= lc) {
                        image.setRGB(x1, y1, black.getRGB());
                        gapmask[x1][y1] = 0;
                    }
                    //if it is above the upper corner, turn it white
                    else if (blue[x1][y1] >= uc) {
                        image.setRGB(x1, y1, white.getRGB());
                        gapmask[x1][y1] = 1;
                    }
                    //otherwise (mixed pixels) run the algorithm
                    else {
                        //go in a circle around it (5 sq) adding together the blue values that aren't in the mask
                        double avg = 0;
                        int count = 0;
                        for (int x2 = -2; x2 < 3; x2++) {
                            for (int y2 = -2; y2 < 3; y2++) {
                                if ((x1 + x2 > 0) && (y1 + y2 > 0) && (x1 + x2 < m) && (y1 + y2 < n)) {
                                    if (mask[x1 + x2][y1 + y2]) {
                                        avg += blue[x1 + x2][y1 + y2];
                                        count++;
                                    }
                                }
                            }
                        }
                        //figure out the average
                        avg = avg / count;
                        //if this pixel is above the average
                        if (blue[x1][y1] > avg) {
                            //make it white
                            image.setRGB(x1, y1, white.getRGB());
                            gapmask[x1][y1] = 1;
                        }
                        //else
                        else {
                            //make it black
                            image.setRGB(x1, y1, black.getRGB());
                            gapmask[x1][y1] = 0;
                        }
                    }
                }
            }
        }

        //return the created image
        return image;
    }
}