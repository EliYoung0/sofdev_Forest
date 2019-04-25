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
        mask(zenith);
    }

    /*
    This calculation is done by splitting the plane into nine portions, which,
    if the Sun was at noon (peak), would mean that there would be nine portions of 10 degrees
    each on every side to the horizon.
     */
    double[][] mask(double zenith){
        int w = Circle.circleR * 2;
        for(int x=0; x<w; x++){
            for(int y=0; y<w; y++){
                double xDist = (double)Circle.circleX - x;
                double yDist = (double)Circle.circleY - y;
                double distance = Math.sqrt(Math.pow(xDist,2) + Math.pow(yDist,2));
                double zDist = Math.sqrt(Math.pow(Circle.circleR, 2) - Math.pow(distance,2));
                double zenDistance = Circle.circleX + ((double)Circle.circleR * Math.cos((Math.PI/12 * zenith) + (Math.PI/2)));
                double zenX = zenDistance * Math.cos(Circle.circleN + (Math.PI / 2));
                double zenY = zenDistance * Math.sin(Circle.circleN + (Math.PI / 2));
                double zenZ = Math.sqrt(Math.pow(Circle.circleR,2) - Math.pow(zenDistance,2));
                isfMask[x][y] = (xDist * zenX) + (yDist * zenY) + (zDist * zenZ);
            }
        }
        return isfMask;
    }
}//Calculate and draw slices
//??Figure out weighting??? >Azimuth and zenith


//Need to create a mask with the different weighted values
//Paint onto image?