cmake_minimum_required(VERSION 3.7)
project(HW2)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES intersection_tester.cpp Point3D.cpp Point3D.h Ray.cpp Ray.h Sphere.cpp Sphere.h)
add_executable(HW2 ${SOURCE_FILES})