lab6:	cscd340Lab6.c ./tokenize/makeArgs.o ./utils/myUtils.o ./process/process.o
	gcc -g cscd340Lab6.c ./tokenize/makeArgs.o ./utils/myUtils.o ./process/process.o -o lab6

makeArgs.o:	./tokenize/makeArgs.c ./tokenize/makeArgs.h
	gcc -g -c ./tokenize/makeArgs.c

myUtils.o:	./utils/myUtils.o ./utils/myUtils.h
	gcc -g -c ./utils/myUtils.c

process.o:	./process/process.c ./process/process.h
	gcc -g -c ./process/process.c	

clean:
	rm ./utils/myUtils.o
	rm ./tokenize/makeArgs.o
	rm ./process/process.o
	rm lab6

