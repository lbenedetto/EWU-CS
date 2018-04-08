CC = gcc

FLAGS = -c -m32

OBJS = ./utils/fileUtils.o ./stock/stock.o ./utils/myUtils.o ./genericArray/genericArray.o

MAIN = cscd340Lab2.c

EXE = ./lab2


lab2:	${OBJS} ${MAIN}
	${CC} ${OBJS} ${MAIN} -o ${EXE}


stock.o:	./stock/stock.c ./stock/stock.h
	${CC} ${FLAGS} lab2.c


fileUitls.o:	./utils/fileUtils.c ./utils/fileUtils.h
	${CC} ${FLAGS} fileUtil.c


myUtils.o:	./utils/myUtils.c ./utils/myUtils.h
	${CC} ${FLAGS} myUtil.c


genericArray.o:	./genericArray/genericArray.c ./genericArray/genericArray.f
	${CC} ${FLAGS} ./genericArray/genericArray.c

clean:
	rm -rf ./stock/*.o
	rm -rf ./utils/*.o
	rm -rf ./genericArray/*.o
	rm -rf lab2
