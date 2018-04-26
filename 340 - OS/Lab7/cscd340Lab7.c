#include "./pipes/pipes.h"
#include "./utils/myUtils.h"
#include "./process/process.h"
#include "./tokenize/makeArgs.h"

int main()
{
  int argc, pipeCount;	
  char **argv = NULL, s[MAX];
  int preCount = 0, postCount = 0;
  char ** prePipe = NULL, ** postPipe = NULL;
  
  printf("command?: ");
  fgets(s, MAX, stdin);
  strip(s);

  while(strcmp(s, "exit") != 0)
  {
	pipeCount = containsPipe(s);
	if(pipeCount > 0)
	{
		prePipe = parsePrePipe(s, &preCount);
		postPipe = parsePostPipe(s, &postCount);
		pipeIt(prePipe, postPipe);
		clean(preCount, prePipe);
        	clean(postCount, postPipe);
	}// end if pipeCount	  

	else
	{
		argc = makeargs(s, &argv);
	  	if(argc != -1)
	  		forkIt(argv);
	  
	  	clean(argc, argv);
	  	argv = NULL;
	}
	
	printf("command?: ");
	fgets(s, MAX, stdin);
      	strip(s);

  }// end while

  return 0;

}// end main

