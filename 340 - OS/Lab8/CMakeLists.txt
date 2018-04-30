cmake_minimum_required(VERSION 3.9.6)
project(Lab8)
configure_file(.msshrc .msshrc COPYONLY)
configure_file(.msshrc_history .msshrc_history COPYONLY)
add_executable(mssh cscd340Lab8.c
		utils/utils.h utils/utils.c
		tokenize/makeArgs.h tokenize/makeArgs.c
		process/process.h process/process.c
		pipes/pipes.h pipes/pipes.c
		linkedlist/linkedList.c linkedlist/listUtils.h
		linkedlist/listUtils.c linkedlist/listUtils.h
		redirects/redirects.c redirects/redirects.h command/command.c command/command.h)