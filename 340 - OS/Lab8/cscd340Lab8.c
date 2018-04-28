#include "./pipes/pipes.h"
#include "./process/process.h"
#include "./tokenize/makeArgs.h"
#include "utils/utils.h"
#include "linkedlist/linkedList.h"
#include "linkedlist/listUtils.h"

#define CONFIG_FILE ".msshrc"
#define HISTORY_FILE ".msshrc_history"
#define false 0
#define true 1


int main() {
	LinkedList *history = linkedList();
	LinkedList *newHistory = linkedList();
	LinkedList *aliases = linkedList();
	int HISTCOUNT = 100;
	int HISTFILECOUNT = 1000;
	char *PATH = getenv("PATH");
	if (PATH == NULL) PATH = "";

	//Load config file
	if (doesFileExist(CONFIG_FILE)) {
		FILE *fin = fopen(CONFIG_FILE, "r");
		if (fin != NULL && !isFileEmpty(fin)) {
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
				addFirst(aliases, buildNode(line, false));
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
			printf("%s exists, but could not be opened or was empty", CONFIG_FILE);
		}
	}

	//Load history file
	if (doesFileExist(HISTORY_FILE)) {
		FILE *fin = fopen(HISTORY_FILE, "r");
		if (fin != NULL) {
			if (!isFileEmpty(fin)) {
				//Read entire file into linked list
				char *line = readLine(fin);
				while (line != NULL) {
					addLast(history, buildNode(line, false));
					line = readLine(fin);
				}
				free(line);
				//Remove last until size == HISTFILECOUNT
				//Make sure to keep it this size
				while (history->size > HISTFILECOUNT) {
					removeFirst(history);
				}
			}
		} else {
			printf("%s exists, but could not be opened", HISTORY_FILE);
		}
	}


	int argc = 0, commandCount;
	char **argv = NULL, s[MAX];

	printf("command?: ");
	fgets(s, MAX, stdin);
	strip(s);

	while (strcmp(s, "exit") != 0) {
		if (strcmp(s, "history") == 0) {
			printList(history, stdout);
		} else if (strcmp(s, "!!") == 0) {
			strcpy(s, getLast(newHistory));
			continue;
		} else if (strncmp("!", s, 1) == 0) {
			char *parseMe = calloc(strlen(s) + 1, sizeof(char));
			strcpy(parseMe, s);
			int n = atoi(parseMe + 1);
			strcpy(s, getNthFromLast(newHistory, n));
			free(parseMe);
			continue;
		} else if (strncmp("alias ", s, 6) == 0) {

		} else if (strncmp("unalias ", s, 8) == 0) {

		} else if (strncmp("cd ", s, 3) == 0) {

		} else {
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
		}
		addLast(newHistory, buildNode(s, true));
		printf("command?: ");
		fgets(s, MAX, stdin);
		strip(s);

	}// end while

	//Save history
	while (history->size + newHistory->size > HISTFILECOUNT) {
		removeFirst(history);
	}
	FILE *fp = fopen(HISTORY_FILE, "a");
	printList(newHistory, fp);
	//TODO: Save aliases

	//Clean up
	clearList(aliases);
	clearList(history);
	clearList(newHistory);
	free(aliases);
	free(history);
	free(newHistory);
	free(PATH);
	PATH = NULL;
	aliases = NULL;
	history = NULL;
	newHistory = NULL;
	return 0;

}// end main