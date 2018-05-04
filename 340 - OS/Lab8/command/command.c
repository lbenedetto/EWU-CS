#include <stdbool.h>
#include "command.h"

LinkedList *aliases;
LinkedList *history;
int HISTCOUNT = 100;
int HISTFILECOUNT = 1000;
char *PATH;
char *myPATH;
char startDir[1024];
bool pathWasAlloced = false;

int getCommandID(char s[]) {
	if (strcmp(s, "history") == 0) return cmd_history;
	else if (strcmp(s, "!!") == 0) return cmd_bangbang;
	else if (strncmp("!", s, 1) == 0) return cmd_bangN;
	else if (strncmp("alias", s, 5) == 0) return cmd_alias;
	else if (strncmp("unalias ", s, 8) == 0) return cmd_unalias;
	else if (strncmp("cd ", s, 3) == 0) return cmd_cd;
	else if (strncmp("HISTCOUNT=", s, 10) == 0) return cmd_histcount;
	else if (strncmp("HISTFILECOUNT=", s, 14) == 0) return cmd_histfilecount;
	else if (strncmp("PATH=", s, 5) == 0) return cmd_path;
	else if (strncmp("exec ", s, 5) == 0) return cmd_exec;
	else if (strncmp("exit", s, 4) == 0) return cmd_exit;
	else return -1;
}

void handleCommand(char in[], bool isSilent) {
	char s[MAX];
	strcpy(s, in);
	int commandID = getCommandID(s);
	if (!isSilent
	    && commandID != cmd_bangbang
	    && commandID != cmd_bangN
	    && strcmp(s, getLast(history)) != 0)
		addLast(history, buildNode(s, true));
	//Test if command is alias
	Node *curr = aliases->head;
	size_t len = strlen(s);
	for (int i = 0; i < aliases->size; i++) {
		curr = curr->next;
		if (strncmp(s, curr->data, len) == 0 && curr->data[len] == '=') {
			strcpy(s, curr->data + len + 2);
			len = strlen(s);
			s[len - 1] = '\0';
			handleCommand(s, true);
			return;
		}
	}

	switch (commandID) {
		case cmd_histcount: {
			//10 is the size of "HISTCOUNT="
			int n = (int) strtol(s + 10, NULL, 10);
			if (n < 0)
				fprintf(stderr, "Invalid HISTCOUNT");
			HISTCOUNT = n;
			break;
		}
		case cmd_histfilecount: {
			//14 is the size of "HISTFILECOUNT="
			int n = (int) strtol(s + 14, NULL, 10);
			if (n < 0)
				fprintf(stderr, "Invalid HISTFILECOUNT");
			HISTFILECOUNT = n;
			break;
		}
		case cmd_path: {
			if (myPATH[0] != 0)
				free(myPATH);
			if (strncmp("$PATH", s + 5, 5) == 0) {
				myPATH = calloc(strlen(s + 10) + 1, sizeof(char));
				strcpy(myPATH, s + 10);
				PATH = concat(PATH, myPATH);
			} else {
				myPATH = calloc(strlen(s + 5) + 1, sizeof(char));
				strcpy(myPATH, s + 5);
				PATH = myPATH;
			}
			pathWasAlloced = true;
			printf("PATH: %s\n", PATH);
			break;
		}
		case cmd_history: {
			printLastN("\t", history, HISTCOUNT);
			break;
		}
		case cmd_bangbang: {
			strcpy(s, getLast(history));
			handleCommand(s, true);
			break;
		}
		case cmd_bangN: {
			char *parseMe = calloc(strlen(s) + 1, sizeof(char));
			strcpy(parseMe, s);
			int n = (int) strtol(parseMe + 1, NULL, 10);
			strcpy(s, getNth(history, n));
			free(parseMe);
			handleCommand(s, true);
			break;
		}
		case cmd_alias: {
			if (strlen(s) == 5) {
				printList(aliases);
			} else {
				curr = aliases->head;
				bool isAlreadyAlias = false;
				int stop = 6;
				while (s[stop++] != '=');
				for (int i = 0; i < aliases->size; i++) {
					curr = curr->next;
					if (strncmp(s + 6, curr->data, (size_t) (stop - 6)) == 0) {
						isAlreadyAlias = true;
						free(curr->data);
						curr->data = calloc(strlen(s) - 5, sizeof(char));
						strcpy(curr->data, s + 6);
						break;
					}
				}
				if (!isAlreadyAlias)
					addFirst(aliases, buildNode(s + 6, 1));
			}
			break;
		}
		case cmd_unalias: {
			curr = aliases->head;
			len = strlen(s) - 8;
			for (int i = 0; i < aliases->size; i++) {
				curr = curr->next;
				if (strncmp(s + 8, curr->data, len) == 0) {
					deleteNode(curr->prev, curr, curr->next);
					aliases->size--;
					return;
				}
			}
		}
		case cmd_cd: {
			if (strncmp(s + 3, "~", 1) == 0) {
				char *home = getenv("HOME");
				char *new = concat(home, s + 4);
				chdir(new);
				free(new);
			} else {
				chdir(s + 3);
			}
			break;
		}
		case cmd_exec: {
			execFile(s + 5, false);
			break;
		}
		case cmd_exit: {
			chdir(getStartDir());
			saveHistory();
			cleanUp();
			exit(0);
		}
		default: {
			int commandCount = countTokens(s, "|");
			if (commandCount > 1) {
				pipeIt(s, commandCount);
			} else if (commandCount > 0) {
				int inCount;
				int outCount;
				bool hasRedirects = checkRedirects(s, &inCount, &outCount);
				if (hasRedirects) {
					if (inCount > 1 || outCount > 1) fprintf(stderr, "Unsupported number of redirects\n");
					redirectIt(s, inCount == 1, outCount == 1);
				} else {
					forkIt(PATH, s);
				}
			}
			break;
		}
	}
}


void execFile(char *filename, bool isSilent) {
	if (doesFileExist(filename)) {
		FILE *fin = fopen(filename, "r");
		if (fin != NULL) {
			if (!isFileEmpty(fin)) {
				LinkedList *fileContents = linkedList();
				char *line = readLine(fin);
				while (line != NULL) {
					addLast(fileContents, buildNode(line, false));
					line = readLine(fin);
				}
				free(line);
				Node *curr = fileContents->head;
				for (int i = 0; i < fileContents->size; i++) {
					curr = curr->next;
					line = curr->data;
					if (!isSilent) printf("~~Executing Command: %s\n", line);
					handleCommand(line, isSilent);
				}
				clearList(fileContents);
				free(fileContents);
			}
			fclose(fin);
		} else {
			fprintf(stderr, "%s exists, but could not be opened or was empty\n", filename);
		}
	} else {
		fprintf(stderr, "%s does not exist\n", filename);
	}
}

void loadHistory() {
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
			fclose(fin);
		} else {
			fprintf(stderr, "%s exists, but could not be opened\n", HISTORY_FILE);
		}
	}
}

void saveHistory() {
	while (history->size > HISTFILECOUNT) {
		removeFirst(history);
	}
	FILE *fp = fopen(HISTORY_FILE, "w");
	if (fp != NULL) {
		fprintList("", history, fp);
		fclose(fp);
	} else {
		fprintf(stderr, "Could not save history\n");
	}
}

void cleanUp() {
	clearList(aliases);
	clearList(history);
	free(aliases);
	free(history);
	if (pathWasAlloced) {
		free(PATH);
		free(myPATH);
	}
	PATH = NULL;
	aliases = NULL;
	history = NULL;
	myPATH = NULL;
}


void init() {
	history = linkedList();
	aliases = linkedList();
	PATH = getenv("PATH");
	myPATH = "";
	if (PATH == NULL) PATH = "";
	getcwd(startDir, sizeof(startDir));
}

char *getStartDir() {
	return startDir;
}