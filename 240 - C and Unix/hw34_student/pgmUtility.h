//
//  pgmUtility.h
//  cscd240pgm
//
//

#ifndef cscd240pgm_pgmUtility_h
#define cscd240pgm_pgmUtility_h

#define rowsInHeader 4      // number of rows in image header
#define maxSizeHeadRow 200  // maximal number characters in one row in the header



/**
 *  Function Name: 
 *      pgmRead()
 *      pgmRead() reads in a pgm image using file I/O, you have to follow the file format.
 *      
 *  @param[in,out]  header  holds the header of the pgm file in a 2D character array.
 *                          We need this header information, because
 *                          it will potentially be modified in pgmDrawCircle() or pgmDrawEdge() function. 
 *                          We have to write the modified header back to a new image file in pgmWrite() function.
 *  @param[in,out]  numRows describes how many rows of pixels in the image.
 *  @param[in,out]  numCols describe how many pixels in each row in the image.
 *  @param[in]      in      FILE pointer, points to an opened image file that we like to read in.
 *  @return         If successful, return all pixels in the pgm image, which is an int **, equivalent to
 *                  a 2D array. Otherwise null.
 *
 */
int ** pgmRead( char **header, int *numRows, int *numCols, FILE *in  );


/**
 *  Function Name:
 *      pgmDrawCircle()
 *      pgmDrawCircle() draw a circle on the image by setting relavant pixel intensity values to Zero.
 *
 *  @param[in,out]  pixels  holds all pixels in the pgm image, which a 2D integer array. The array
 *                          is modified after the drawing.
 *  @param[in]      numRows describes how many rows of pixels in the image.
 *  @param[in]      numCols describes how many columns of pixels in one row in the image.
 *  @param[in]      centerCol specifies at which column you like to center your circle.
 *  @param[in]      centerRow specifies at which row you like to center your circle.
 *                        centerCol and centerRow defines the center of the circle.
 *  @param[in]      radius    specifies what the radius of the circle would be, measured in number of pixels.
 *  @param[in,out]  header returns the new header after draw. 
 *                  Drawing the circle might change the maximum intensity value in the image, so we
 *                  have to change maximum intensity value in the header accordingly.
 *  @return         return 1 if max intensity is changed, otherwise return 0;
 */
int pgmDrawCircle( int **pixels, int numRows, int numCols, int centerRow,
                  int centerCol, int radius, char **header );


/**
 *  Function Name:
 *      pgmDrawEdge()
 *      pgmDrawEdge() draws a black edge frame around the image by setting relavant pixels to Zero.
 *
 *  @param[in,out]  pixels  holds all pixels in the pgm image, which a 2D integer array. The array
 *                          is modified after the drawing.
 *  @param[in]      numRows describes how many rows of pixels in the image.
 *  @param[in]      numCols describes how many columns of pixels in one row in the image.
 *  @param[in]      edgeWidth specifies how wide the edge frame would be, measured in number of pixels.
 *  @param[in,out]  header returns the new header after draw.
 *                  The function might change the maximum intensity value in the image, so we
 *                  have to change the maximum intensity value in the header accordingly.
 *
 *  @return         return 1 if max intensity is changed by the drawing, otherwise return 0;
 */
int pgmDrawEdge( int **pixels, int numRows, int numCols, int edgeWidth, char **header );



/**
 *  Function Name:
 *      pgmWrite()
 *      pgmWrite()  writes headers and pixels into a pgm image using file I/O.
 *                  writing back image has to strictly follow the image format.
 *
 *  @param[in]  header  holds the header of the pgm file in a 2D character array.
 *                          We write the header back to a NEW image file on disk.
 *  @param[in]  pixels  holds all pixels in the pgm image, which a 2D integer array.
 *  @param[in]  numRows describes how many rows of pixels in the image.
 *  @param[in]  numCols describe how many columns of pixels in one row in the image.
 *  @param[in]  out     FILE pointer, points to an opened text file that we like to write into.
 *  @return     return 0 if the function successfully writes the header and pixels into file.
 *                          else return -1;
 */
int pgmWrite( const char **header, const int **pixels, int numRows, int numCols, FILE *out );


/**
 *  Function Name:
 *      distance()
 *      distance() returns the Euclidean distance between two pixels.
 *
 *  @param[in]  p1  coordinates of pixel one, p1[0] is for row number, p1[1] is for column number.
 *  @param[in]  p2  coordinates of pixel two, p2[0] is for row number, p2[1] is for column number.
 *  @return         return distance between p1 and p2
 */
double distance( int p1[], int p2[] );


#endif
