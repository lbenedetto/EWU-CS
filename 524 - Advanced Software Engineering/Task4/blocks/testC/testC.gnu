# component [c.myHull] {
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
# component [c.myTurret] {
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
# component [c.myGun] {
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


# } component [c.myGun]
# } subcomponents
# } component [c.myTurret]
# } subcomponents
# } component [c.myHull]
