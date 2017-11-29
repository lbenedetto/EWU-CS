setwd("N:/EWU-Computer-Science/CSCD-439 - Virtual Reality and Data Visualization/Assignment 4")
mydata = read.csv("TheSouthSucks.csv")
mydata

mydata_ordered<-mydata[order(mydata$Male.white, decreasing=TRUE),]
row.names(mydata_ordered)<-mydata_ordered$Age.group