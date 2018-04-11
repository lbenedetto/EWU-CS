CC=gcc
FLAGS= -g -c -m32

EXE = lab3
MAIN = ./cscd340Lab3.c

INTS = ./myInt/myInt.o
MOVIES = ./movies/movie.o

UTILS= ./utils/myUtils.o ./utils/fileUtils.o ./utils/myFileUtils.o
LINKEDLIST = ./linkedlist/linkedList.o ./linkedlist/listUtils.o 

${EXE}:	${UTILS} ${INTS} ${MOVIES} ${LINKEDLIST}  ${MAIN}
	gcc -o ${EXE} ${MAIN} ${INTS} ${MOVIES} ${LINKEDLIST} ${UTILS}

#==============================================================================================================================

listUtils.o:	./linkedlist/listUtils.c listUtils.h
	gcc  ${FLAGS} ./linkedlist/listUtils.c

linkedList.o:	./linkedlist/linkedList.h ./linkedlist/linkedList.c ./linkedlist/requiredIncludes.h
	gcc ${FLAGS} ./linkedlist/linkedList.c

#==============================================================================================================================

myUtils.o:	./utils/myUtils.h ./utils/myUtils.c
	gcc ${FLAGS} ./utils/myUtils.c

fileUtils.o:	./utils/fileUtils.h ./utils/fileUtils.c
	gcc ${FLAGS} ./utils/fileUtil.c

myFileUtils.o: ./utils/myFileUtils.h ./utils/myFileUtils.c
	gcc ${FLAGS} ./utils/myFileUtils.c

#==============================================================================================================================

myInt.o:	./myInt/myInt.h ./myInt/myInt.c
	gcc ${FLAGS} ./myInt/myInt.c

movie.o:	./movies/actor.h ./movies/movie.h ./movies/movie.c
	gcc ${FLAGS} ./movies/movie.c

#==============================================================================================================================

clean:	
	rm myInt/*.o
	rm movies/*.o
	rm linkedlist/*.o
	rm utils/*.o
	rm ${EXE}


