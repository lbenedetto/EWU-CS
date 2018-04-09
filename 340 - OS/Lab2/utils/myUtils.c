#include "myUtils.h"

void strip(char *array)
{
	if(array == NULL)
	{
		perror("array is null");
		exit(-99);
	}// end if

	int len = strlen(array), x = 0;
   
	while(array[x] != '\0' && x < len)
	{
	  if(array[x] == '\r')
		 array[x] = '\0';

	  else if(array[x] == '\n')
		 array[x] = '\0';

	  x++;

	}// end while
   
}// end strip



int menu()
{
	int num;
	
	do
	{
		printf("Please choose from the following\n");
		printf("1) Sort by Symbol\n");
		printf("2) Sort by Company Name\n");
		printf("3) Sort by Price\n");
		printf("4) Quit\n");
		printf("Choice --> ");
		scanf("%d", &num);
	}while(num < 1 || num > 4);

	while(fgetc(stdin) != '\n');
   
	return num;
}// end menu



