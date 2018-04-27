#include "./pipes/pipes.h"
#include "./utils/myUtils.h"
#include "./process/process.h"
#include "./tokenize/makeArgs.h"

int main() {
	int argc = 0, commandCount;
	char **argv = NULL, s[MAX];

	printf("command?: ");
	fgets(s, MAX, stdin);
	strip(s);

	while (strcmp(s, "exit") != 0) {
		commandCount = countTokens(s, "|");
		if (commandCount > 1) {
			argc = makeargss(s, &argv, "|", commandCount);
			if (argc != -1) {
				pipeIt(argc, argv);
			}
		}else if (commandCount > 0) {
			argc = makeargs(s, &argv, " ");
			if (argc != -1)
				forkIt(argv);
		}
		if (commandCount > 0) {
			clean(argc, argv);
			argv = NULL;
		}

		printf("command?: ");
		fgets(s, MAX, stdin);
		strip(s);

	}// end while

	return 0;

}// end main