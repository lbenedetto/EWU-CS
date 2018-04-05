#include "genericArray.h"
#include "myInt.h"
#include "myWord.h"

int main()
{
	int length = 0;
	GenericArray * array = NULL;

	array = fillArray(buildInt, stdin, &length);
	printArray(array, length, printInt);
	cleanArray(array, length, cleanInt);
	array = NULL;	

	array = fillArray(buildWord, stdin, &length);
	printArray(array, length, printWord);
	cleanArray(array, length, cleanWord);	
	array = NULL;

	return 0;
}// end main

