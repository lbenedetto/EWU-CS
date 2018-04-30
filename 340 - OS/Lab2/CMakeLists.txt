cmake_minimum_required(VERSION 3.9.6)
project(Lab2)

add_executable(Lab2 cscd340Lab2.c
        ./utils/fileUtils.c ./utils/fileUtils.h
        ./utils/myUtils.c ./utils/myUtils.h
        ./stock/stock.c ./stock/stock.h
        ./genericArray/genericArray.c ./genericArray/genericArray.h)