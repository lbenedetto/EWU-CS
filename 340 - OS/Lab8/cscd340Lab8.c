#include "./pipes/pipes.h"
#include "./process/process.h"
#include "utils/utils.h"
#include "linkedlist/linkedList.h"
#include "linkedlist/listUtils.h"
#include "redirects/redirects.h"
#include "command/command.h"

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
	prompt(s);

	while (strcmp(s, "exit") != 0) {
		handleCommand(s, false);
		prompt(s);
	}

	chdir(getStartDir());
	saveHistory();
	saveConfig();
	cleanUp();
	return 0;

}