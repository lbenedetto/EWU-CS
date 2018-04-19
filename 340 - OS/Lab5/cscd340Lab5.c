#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "makeArgs.h"
#include "./utils/myUtils.h"

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
	  {
		printf("There are %d tokens.\nThe tokens are:\n", argc);
		printargs(argc, argv);

	  }// end if

	  clean(argc, argv);
	  argv = NULL;

	  printf("command?: ");
	  fgets(s, MAX, stdin);
      	  strip(s);

  }// end while

  return 0;

}// end main







