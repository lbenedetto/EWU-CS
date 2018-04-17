#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 100

int main()
{
	char temp[MAX];
	FILE * fin = fopen("input.txt", "r");

	if(fin == NULL)
	{
		perror("File did not open");
		exit(-1);
	}// end if
	
	fgets(temp, MAX, fin);

	while(!feof(fin))
	{
		printf("%s", temp);
		fgets(temp, MAX, fin);

	}// end while

	fclose(fin);

	return 0;

}// end main

