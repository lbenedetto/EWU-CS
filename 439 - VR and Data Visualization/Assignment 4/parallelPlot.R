library(lattice)

crimeData = read.csv("TheSouthSucks.csv")
attach(crimeData)
row.names(crimeData)<-crimeData$State # move state names into row names
crimeData = crimeData[,2:7] # remove state name column
crimeData <- scale(crimeData) #standardize variables

# K-Means Cluster Analysis
fit <- kmeans(crimeData, 4) # Find 4 clusters
# get cluster means 
aggregate(crimeData,by=list(fit$cluster),FUN=mean)
# append cluster assignment
crimeData <- data.frame(crimeData, fit$cluster)

#The seventh column of the table contains cluster information, use it to determine the colors
plot <- parallelplot(crimeData[,1:6], col=crimeData[,7], horizontal.axis=FALSE)
print(plot)