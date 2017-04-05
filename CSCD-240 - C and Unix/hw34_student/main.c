//
//  main.c
//  cscd240PGM
//
//

#include <stdio.h>
#include <stdlib.h>

#include "pgmUtility.h"

int main(int argc, const char *argv[]) {

    //printf("Program name: %s\n", argv[0]);
    int edgeWidth = 0;
    int centerR = 0, centerC = 0, radius = 0;
    int i;

    int drawWhat = 0;
    char infile[20];
    char outfile[20];

    printf("Please enter the input file name, including its file extension:");
    scanf("%s", infile);
    printf("Please enter the output file name, including its file extension:");
    scanf("%s", outfile);

    do {
        printf("------------------Menu--------------:\n");
        printf("1--draw an edge in a picture.\n");
        printf("2--draw a circle in a picture. \n");
        printf("------------------------------------:\n");
        printf("Please choose an menu item: ");
        scanf("%d", &drawWhat);
    } while (drawWhat != 1 && drawWhat != 2);

    //scan in the parameters
    if (drawWhat == 1) //handle edge
    {
        printf("Please enter the egde width measured in number of pixels: ");
        scanf("%d", &edgeWidth);
    }
    else if (drawWhat == 2) //handle circle
    {
        printf("Please enter the circle center's row index and column index: ");
        scanf("%d %d", &centerR, &centerC);
        printf("Please enter the radius of the circle: ");
        scanf("%d", &radius);
    }

    //printf("In is %s\n", infile);

    //Open file for reading and writing
    FILE *in = fopen(infile, "r");
    if (!in) {
        printf("No such input file is found! \n");
        exit(1);
    }
    FILE *out = fopen(outfile, "w");
    if (!out) {
        printf("Open output file fails!\n");
        exit(1);
    }

    // Read in picture
    int numRows = 0, numCols = 0;
    char **header = (char **) malloc(rowsInHeader * sizeof(char *)); //allocate header of 2D char array

    for (i = 0; i < rowsInHeader; i++)
        header[i] = (char *) malloc(maxSizeHeadRow * sizeof(char)); //allocate each row of the 2D array

    int **pixels = pgmRead(header, &numRows, &numCols, in);
    //printf("rows: %d, cols: %d\n", numRows, numCols);

    // draw
    if (drawWhat == 1)
        pgmDrawEdge(pixels, numRows, numCols, edgeWidth, header);
    else if (drawWhat == 2)
        pgmDrawCircle(pixels, numRows, numCols, centerR,
                      centerC, radius, header);



    // write image back
    pgmWrite((const char **) header, (const int **) pixels, numRows, numCols, out);

    fclose(in);
    fclose(out);

    //In the space below, free all memories used by this program down here

    return 0;
}

