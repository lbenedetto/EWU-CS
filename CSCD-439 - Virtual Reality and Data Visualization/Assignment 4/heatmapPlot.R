library(mapproj)
library(maps)
library(ggplot2)
states_map <- map_data("state")

#Read and prepare data
crimeData = read.csv("TheSouthSucks.csv")
attach(crimeData)
crimeData$State <- tolower(crimeData$State)
row.names(crimeData)<-crimeData$State
crimeData_map <- merge(states_map,crimeData,by.x="region", by.y="State")
#Show data in map
plot <- ggplot(crimeData_map, aes(x=long, y=lat, group=group, fill=Murder))
plot <- plot + geom_polygon(colour="black")
plot <- plot + scale_fill_gradient2(low="#3bbc00", mid="#bc6100", high="#bc0000", midpoint=median(crimeData$Murder))
plot <- plot +coord_map("polyconic")
print(plot)
