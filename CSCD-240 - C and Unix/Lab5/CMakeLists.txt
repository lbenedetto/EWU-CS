cmake_minimum_required(VERSION 3.5)
project(Lab5)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Werror")

set(SOURCE_FILES main.c)
add_executable(Lab5 ${SOURCE_FILES})