Forest Canopy Photograph Analysis Program
=======

This project was created with the goal of making a Java program capable of implementing various algorithms to analyze forest canopy photos and calculate various metrics including gap fraction and ISF (indirect site factor) to be used in the study of the effects of sunlight on plant growth.

## Table of Contents:
1. Installation
2. Usage
3. Algorithms
4. Authors and Acknowledgements
5. Github

## Installation
1. Download Forest.jar file from the github repository.
2. Install java from this website if you do not already have it installed: [https://www.java.com/en/download/]

## Usage
1. Open jar file
2. Select one or more jpg files to analyze or select a folder containing at least one jpg by using the browse button
or by entering the file path into the address bar.
3. After proceeding to the next screen enter the image's center x and center y pixel as well as the radius of the circular
image.
4. Press the draw circle button to display the image border and re-enter values if the border is off.
5. Enter the direction of north and press the add north button to display the direction of north.
6. Enter the time of day the image was taken at (from 0-24) to calculate ISF and press the add zenith button to display the location of the sun when the image was taken.
7. When satisfied press the save & continue button to proceed to the thresholding screen.
8. Select the algorithm you would like to use to threshold the image and wait for the black & white image to be displayed.
9. When satisfied press the save & continue button to proceed to saving the results.
10. At this point a dialog window will appear to get the location of where to save the csv file.
11. Select the location of where to save the csv file either by using the file browser or by going to a directory and entering a .csv file name, or typing the path into the address bar.
12. Press save to confirm this path and proceed to batch processing (if more than one file selected) or finish.
Or press cancel to go back to the thresholding screen.
13. If you have more than one file to process, press the start processing button to start batch processing.
14. When the batch processing button has completed, the finish and exit button will become clickable. Click this to close
the program. Results will have been saved to a .csv file in the location previously specified.

## Algorithms
* __Manual:__ User inputs a threshold from 0 to 255.  Any pixels which are brighter than that threshold are turned to white, all others are turned black.
* __Nobis:__ The algorithm iterates through every threshold values from a lower limit to an upper limit and finds the threshold with the highest contrast between neighboring pixels. Then the image is thresholded using the found value.
* __Single Binary:__ Upper and lower corners are calculated from a histogram (formatted as an array) of DN (digital number) values of all pixels in the image. The threshold used is the midpoint between the upper and lower corners; any pixels brighter than that threshold (meaning their digital number is highter than that of the threshold) are turned white, all others are turned black.
* __DHP:__ Lower threshold and upper threshold are calculated using the upper and lower corners. All pixels with DN (digital number) lower than the lower threshold are turned black, all pixels with DN higher than the upper threshold are turned white. All other "mixed" pixels are assigned a gap fraction based on their distance from the lower or upper threshold.
* __Local:__ Goes through every pixel and compares it with the mean of all the pixels in a 5x5 square centered on the target pixel.  If the target pixel is brighter than the mean, then that pixel is turned white.  Otherwise it is turned black.

## Authors and Acknowledgements:
This project was created by a group of Lewis & Clark College students:
 * Amelia Berle
 * Sara Jane Maass
 * Sherlock Ortiz
 * Eli Young

We would like to thank Professor Peter Drake for his help with our coding and
Professor Margaret Metz for her patience and support.

## Github:
Our code can be found at
[https://github.com/PeterDrake/sofdev-sp19-forest]
