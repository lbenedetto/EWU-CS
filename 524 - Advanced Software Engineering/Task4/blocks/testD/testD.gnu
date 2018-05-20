# component [d.myHull] {
# top
-3.0 -2.0 3.0
3.0 -2.0 3.0
3.0 2.0 3.0
-3.0 2.0 3.0
-3.0 -2.0 3.0

# bottom
-3.0 -2.0 0.0
3.0 -2.0 0.0
3.0 2.0 0.0
-3.0 2.0 0.0
-3.0 -2.0 0.0


# subcomponents {
# component [d.myTurret] {
# top
-2.0 -1.0 4.0
0.0 -1.0 4.0
0.0 1.0 4.0
-2.0 1.0 4.0
-2.0 -1.0 4.0

# bottom
-2.0 -1.0 3.0
0.0 -1.0 3.0
0.0 1.0 3.0
-2.0 1.0 3.0
-2.0 -1.0 3.0


# subcomponents {
# component [d.myGun] {
# top
0.0 -0.25 3.75
5.0 -0.25 3.75
5.0 0.25 3.75
0.0 0.25 3.75
0.0 -0.25 3.75

# bottom
0.0 -0.25 3.25
5.0 -0.25 3.25
5.0 0.25 3.25
0.0 0.25 3.25
0.0 -0.25 3.25


# } component [d.myGun]
# component [d.mySensor] {
# top
-1.75 0.25 4.5
-1.25 0.25 4.5
-1.25 0.75 4.5
-1.75 0.75 4.5
-1.75 0.25 4.5

# bottom
-1.75 0.25 4.0
-1.25 0.25 4.0
-1.25 0.75 4.0
-1.75 0.75 4.0
-1.75 0.25 4.0


# } component [d.mySensor]
# } subcomponents
# } component [d.myTurret]
# } subcomponents
# } component [d.myHull]
