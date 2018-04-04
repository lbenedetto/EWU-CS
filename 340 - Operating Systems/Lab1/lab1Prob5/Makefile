CC = gcc

OBJ = myInt.o genericArray.o myWord.o
EXE = prob5
MAIN = cscd340_s18_lab1prob5.c


prob5:	${OBJ}
	${CC} -g -o ${EXE} ${OBJ} ${MAIN}


genericArray.o:	genericArray.h genericArray.c
	${CC} -g -c genericArray.c


myInt.o:	myInt.h myInt.c
	${CC} -g -c myInt.c

myWord.o:	myWord.c myWord.h
	${CC} -g -c myWord.c


clean:
	rm -rf *.o
	rm -rf prob5

