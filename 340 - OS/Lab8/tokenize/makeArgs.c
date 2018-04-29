#include "makeArgs.h"
#include "stdbool.h"
#include "../utils/utils.h"

int countTokens(char *s, const char *t) {
	int size;
	bool lastWasChar = false;
	for (size = 0; s[size];) {
		if (s[size] == *t) {
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
}

void printargs(int argc, char **argv) {
	int x;
	for (x = 0; x < argc; x++)
		printf("%s\n", argv[x]);

}

int makeargss(char *s, char ***argv, char *t, int size) {
	char *token;
	char *save = s;
	int i = 0;

	argv[0] = malloc((size + 1) * sizeof(char **));
	if (!argv[0]) return -1;

	/* get the first token */
	token = strtok_r(save, t, &save);

	/* walk through other tokens */
	while (token != NULL) {
		token = trimWhitespace(token);
		argv[0][i] = malloc((strlen(token) + 1) * sizeof(char));
		strcpy(argv[0][i], token);
		token = strtok_r(NULL, t, &save);
		i++;
	}
	argv[0][i] = (char *) NULL;
	return i;
}

int makeargs(char *s, char ***argv, char *t) {
	int size = countTokens(s, t);
	if (size == 0) return -1;
	return makeargss(s, argv, t, size);
}