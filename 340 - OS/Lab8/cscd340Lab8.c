#include "./pipes/pipes.h"
#include "./utils/myUtils.h"
#include "./process/process.h"
#include "./tokenize/makeArgs.h"

int main() {
	int argc, pipeCount;
	char **argv = NULL, s[MAX];

	printf("command?: ");
	fgets(s, MAX, stdin);
	strip(s);

	while (strcmp(s, "exit") != 0) {
		pipeCount = countTokens(s, '|');
		if(pipeCount == 0){
			argc = makeargs(s, &argv, ' ');
			if (argc != -1)
				forkIt(argv);
		}else{
			argc = makeargs(s, &argv, '|');
			if(argc != -1){
				pipeIt(argc, argv);
			}
		}
		clean(argc, argv);
		argv= NULL;

		printf("command?: ");
		fgets(s, MAX, stdin);
		strip(s);

	}// end while

	return 0;

}// end main