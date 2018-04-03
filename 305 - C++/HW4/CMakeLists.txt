cmake_minimum_required(VERSION 3.7)
project(HW4)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES graph_tester.cpp Graph.cpp Graph.h Point3D.cpp Point3D.h)
add_executable(HW4 ${SOURCE_FILES})