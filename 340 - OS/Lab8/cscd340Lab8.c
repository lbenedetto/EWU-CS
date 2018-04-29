#include "./pipes/pipes.h"
#include "./process/process.h"
#include "utils/utils.h"
#include "linkedlist/linkedList.h"
#include "linkedlist/listUtils.h"
#include "redirects/redirects.h"
#include "stdbool.h"
#define CONFIG_FILE ".msshrc"
#define HISTORY_FILE ".msshrc_history"
LinkedList *aliases;
LinkedList *history;
LinkedList *newHistory;
int HISTCOUNT = 100;
int HISTFILECOUNT = 1000;
char *PATH;
char *myPATH;

#define cmd_history 0
#define cmd_bangbang 1
#define cmd_bangN 2
#define cmd_alias 3
#define cmd_unalias 4
#define cmd_cd 5
#define cmd_histcount 6
#define cmd_histfilecount 7
#define cmd_path 8

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
	else return -1;
}

void handleCommand(char s[]) {
	//Test if command is alias
	Node *curr = aliases->head;
	size_t len = strlen(s);
	for (int i = 0; i < aliases->size; i++) {
		curr = curr->next;
		if (strncmp(s, curr->data, len) == 0 && curr->data[len] == '=') {
			strcpy(s, curr->data + len + 2);
			len = strlen(s);
			s[len - 1] = '\0';
			handleCommand(s);
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
			printList("", history, stdout);
			break;
		}
		case cmd_bangbang: {
			strcpy(s, getLast(newHistory));
			handleCommand(s);
			break;
		}
		case cmd_bangN: {
			char *parseMe = calloc(strlen(s) + 1, sizeof(char));
			strcpy(parseMe, s);
			int n = atoi(parseMe + 1);
			strcpy(s, getNthFromLast(newHistory, n));
			free(parseMe);
			handleCommand(s);
			break;
		}
		case cmd_alias: {
			char *alias = calloc(strlen(s) - 5, sizeof(char));
			strncpy(alias, s + 6, strlen(s) - 6);
			addFirst(aliases, buildNode(alias, 0));
			break;
		}
		case cmd_unalias: {
			char *alias = calloc(strlen(s) - 7, sizeof(char));
			strncpy(alias, s + 8, strlen(s) - 8);
			removeItem(aliases, buildNode(alias, 0));
			break;
		}
		case cmd_cd: {
			chdir(s + 3);
			break;
		}
		default: {
			int commandCount = countTokens(s, "|");
			if (commandCount > 1) {
				pipeIt(PATH, s, commandCount);
			} else if (commandCount > 0) {
				int inCount;
				int outCount;
				bool hasRedirects = checkRedirects(s, &inCount, &outCount);
				if(hasRedirects){
					if(inCount > 1 || outCount > 1) fprintf(stderr, "Unsupported number of redirects");
					redirectIt(PATH, s, inCount == 1, outCount == 1);
//					if(inCount == 1 && outCount == 1){
//						fileToCommandToFile(PATH, s);
//					}else if(inCount == 1){
//						fileToCommand(PATH, s);
//					}else if(outCount == 1){
//						commandToFile(PATH, s);
//					}
				}else{
					forkIt(PATH, s);
				}
			}
			break;
		}
	}
}

void execFile(char *filename) {
	if (doesFileExist(filename)) {
		FILE *fin = fopen(filename, "r");
		if (fin != NULL && !isFileEmpty(fin)) {
			char *line = readLine(fin);
			while (line != NULL) {
				handleCommand(line);
				free(line);
				line = readLine(fin);
			}
		} else {
			fprintf(stderr, "%s exists, but could not be opened or was empty", filename);
		}
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
		} else {
			fprintf(stderr, "%s exists, but could not be opened", HISTORY_FILE);
		}
	}
}

void saveConfig() {
	FILE *fp = fopen(CONFIG_FILE, "w");
	fprintf(fp, "HISTCOUNT=%d\n", HISTCOUNT);
	fprintf(fp, "HISTFILECOUNT=%d\n\n", HISTFILECOUNT);
	printList("alias ", aliases, fp);
	fprintf(fp, "\nPATH=$PATH%s", myPATH);
}

void saveHistory() {
	while (history->size + newHistory->size > HISTFILECOUNT) {
		removeFirst(history);
	}
	FILE *fp = fopen(HISTORY_FILE, "a");
	printList("", newHistory, fp);
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
}

void prompt(char *s) {
	printf("command?: ");
	fgets(s, MAX, stdin);
	strip(s);
}

void init() {
	history = linkedList();
	newHistory = linkedList();
	aliases = linkedList();
	PATH = getenv("PATH");
	myPATH = "";
	if (PATH == NULL) PATH = "";
}

int main() {
	init();
	execFile(CONFIG_FILE);
	loadHistory();

	char s[MAX];
	prompt(s);

	while (strcmp(s, "exit") != 0) {
		handleCommand(s);
		addLast(newHistory, buildNode(s, true));
		prompt(s);

	}

	saveHistory();
	saveConfig();
	cleanUp();
	return 0;

}// end main