.data
	prompt: .asciiz "Enter number to search for: "
	Q1: .asciiz "\nQ1:\n"
	Q2: .asciiz "\nQ2:\n"
	newline: .asciiz "\n"
	space: .asciiz " "
	my_array: .word 10, 20, -30, 421, -7,  6, 41, 40, 39, 1,  2, -3, 42, -7, 6,  411, 400, 9, 56, 65,  66, -88, 33, 44, 55,  66, 77
	target_array: .space 120			
	N: .word 25   #size of my_array (there are 27 words in my_array, but we deal only the first 25 elements)

.text
main:
  # print
  li $v0, 4
  la $a0, prompt
  syscall

  # read int
  li $v0, 5
  syscall
  move $t0, $v0

  #print Q1 marker
  li $v0, 4
  la $a0, Q1
  syscall
  
  # $t0 int to search for
  # $t1 the array
  # $t2 the value at the ix
  # $t3 stop address

  la $t1, my_array
  add $t3, $t1, 96

  Q1Loop:
    bgt $t1,$t3,exitLoop1 # for 0 through 25
    lw $t2, 0($t1) # put the value at ix in $t3
    bgt $t0, $t2, increment1 # if n > v
      jal printT2
    increment1: addi $t1,$t1,4 # increment ix by 4
    j Q1Loop

  exitLoop1:
  
  #print Q2 marker
  li $v0, 4
  la $a0, Q2
  syscall
  
  # $t0 int to search for
  # $t1 the array
  # $t2 the value at the ix
  # $t3 stop address
  # $t4 target array
  # $t5 size of target array
  
  la $t1, my_array
  add $t3, $t1, 96
  la $t4, target_array
  li $t5, -4
  
  Q2Loop:
    bgt $t1,$t3,exitLoop2 # for 0 through 25
    lw $t2, 0($t1) # put the value at ix in $t3
    bgt $t0, $t2, increment2 # if n > v
      sw $t2,0($t4)
      addi $t4,$t4,4
      addi $t5,$t5,4
    increment2: addi $t1,$t1,4 # increment ix by 4
    j Q2Loop
  exitLoop2:
  
  # $t0 the array
  # $t1 the stop address
  # $t2 curr value
  
  la $t0, target_array #Prep for printing array
  add $t1,$t0,$t5
  
  Q3Loop:
    bgt $t0,$t1,exit # for 0 through 25
    lw $t2, 0($t0) # put the value at ix in $t3
    jal printT2
    addi $t0,$t0,4 # increment ix by 4
    j Q3Loop
    
    printT2:
      move $a0, $t2
      li, $v0, 1
      syscall # print the int
      li $v0, 4
      la $a0, space
      syscall # print a space
      jr $ra
      
  exit:
  
  
