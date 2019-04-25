/*
This is not a true IndirectSiteFactor calculator.
True ISF would have the option for multiple locations in the world,
and would calculate the zenith based on the time of day, time of year, and N/S latitude.
(This would mean that the zenith would be more variable and move around depending on the input image
and where and when it was taken in the world. However, this would require a lot of calculations to discover,
and since this program is only for Dr. Metz's use, it will not be as relevant.)
*/


//TODO Call this in Thresholder
public class IndirectSiteFactor {
    double[][] isfMask;

    void ISF(){
        double north = Circle.circleN;
        //Zenith is how high in the sky the Sun is, in hour decimal i.e. 1:30pm = 13.5
        double zenith = Circle.circleZ;
        double zenithCentre = Circle.circleZDistance;
        double[] centre = new double[]{Circle.circleX+Circle.circleR, Circle.circleX-Circle.circleR,
                Circle.circleY+Circle.circleR, Circle.circleY-Circle.circleR}; //X+R, X-R, Y+R, Y-R
        
        mask(zenith, zenithCentre, centre);
    }

    /*
    This calculation is done by splitting the plane into nine portions, which,
    if the Sun was at noon (peak), would mean that there would be nine portions of 10 degrees
    each on every side to the horizon.
     */
    double[][] mask(double zenith, double zenithCentre, double[] centre){
        int w = -1;//square.getWitdh();
        for(int i=0; i<9; i++) {
            for(int x=0; x<w; x++){
                for(int y=0; y<w; y++){

                }
            }
        }

        
        return isfMask;
    }
}//Calculate and draw slices
//??Figure out weighting??? >Azimuth and zenith


//Need to create a mask with the different weighted values
//Paint onto image?