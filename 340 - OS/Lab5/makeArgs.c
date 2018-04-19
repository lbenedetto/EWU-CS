#include "makeArgs.h"

void clean(int argc, char **argv)
{

}// end clean

void printargs(int argc, char **argv)
{
	int x;
	for(x = 0; x < argc; x++)
		printf("%s\n", argv[x]);

}// end printargs

int makeargs(char *s, char *** argv)
{
	argv[0] = calloc(strlen(s), sizeof(char));
	const char t = ' ';
	char *token;
	int i = 0;
	/* get the first token */
	token = strtok(s, &t);

	/* walk through other tokens */
	while( token != NULL ) {
		argv[0][i] = token;
		token = strtok(NULL, &t);
		i++;
	}

	return i;

}// end makeArgs
