#include "fileUtils.h"
#include "myUtils.h"

FILE * openInputFile_Prompt()
{
   char temp[100];
   printf("Please enter the name of the input file ");
   fgets(temp, 100, stdin);
   strip(temp);
   FILE * fin = fopen(temp, "r");

   while(fin == NULL)
   {
      printf("Please enter the name of the input file ");
      fgets(temp, 100, stdin);
      strip(temp);
      fin = fopen(temp, "r");
   }// end while

   return fin;
}// end openInputFile_Prompt

FILE * openInputFileType_Prompt(char * type)
{
   char temp[100];
   printf("Please enter the name of the %s input file ", type);
   fgets(temp, 100, stdin);
   strip(temp);
   FILE * fin = fopen(temp, "r");

   while(fin == NULL)
   {
      printf("Please enter the name of the input file ");
      fgets(temp, 100, stdin);
      strip(temp);
      fin = fopen(temp, "r");
   }// end while

   return fin;
}// end openInputFileType_Prompt


int countRecords(FILE * fin, int linesPerRecord)
{
	if(fin == NULL || linesPerRecord <= 0)
	{
		perror("fin is null or linesPerRecord<=0");
		exit(-99);
	}// end if

   int count = 0;
   char temp[MAX];
   fgets(temp, MAX, fin);
   while(!feof(fin))
   {
      count ++;
      fgets(temp, MAX, fin);

   }// end while

   if(count == 0)
   {
   		perror("count is 0");
   		exit(-99);
   }// end if

   rewind(fin);
   return count / linesPerRecord;
}// end count records


int readTotal(FILE * fin)
{
	if(fin == NULL)
	{
		perror("fin is null");
		exit(-99);
	}// end if

	int total;
	char temp[MAX];
	fgets(temp, MAX, fin);

	total = atoi(temp);
	if(total <= 0)
	{
		perror("total is <= 0");
		exit(-99);
	}// end if

	return total;

}// end readTotal


