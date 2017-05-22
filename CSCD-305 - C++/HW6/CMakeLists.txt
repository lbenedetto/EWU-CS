cmake_minimum_required(VERSION 3.7)
project(HW6)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES main.cpp InfoNode.cpp InfoNode.h InfoRepository.cpp InfoRepository.h)
add_executable(HW6 ${SOURCE_FILES})