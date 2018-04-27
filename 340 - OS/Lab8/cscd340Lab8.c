#include "./pipes/pipes.h"
#include "./process/process.h"
#include "./tokenize/makeArgs.h"
#include "utils/utils.h"
#include "linkedlist/linkedList.h"
#include "linkedlist/listUtils.h"

#define CONFIG_FILE ".msshrc"
#define HISTORY_FILE ".msshrc_history"

int main() {
	LinkedList *history = linkedList();
	LinkedList *aliases = linkedList();
	int HISTCOUNT = 5;
	int HISTFILECOUNT = 10;
	char *PATH = getenv("PATH");
	if (PATH == NULL) PATH = "";

	//Load config file
	if (doesFileExist(CONFIG_FILE)) {
		FILE *fin = fopen(CONFIG_FILE, "r");
		if (fin != NULL) {
			//Read history settings
			char *line = readLine(fin);
			//10 is the size of "HISTCOUNT="
			HISTCOUNT = atoi(line + 10);
			free(line);

			line = readLine(fin);
			//14 is the size of "HISTFILECOUNT="
			HISTFILECOUNT = atoi(line + 14);
			free(line);

			//Consume blank line
			char *blank = readLine(fin);

			//Read aliases
			line = readLine(fin);
			while (strcmp(line, blank) != 0) {
				addFirst(aliases, buildNode(line));
				line = readLine(fin);
			}
			free(line);
			free(blank);

			//Read path
			line = readLine(fin);
			//10 is the size of "PATH=$PATH"
			PATH = concat(PATH, line + 10);
			printf("PATH: %s\n", PATH);
			free(line);
		} else {
			printf("%s exists, but could not be opened", CONFIG_FILE);
		}
	}


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
		} else if (commandCount > 0) {
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

	//TODO: Save history and aliases
	//Clean up
	clearList(aliases);
	clearList(history);
	free(aliases);
	free(history);
	free(PATH);
	PATH = NULL;
	aliases = NULL;
	history = NULL;
	return 0;

}// end main