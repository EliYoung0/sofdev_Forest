
/*
This is not a true IndirectSiteFactor calculator.
True ISF would have the option for multiple locations in the world,
and would calculate the zenith based on the time of day, time of year, and N/S latitude.
(This would mean that the zenith would be more variable and move around depending on the input image
and where and when it was taken in the world. However, this would require a lot of calculations to discover,
and since this program is only for Dr. Metz's use, it will not be as relevant.)

*/
public class IndirectSiteFactor {
    void ISF(){
        double north = Circle.circleN;
        //Azimuth is the direction the Sun is shining from
        double azimuth = north - 90; //Hardcoded EAST direction of sun-shine
        //Zenith is how high in the sky the Sun is
        double zenith = Circle.circleZ; //hour of the day picture is taken at, time of year, etc.
        double zenithCentre = Circle.circleZDistance;
        double[] centre = new double[]{Circle.circleX, Circle.circleY};


    }

    /*
    go along circumference from north to azimuth
    then calculate distance from centre of image that sun is (using zenith angle with height as opposite)
    >>What is the height? What is the hypotenuse?
    >>(Since this is a sphere we can walk up the circumference until the distance from the centre creates the zenith angle height (which can be calculated using the circumference and distance from the circumference))
    */
    //Found centre using the calculated distances
    //Average distances on the plane along the multiples of 30deg or pi/6
    //Create ellipse on distances of 10degrees, or nine slices, from centre to the circumference
    //Calculate and draw slices
    //??Figure out weighting??? >Azimuth and zenith


    //Need to create a mask with the different weighted values
    //Paint onto image?
}