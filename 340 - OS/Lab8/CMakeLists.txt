cmake_minimum_required(VERSION 3.9.6)
project(Lab8)
add_executable(Lab8 cscd340Lab8.c
		utils/myUtils.h utils/myUtils.c
		tokenize/makeArgs.h tokenize/makeArgs.c
		process/process.h process/process.c
		pipes/pipes.h pipes/pipes.c)