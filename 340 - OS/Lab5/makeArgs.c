#include "makeArgs.h"

int countSpaces(char * s){
	int size;
	for (size=1; s[size]; s[size]==' ' ? size++ : *s++);
	return size;
}

void clean(int argc, char **argv) {
	free(argv);
}// end clean

void printargs(int argc, char **argv) {
	int x;
	for (x = 0; x < argc; x++)
		printf("%s\n", argv[x]);

}// end printargs

int makeargs(char *s, char ***argv) {
	int size = countSpaces(s);
	argv[0] = malloc(size * sizeof(char **));
	const char t = ' ';
	char *token;
	int i = 0;
	/* get the first token */
	token = strtok(s, &t);

	/* walk through other tokens */
	while (token != NULL) {
		argv[0][i] = token;
		token = strtok(NULL, &t);
		i++;
	}
	return i;

}// end makeArgs