rm(list=ls())
gc()

library(lattice)

mydata = read.csv("TheSouthSucks.csv")
attach(mydata)
partialData <- scale(mydata[,2:7]) #standardize variables

# K-Means Cluster Analysis
fit <- kmeans(partialData, 4) # Find 4 clusters
# get cluster means 
aggregate(partialData,by=list(fit$cluster),FUN=mean)
# append cluster assignment
partialData <- data.frame(partialData, fit$cluster)

#The seventh column of the table contains cluster information, use it to determine the colors
plot <- parallelplot(partialData[,1:6], col=partialData[,7], horizontal.axis=FALSE)
print(plot)