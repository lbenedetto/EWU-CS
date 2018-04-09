cmake_minimum_required(VERSION 3.9.6)
project(Lab3)

add_executable(Lab3 cscd340Lab3.c                 ./utils/fileUtils.c ./utils/myUtils.c ./myInt/myInt.c ./movies/movie.c ./linkedlist/listUtils.c ./linkedlist/linkedList.c
 ./linkedlist/requiredIncludes.h ./movies/actor.h ./utils/fileUtils.h ./utils/myUtils.h ./myInt/myInt.h ./movies/movie.h ./linkedlist/listUtils.h ./linkedlist/linkedList.h)