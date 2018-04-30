#include "command.h"

LinkedList *aliases;
LinkedList *history;
LinkedList *newHistory;
int HISTCOUNT = 100;
int HISTFILECOUNT = 1000;
char *PATH;
char *myPATH;
char startDir[1024];

int getCommandID(char s[]) {
	if (strcmp(s, "history") == 0) return cmd_history;
	else if (strcmp(s, "!!") == 0) return cmd_bangbang;
	else if (strncmp("!", s, 1) == 0) return cmd_bangN;
	else if (strncmp("alias ", s, 6) == 0) return cmd_alias;
	else if (strncmp("unalias ", s, 8) == 0) return cmd_unalias;
	else if (strncmp("cd ", s, 3) == 0) return cmd_cd;
	else if (strncmp("HISTCOUNT=", s, 10) == 0) return cmd_histcount;
	else if (strncmp("HISTFILECOUNT=", s, 14) == 0) return cmd_histfilecount;
	else if (strncmp("PATH=", s, 5) == 0) return cmd_path;
	else if (strncmp("exec ", s, 5) == 0) return cmd_exec;
	else return -1;
}

void handleCommand(char s[], bool isSilent) {
	//Test if command is alias
	Node *curr = aliases->head;
	size_t len = strlen(s);
	for (int i = 0; i < aliases->size; i++) {
		curr = curr->next;
		if (strncmp(s, curr->data, len) == 0 && curr->data[len] == '=') {
			strcpy(s, curr->data + len + 2);
			len = strlen(s);
			s[len - 1] = '\0';
			handleCommand(s, isSilent);
			return;
		}
	}

	switch (getCommandID(s)) {
		case cmd_histcount: {
			//10 is the size of "HISTCOUNT="
			HISTCOUNT = atoi(s + 10);
			break;
		}
		case cmd_histfilecount: {
			//14 is the size of "HISTFILECOUNT="
			HISTFILECOUNT = atoi(s + 14);
			break;
		}
		case cmd_path: {
			if (strncmp("$PATH", s + 5, 5) == 0) {
				myPATH = calloc(strlen(s + 10) + 1, sizeof(char));
				strcpy(myPATH, s + 10);
				PATH = concat(PATH, myPATH);
			} else {
				myPATH = calloc(strlen(s + 5) + 1, sizeof(char));
				strcpy(myPATH, s + 5);
				PATH = myPATH;
			}
			printf("PATH: %s\n", PATH);
			break;
		}
		case cmd_history: {
			printList("\t", history, stdout);
			break;
		}
		case cmd_bangbang: {
			strcpy(s, getLast(newHistory));
			handleCommand(s, isSilent);
			break;
		}
		case cmd_bangN: {
			char *parseMe = calloc(strlen(s) + 1, sizeof(char));
			strcpy(parseMe, s);
			int n = atoi(parseMe + 1);
			strcpy(s, getNthFromLast(newHistory, n));
			free(parseMe);
			handleCommand(s, isSilent);
			break;
		}
		case cmd_alias: {
			char *alias = calloc(strlen(s) - 5, sizeof(char));
			strncpy(alias, s + 6, strlen(s) - 6);
			addFirst(aliases, buildNode(alias, 0));
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
			chdir(s + 3);
			break;
		}
		case cmd_exec: {
			execFile(s + 5, false);
			break;
		}
		default: {
			int commandCount = countTokens(s, "|");
			if (commandCount > 1) {
				pipeIt(s, commandCount, isSilent);
			} else if (commandCount > 0) {
				int inCount;
				int outCount;
				bool hasRedirects = checkRedirects(s, &inCount, &outCount);
				if (hasRedirects) {
					if (inCount > 1 || outCount > 1) fprintf(stderr, "Unsupported number of redirects\n");
					redirectIt(s, inCount == 1, outCount == 1, isSilent);
				} else {
					forkIt(PATH, s);
				}
			}
			break;
		}
	}
	if (!isSilent) addLast(newHistory, buildNode(s, true));
}


void execFile(char *filename, bool silent) {
	if (doesFileExist(filename)) {
		FILE *fin = fopen(filename, "r");
		if (fin != NULL) {
			if (!isFileEmpty(fin)) {
				char *line = readLine(fin);
				while (line != NULL) {
					if (!silent) printf("~~Executing Command: %s\n", line);
					handleCommand(line, silent);
					free(line);
					line = readLine(fin);
				}
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

void saveConfig() {
	FILE *fp = fopen(CONFIG_FILE, "w");
	if (fp != NULL) {
		fprintf(fp, "HISTCOUNT=%d\n", HISTCOUNT);
		fprintf(fp, "HISTFILECOUNT=%d\n\n", HISTFILECOUNT);
		printList("alias ", aliases, fp);
		fprintf(fp, "\nPATH=$PATH%s", myPATH);
		fclose(fp);
	} else {
		fprintf(stderr, "Could not save config\n");
	}

}

void saveHistory() {
	while (history->size + newHistory->size > HISTFILECOUNT) {
		removeFirst(history);
	}
	FILE *fp = fopen(HISTORY_FILE, "w");
	if (fp != NULL) {
		printList("", history, fp);
		printList("", newHistory, fp);
		fclose(fp);
	} else {
		fprintf(stderr, "Could not save history\n");
	}
}

void cleanUp() {
	clearList(aliases);
	clearList(history);
	clearList(newHistory);
	free(aliases);
	free(history);
	free(newHistory);
	free(PATH);
	free(myPATH);
	PATH = NULL;
	aliases = NULL;
	history = NULL;
	newHistory = NULL;
	myPATH = NULL;
}


void init() {
	history = linkedList();
	newHistory = linkedList();
	aliases = linkedList();
	PATH = getenv("PATH");
	myPATH = "";
	if (PATH == NULL) PATH = "";
	getcwd(startDir, sizeof(startDir));
}

char *getStartDir() {
	return startDir;
}