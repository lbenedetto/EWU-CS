#include "redirect.h"
#include "../tokenize/makeArgs.h"
#include "../process/process.h"

bool checkRedirects(char *s, int *inCount, int *outCount) {
	*inCount = 0;
	*outCount = 0;
	for (int i = 0; i < strlen(s); i++) {
		if (s[i] == '<') *inCount = *inCount + 1;
		else if (s[i] == '>') *outCount = *outCount + 1;
	}
	if (inCount > 0 || outCount > 0) return true;
}

void genericRedirect(char *PATH, char *s, char *separator, char *fileType, FILE *replacementTarget) {
	char **parts;
	int argc = makeargs(s, &parts, separator);
	if (argc == 2) {
		int status;
		pid_t pid = fork();
		if (pid == child) {
			freopen(parts[1], fileType, replacementTarget);
			forkIt(PATH, parts[0]);
			exit(0);
		} else {
			waitpid(pid, &status, 0);
		}
	} else {
		fprintf(stderr, "Incorrect args in commandToFile");
	}
}

void fileToCommandToFile(char *PATH, char *s) {

}

void fileToCommand(char *PATH, char *s) {
	genericRedirect(PATH, s, "<", "r", stdin);
}

void commandToFile(char *PATH, char *s) {
	genericRedirect(PATH, s, ">", "w", stdout);
}