#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "./utils/myUtils.h"
#include "./process/process.h"
#include "./tokenize/makeArgs.h"

int main()
{
  char **argv = NULL, s[MAX];
  int argc;

  printf("command?: ");
  fgets(s, MAX, stdin);
  strip(s);

  while(strcmp(s, "exit") != 0)
  {
	  argc = makeargs(s, &argv);
	  if(argc != -1)
	     forkIt(argv);
	  
	  clean(argc, argv);
	  argv = NULL;

	  printf("command?: ");
	  fgets(s, MAX, stdin);
     strip(s);

  }// end while
  
  return 0;

}// end main







