#include "makeArgs.h"

#define bool int
#define false 0
#define true 1

int countTokens(char *s) {
	int size;
	bool lastWasChar = false;
	for (size = 0; s[size];) {
		if (s[size] == ' ') {
			lastWasChar ? size++ : *s++;
			lastWasChar = false;
		} else {
			lastWasChar = true;
			s++;
		}
	}
	if (!lastWasChar) return size;
	return size + 1;
}

void clean(int argc, char **argv) {
	int x;
	for (x = 0; x < argc + 1; x++)
		free(argv[x]);
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
	int size = countTokens(s);
	if(size == 0) return -1;
	argv[0] = malloc((size + 1) * sizeof(char **));
	if (!argv[0]) return -1;

	/* get the first token */
	token = strtok_r(save, &t, &save);

	/* walk through other tokens */
	while (token != NULL) {
		argv[0][i] = malloc((strlen(token) + 1) * sizeof(char));
		strcpy(argv[0][i], token);
		token = strtok_r(NULL, &t, &save);
		i++;
	}
	argv[0][i] = NULL;
	return i;

}// end makeArgs