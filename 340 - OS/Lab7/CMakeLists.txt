cmake_minimum_required(VERSION 3.9.6)
project(Lab7)
add_executable(Lab7a cscd340Lab7a.c)
add_executable(Lab7 cscd340Lab7.c
		utils/myUtils.h utils/myUtils.c
		tokenize/makeArgs.h tokenize/makeArgs.c
		process/process.h process/process.c
		pipes/pipes.h pipes/pipes.c)