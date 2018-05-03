#include "./pipes/pipes.h"

void prompt(char *s) {
	printf("command?: ");
	fgets(s, MAX, stdin);
	strip(s);
}

int main() {
	init();
	execFile(CONFIG_FILE, true);
	loadHistory();

	char s[MAX];

	if (!isatty(fileno(stdin))) {
		LinkedList *fileContents = linkedList();
		while (fgets(s, MAX, stdin) != NULL) {
			strip(s);
			addLast(fileContents, buildNode(s, true));
		}
		Node *curr = fileContents->head;
		for (int i = 0; i < fileContents->size; i++) {
			curr = curr->next;
			printf("~~Executing Command: %s\n", curr->data);
			strcpy(s, curr->data);
			handleCommand(s, false);
		}
		clearList(fileContents);
		free(fileContents);
	} else {
		prompt(s);
		while (strcmp(s, "exit") != 0) {
			handleCommand(s, false);
			prompt(s);
		}
	}

	chdir(getStartDir());
	saveHistory();
	cleanUp();
	return 0;
}