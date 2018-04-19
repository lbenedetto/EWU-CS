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
	const char t = ' ';
	char *token;
	char *save = s;
	int i = 0;

	/* make some room */
	int size = countSpaces(s);
	argv[0] = malloc(size * sizeof(char **));
	if(!argv[0]) return -1;

	/* get the first token */
	token = strtok_r(save, &t, &save);

	/* walk through other tokens */
	while (token != NULL) {
		argv[0][i] = token;
		token = strtok_r(save, &t, &save);
		i++;
	}
	return i;

}// end makeArgs