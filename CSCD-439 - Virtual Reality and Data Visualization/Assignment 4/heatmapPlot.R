rm(list=ls())
gc()

library(RColorBrewer)

mydata = read.csv("TheSouthSucks.csv")
attach(mydata)

mycolors = brewer.pal(5,'Reds')
thecolors = mycolors[mapgroups('state',thestates,hgroups)]
map('state',col=thecolors,fill=TRUE)
