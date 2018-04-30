mssh:	cscd340Lab8.c ./command/command.o ./linkedlist/listUtils.o ./linkedlist/linkedList.o ./pipes/pipes.o ./process/process.o ./redirects/redirects.o ./tokenize/makeArgs.o ./utils/utils.o
	gcc -g cscd340Lab8.c ./command/command.o ./linkedlist/listUtils.o ./linkedlist/linkedList.o ./pipes/pipes.o ./process/process.o ./redirects/redirects.o ./tokenize/makeArgs.o ./utils/utils.o -o mssh

command.o:	./command/command.c ./command/command.h
	gcc -g -c ./command/command.c

listUtils.o:	./linkedlist/listUtils.c ./linkedlist/listUtils.h
	gcc -g -c ./linkedlist/listUtils.c

linkedList.o:	./linkedlist/linkedList.h ./linkedlist/linkedList.c
	gcc -g -c ./linkedlist/linkedList.c

pipes.o:	./pipes/pipes.c ./pipes/pipes.h
	gcc -g -c ./pipes/pipes.c

process.o:	./process/process.c ./process/process.h
	gcc -g -c ./process/process.c

redirects.o:	./redirects/redirects.c ./redirects/redirects.h
	gcc -g -c ./redirects/redirects.c

makeArgs.o:	./tokenize/makeArgs.c ./tokenize/makeArgs.h
	gcc -g -c ./tokenize/makeArgs.c

utils.o:	./utils/utils.c ./utils/utils.h
	gcc -g -c ./utils/utils.c

clean:
	rm ./command/command.o
	rm ./linkedlist/listUtils.o
	rm ./linkedlist/linkedList.o
	rm ./pipes/pipes.o
	rm ./process/process.o
	rm ./redirects/redirects.o
	rm ./tokenize/makeArgs.o
	rm ./utils/utils.o
	rm mssh


