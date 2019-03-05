import java.awt.image.BufferedImage;
import java.util.Arrays;

public abstract class Algorithms {

    static BufferedImage Nobis(BufferedImage blue) {
        int[] limit = {140, 170};
        int m = blue.getWidth();
        int n = blue.getHeight();
        int[][] blueImage;

        int[][][] db = new int[4][m - 1][n - 1];
    }

    private int[] addOneD(int[] a,int[] b, int l){
        int[] c = new int[l];
        for (int i = 0; i < l; i++) {
            c[i]=a[i]+b[i];
        }
        return c;
    }

    private int[][] addTwoD(int[][] a,int[][] b, int l, int m){
        int[][] c = new int[l][m];
        for (int i = 0; i < l; i++) {
            c[i]=addOneD(a[i],b[i],m);
        }
        return c;
    }

    private int[] subOneD(int[] a,int[] b, int l){
        int[] c = new int[l];
        for (int i = 0; i < l; i++) {
            c[i]=a[i]-b[i];
        }
        return c;
    }

    private int[][] subTwoD(int[][] a,int[][] b, int l, int m){
        int[][] c = new int[l][m];
        for (int i = 0; i < l; i++) {
            c[i]=subOneD(a[i],b[i],m);
        }
        return c;
    }
}
