#include <regex.h>
#include "redirect.h"
#include "../process/process.h"
#include "../utils/utils.h"

bool checkRedirects(char *s, int *inCount, int *outCount) {
	*inCount = 0;
	*outCount = 0;
	for (int i = 0; i < strlen(s); i++) {
		if (s[i] == '<') *inCount = *inCount + 1;
		else if (s[i] == '>') *outCount = *outCount + 1;
	}
	if (inCount > 0 || outCount > 0) return true;
}

char *getRedirect(char *s, char start, char stop) {
	int i = 0;
	while (s[i++] != start);
	int startIndex = i;
	while (s[i++] == ' ');
	while (s[i] != ' ' && s[i] != stop && s[i] != 0) i++;
	int endIndex = i;
	int size = endIndex - startIndex;
	char *redirect = calloc((size + 1), sizeof(char));
	strncpy(redirect, s + startIndex, (size_t) size);
	if (startIndex > 0) startIndex--;
	for (i = startIndex; i < endIndex; i++) {
		s[i] = ' ';
	}
	return redirect;
}

void redirectIt(char *PATH, char *s, bool isIn, bool isOut) {
	int status;
	pid_t pid = fork();
	if (pid == child) {
		if (isOut) {
			char * out = getRedirect(s, '>', '<');
			freopen(trimWhitespace(out), "w", stdout);
			free(out);
		}
		if (isIn) {
			char *in = getRedirect(s, '<', '>');
			freopen(trimWhitespace(in), "r", stdin);
			free(in);
		}
		forkIt(PATH, trimWhitespace(s));
		exit(0);
	} else {
		waitpid(pid, &status, 0);
	}
}