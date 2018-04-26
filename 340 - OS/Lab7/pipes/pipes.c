#include "pipes.h"
#include "../tokenize/makeArgs.h"

int containsPipe(char *s) {
	int count = 0;
	for (int i = 0; i < strlen(s); i++) {
		if (s[i] == '|') count++;
	}
	printf("containsPipe: %d\n", count);
	return count > 0;
}

char **parsePrePipe(char *s, int *preCount) {
	char **argv = NULL;
	char *save = calloc(strlen(s) + 1, sizeof(char));
	strcpy(save, s);

	char *token = strtok_r(save, "|", &save);
	strip(token);
	*preCount = makeargs(token, &argv);

	printf("prePipe:\n");
	for(int i = 0; i < *preCount; i++){
		printf("\t%d:%s\n",i, argv[i]);
	}

	return argv;
}

char **parsePostPipe(char *s, int *postCount) {
	char **argv = NULL;
	char *save = calloc(strlen(s) + 1, sizeof(char));
	strcpy(save, s);

	strtok_r(save, "|", &save);
	char *token = strtok_r(save, "|", &save);
	strip(token);
	*postCount = makeargs(token, &argv);

	printf("postPipe:\n");
	for(int i = 0; i < *postCount; i++){
		printf("\t%d:%s\n",i, argv[i]);
	}

	return argv;
}

void pipeIt(char **prePipe, char **postPipe) {
	pid_t pid;
	int fd[2], res, status;

	res = pipe(fd);

	if (res < 0) {
		printf("Pipe Failure\n");
		exit(-1);
	}// end if

	pid = fork();

	if (pid != 0) {
		close(fd[1]);
		close(0);
		dup(fd[0]);
		close(fd[0]);
		execvp(postPipe[0], postPipe);
	}// end if AKA parent
	else {
		close(fd[0]);
		close(1);
		dup(fd[1]);
		close(fd[1]);
		execvp(prePipe[0], prePipe);
	}// end else AKA child
}