cmake_minimum_required(VERSION 3.7)
project(HW_5)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES main.cpp Shapes/Shape.h Shapes/Shape2D.h Shapes/Shape3D.h Shapes/2DShapes/Triangle.cpp Shapes/2DShapes/Triangle.h Shapes/2DShapes/Rectangle.cpp Shapes/2DShapes/Rectangle.h Shapes/2DShapes/Circle.cpp Shapes/2DShapes/Circle.h Shapes/3DShapes/Sphere.cpp Shapes/3DShapes/Sphere.h Shapes/3DShapes/Box.cpp Shapes/3DShapes/Box.h Shapes/3DShapes/Cylinder.cpp Shapes/3DShapes/Cylinder.h)
add_executable(HW_5 ${SOURCE_FILES})