cmake_minimum_required(VERSION 3.5)
project(Homework_3_4)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    main.c
    pgmUtility.c
    pgmUtility.h)

add_executable(Homework_3_4 ${SOURCE_FILES})