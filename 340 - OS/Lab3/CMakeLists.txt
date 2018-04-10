cmake_minimum_required(VERSION 3.9.6)
project(Lab3)

add_executable(Lab3 cscd340Lab3.c ./linkedlist/requiredIncludes.h ./movies/actor.h
        ./utils/fileUtils.c ./utils/fileUtils.h
        ./utils/myUtils.c ./utils/myUtils.h
        ./utils/myFileUtils.c ./utils/myFileUtils.h
        ./myInt/myInt.c ./myInt/myInt.h
        ./movies/movie.c ./movies/movie.h
        ./linkedlist/listUtils.c ./linkedlist/listUtils.h
        ./linkedlist/linkedList.c ./linkedlist/linkedList.h)