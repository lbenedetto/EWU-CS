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

void fileToCommandToFile(char *PATH, char *s) {

}

void fileToCommand(char *PATH, char *s) {

}

void commandToFile(char *PATH, char *s) {
	char **parts;
	int argc = makeargs(s, &parts, ">");
	if (argc == 2) {
		int status;
		int filefd = open(parts[1], O_WRONLY | O_CREAT, 0666);
		pid_t pid = fork();
		if (pid == child) {
			close(std_out);
			dup2(filefd, std_out);
			forkIt(PATH, parts[0]);
			close(filefd);
			exit(0);
		} else {
			close(filefd);
			waitpid(pid, &status, 0);
		}
	} else {
		fprintf(stderr, "Incorrect args in commandToFile");
	}
}
