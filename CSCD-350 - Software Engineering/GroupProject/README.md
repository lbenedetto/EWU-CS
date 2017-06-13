# Team 11 - The Project

## Part 1 : Utility Application
The first part of the project serves two basic but valuable purposes. First, it is a utility for users who want to
interact later with the larger project to come. It is very common for different stakeholders and resources to use
different definitions for their data; e.g., the axes and units in coordinate systems. This leads to endless confusion
and chances to make conversion mistakes. Second, it allows users to plan simple flight paths for spacecraft.
Specifically, it calculates the distances traveled on different axes from a list of coordinates. The implementation as
usual involves nothing more than basic arithmetic, and there are only three functional methods (two virtually
identical). However, managing the complexity to perform the right actions on the right data is nontrivial. There
are many combinations to test, so a good design and test plan are critical.

### Specifications
See the Javadoc for full details. There is only one source file, but it contains an inner class and three
enumerations. As always, implement the public contract as specified. Not all details are explicitly stated. Be sure to
ask for clarification if you are unsure.

### Operational Details
The client instantiates a `WaypointPlanner` by defining how to interpret the axes of the input stream. Each line
consists of a triple containing three comma-delimited real values that specify coordinates in three-dimensional
space according to the user’s coordinate system. A line ends with a newline. Whitespace is ignored.
The columns in the input stream can be any of six permutations of `x`, `y`, and `z`, namely `xyz`, `xzy`, `yxz`, `yzx`, `zxy`,
and `zyx`. This native external format maps onto the canonical internal format defined as axes `A`, `B`, and `C`, where
the origin is `(0,0,0)` and the arrows indicate increasing values.

Each native axis can increase in the same direction as the canonical axis it is mapped to or in the opposite
direction. For example, native z might increase downward on B. There are six permutations of the axes and eight
of the directions for a total of 48 possible input definitions. Similarly, the input unit may be defined as meters,
kilometers, feet, or miles. The canonical unit is always meters. This utility converts them all to the canonical
format for consistency with other (nonexistent) tools in the larger project. It can return the coordinates in either
format and in any unit.
The second feature is to be able to compute the distances traveled from the first to the last coordinates. There are
two variants: the total distance as single number, and a list of intermediate distances between each of the
coordinates (which sums to the total). Flight planners may need to know how much distance is covered on
different combinations of axes; e.g., one-dimensional x distance on a line, two-dimensional xy distance on a
plane, or three-dimensional `xyz` distance within a volume. The unit of distance can be selected.
See the resources on the task link for examples.

### Deliverables
Implement all the Javadoc functionality unless indicated otherwise. Be sure to check for all errors. Submit only the
Java source file. There is only one chance to get this solution correct. Although this is a small programming task,
work on it as a team. You will evaluate each other at the end of the project.

## Part 2 : Command Parser [PRE-RELEASE]
The role of the parser is to read the commands that create, connect, and manipulate entities in the world, as well
as perform miscellaneous administrative actions. Implement `s17cs350project.parser.CommandParser` to
interpret a string of text commands.
* The public constructor is `CommandParser(CommandController controller, String commands)`, where
commands contains the commands.
* Public method `A_Command parse()` executes the parser. It reads each command, extracts its data fields, populates
the corresponding A_Command object (called the binding), and submits it to the CommandController for
execution. Return `null` because you are not required to implement `Command 23`, which would generate the object to return.

Punctuation symbols are not part of commands unless in bold. They have the following meanings:
* `|` exclusive or 
* `a | b` is a not b or b not a
* `^` nonexclusive or 
* `a ^ b` is a not b or b not a or a and b; order matters
* `*` zero or more instances of the preceding termor parenthetical group
* `id*` means zero or more id instances
* `+` one or more instances of the preceding term or parenthetical group
* `id+` means one or more id instances
* `[]` optional group
* `[ id ]` means id is optional

Whitespace, except in literals, does not matter. All text except identifiers is case insensitive. All statements appear
on a separate line. Singular and plural forms need not correspond grammatically to the number of elements
specified. Identifiers must be globally unique. C++ comments (// and /* */) are supported.
Error checking need not be friendly, but do catch any problems on your end that you can. Most range checks are
done on my end. These specifications also serve as instructions for the user. Not everything needs to be
implemented on your end.

The resources are provided in the `JAR` file on the task link. Always use the most recent version.

For development purposes, temporarily create your own stub class called CommandController with public
method void schedule(A_Command). Pass it to the constructor of your CommandParser.
Use only the standard built-in Java language features. Do not use any resources from the `parser` package.

### Field Definitions
Fields are the lowercase elements of each command rule below. (Subscripts are only for clarity.) Read the text data
and store it in the appropriate datatype (built-in or project-specific in `s17cs350project.datatype`) as dictated by
the constructor in the corresponding command in `s17cs350project.command`.

Field | Format
---|---
attitude | a number on the interval (-360,+360)
number | a standard signed integer or real; no *e* notation. + is acceptable on positive numbers
string | a standard string delimited by single quotes; no escapes
id | a standard alphanumerical identifier
percent | a number on the interval [0,100]

### Creational Commands
Creational commands are responsible for making components and thrusters.
1. `CREATE [ ROOT ] COMPONENT id SIZE WIDTH number1 HEIGHT number2 DEPTH number3`
Creates component id with dimensions `number1`, `number2`, and `number3`.
Bind to command `CommandCreateComponent`.

2. `CREATE MAIN THRUSTER id AT OFFSET` **(** `number1 number2 number3` **)** `ON SURFACE ( TOP | BOTTOM | LEFT | RIGHT | FRONT | BACK ) WITH ORIENTATION ( UPWARD | DOWNWARD | LEFTWARD | RIGHTWARD | FORWARD | BACKWARD ) USING FIXED THRUST thrust RATE percent5`
Creates main thruster id offset from its mount by (`number1`, `number2`, `number3`). It is mounted relative to a
surface of the component to connect to later and facing a direction. The thrust is fixed at `thrust`, with a burn
rate of `percent5`.
Bind to command `CommandCreateMainThruster`.

3. `CREATE MAIN THRUSTER id AT OFFSET` **(** `number1 number2 number3` **)** `ON SURFACE ( TOP | BOTTOM | LEFT | RIGHT | FRONT | BACK ) WITH ORIENTATION ( UPWARD | DOWNWARD | LEFTWARD | RIGHTWARD | FORWARD | BACKWARD ) USING VARIABLE THRUST MIN thrust1 MAX thrust2 RATE percent6`
Creates main thruster id offset from its mount by (`number1`, `number2`, `number3`). It is mounted relative to a
surface of the component to connect to later and facing a direction. The thrust is variable between `thrust1` and
`thrust2`, with a burn rate of percent6.
Bind to command `CommandCreateMainThruster`.

4. `CREATE VERNIER THRUSTER id AT OFFSET` **(** `number1 number2 number3` **)** `ON SURFACE ( TOP | BOTTOM | LEFT | RIGHT | FRONT | BACK ) WITH ORIENTATION ( UPWARD | DOWNWARD | LEFTWARD | RIGHTWARD | FORWARD | BACKWARD ) USING THRUST thrust RATE percent5`
Creates vernier thruster id offset from its mount by (`number1`, `number2`, `number3`). It is mounted relative to a
surface of the component to connect to later and facing a direction. The thrust is fixed at `thrust`, with a burn
rate of `percent5`.
Bind to command `CommandCreateVernierThruster`.

### Structural Commands
Structural commands are responsible for connecting thrusters to components.
1. `CREATE STATIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET` **(** `number1 number2 number3` **)** `[ ALLOW ( DISCONNECTION ^ RECONNECTION ) ]`
Creates static connector `id1` from child component `id2` to parent component `id3` offset by (`number1`, `number2`,
`number3`). A connector may be disconnected and reconnected to another component.
Bind to command `CommandCreateStaticConnector`.

2. `CREATE DYNAMIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET ALPHA` **(** `number1 number2 number3` **)** `BETA` **(** `number4 number5 number6` **)** `EXTENT INITIAL percent7 SPEED percent8 [ ALLOW ( DISCONNECTION ^ RECONNECTION ) ]`
Creates dynamic connector `id1` from child component `id2` to parent component `id3` offset at the minimum
extent by (`number1`, `number2`, `number3`) and at the maximum by (`number4`, `number5`, `number6`). The initial extent
is `percent7`. Change in position occurs at speed `percent8`. A connector may be disconnected and reconnected
to another component.
Bind to command `CommandCreateDynamicConnector`.

3. `BUILD MAIN THRUSTER GROUP id1 WITH ( THRUSTER | THRUSTERS ) idn+`
Builds main thruster group `id1` with main thrusters `idn`.
Bind to command `CommandBuildMainThrusterGroup`.

4. `BUILD VERNIER THRUSTER GROUP id1 WITH ( THRUSTER | THRUSTERS ) idn+`
Builds vernier thruster group `id1` with vernier thrusters `idn`.
Bind to command `CommandBuildVernierThrusterGroup`.

5. `ADD THRUSTER ( GROUP | GROUPS ) idn+ TO id1`
Adds thruster groups `idn` to component `id1`.
Bind to command `CommandAddThrusterGroups`.

### Behavioral Commands
Behaviorial commands are responsible for making entities perform a function.
1. `FIRE THRUSTER GROUP id FOR number ( SECOND | SECONDS ) [ AT THRUST thrust ]`
Fires thruster group `id` for `number` seconds. If the thrusters are variable, the thrust is set to `thrust`.
Bind to command `CommandFireThruster`.

1. `EXTEND STRUT id`
Extends dynamic strut `id` to its beta extent.
Bind to command `CommandExtendStrut`.

3. `RETRACT STRUT id`
Extends dynamic strut `id` to its alpha extent.
Bind to command `CommandRetractStrut`.

4. `DISCONNECT STRUT id`
Disconnects disconnectable strut `id`.
Bind to command `CommandDisconnectStrut`.

5. `RECONNECT STRUT id1 TO id2`
Reconnects reconnectable strut `id1` to component `id2`.
Bind to command `CommandReconnectStrut`.

6. `GENERATE FLIGHT PATH [ USING` **[** `(` **+** `|` **-** `) ( X | Y | Z ) (` **+** `|` **-** `) ( X | Y | Z ) (` **+** `|` **-** `) ( X | Y | Z )` **]** `] FROM {` **[** `number1 number2 number3` **]** **[** `numberi numberj numberk` **]**`+` **}** `WITH ATTITUDE RATE number4 MOTION RATE number5`
Generates a flight path from the current position to waypoint (`number1`, `number2`, `number3`) and then to one or
more waypoints (`numberi`, `numberj`, `numberk`). Attitude changes between waypoints occur at rate `number4`, and
the speed is `number5`. The coordinate system can be redefined as in Project Part 1; otherwise, the default is our
standard system. This functionality is experimental; there is no guarantee that a solution exists. 
Bind to command `CommandGeneratePath`.

7. `GENERATE FLIGHT PATH [ USING` **[** `(` **+** `|` **-** `) ( X | Y | Z ) (` **+** `|` **-** `) ( X | Y | Z ) (` **+** `|` **-** `) ( X | Y | Z )` **]** `] FROM `**'**`filename`**'**` WITH ATTITUDE RATE number4 MOTION RATE number5`
Functions the same as Command 6, but reads the coordinates from file `filename`. 
Bind to command `CommandGeneratePath`.

### Metacommands
Metacommands are responsible for controlling the simulation itself.

1. **@**`FORCE ATTITUDE ON id TO ( ( YAW attitude1 ) ^ ( PITCH attitude2) ^ ( ROLL attitude3 ) )`
Forces the attitude on root component `id` to any combination of yaw `attitude1`, pitch `attitude2`, and/or roll
`attitude3`. 
Bind to command `CommandMetaForceAttitude`.

2. **@**`FORCE ATTITUDE RATE ON id TO ( ( YAW attitude1 ) ^ ( PITCH attitude2) ^ ( ROLL attitude3 ) )`
Forces the attitude change rate on root component `id` to any combination of yaw `attitude1`, pitch `attitude2`,
and/or roll `attitude3`.
Bind to command `CommandMetaForceAttitudeRate`.

3. **@**`FORCE POSITION ON id TO ( ( number1 | _ ) ( number2 | _ ) ( number3 | _ ) )`
Forces the position on root component id to x number1, y number2, and z number3. Underscore indicates to use
the existing value. Bind to command CommandMetaForcePosition.

4. **@**`FORCE MOTION VECTOR ON id TO [ ( attitude1 | _ ) ( attitude2 | _ ) ( attitude3 | _ ) ( number4 | _ ) ]`
Forces the change in position on root component `id` to vector yaw `attitude1`, pitch `attitude2`, and roll
`attitude3` with magnitude `number4`. Underscore indicates to use the existing value.
Bind to command `CommandMetaForceMotionVector`.

5. **@**`CONFIG CLOCK number1 number2`
Changes the simulation clock to `number1` updates per wallclock second, with each update corresponding to
`number2` simulation seconds. 
Bind to command `CommandMetaConfigClock`.

6. **@**`WAIT number`
Waits `number` simulation seconds before executing the next command. 
Bind to command `CommandMetaWait`.

7. **@**`SCHEDULE number < command >`
Schedules `command` to execute at absolute simulation time number.
This command is not required in your solution. But think about the ramifications to your design. Encountering this
one after implementing the others could be very painful.
Bind to command `CommandMetaSchedule`.

8. **@**`LOAD string`
Loads command script with the filename in `string`. 
Bind to command `CommandMetaLoad`.

9. **@**`COMMIT`
Commits all creational and structural commands; only behavior commands are available after this point.
Bind to command `CommandMetaCommit`.

10. **@**`EXIT`
Exits the system. 
Bind to command `CommandMetaExit`.

11. **@**`PAUSE`
Pauses the simulation until any command is entered.
Bind to command `CommandMetaPause`.

12. **@**`RESUME`
Resumes the system after @PAUSE without doing anything else. However, any command causes a resume, too.
Bind to command `CommandMetaResume`.

13. **@**`DUMP COMPONENT id [ string ]`
Prints the state of component `id` to standard output with the optional message `string`. 
Bind to command `CommandMetaDumpComponent`.
