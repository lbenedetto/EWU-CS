.data
  array: .word 5,5,5,5,5,10,10,10,10,10
  prompt: .asciiz "Enter the number to compare: "
  result: .asciiz "Final Result: "
  newline: .asciiz "\n"
.text

  # print
  li $v0, 4
  la $a0, prompt
  syscall

  # read int
  li $v0, 5
  syscall
  move $t0, $v0
  # $t0, int to search for
  jal search
  
  li $v0, 4
  la $a0, result
  syscall
  
  lw $t1, 0($sp)
  move $a0, $t1
  li $v0, 1
  syscall
  
  j exit
  
  search:
    # store the return link in the stack
    addi $sp, $sp, -4
    sw $ra, 0($sp)

    la $t1, array #load the address of the array into t1
    addi $sp, $sp, -4
    sw $t1, 0($sp) # load address of array into the stack
    # $t2 value at ix
    add $t3, $t1, 36 #store the end address of the array in $t3
    li $t1, 0 # the value to keep track of the sum
  loop:
    lw $t1, 0($sp)
    bgt $t1, $t3, return
    lw $t1, 0($t1)
    bgt $t0, $t1, increment
      jal addToSum
    increment:
    lw $t1, 0($sp)
    addi $t1, $t1, 4
    sw $t1, 0($sp)

    j loop
    return:
    addi $sp, $sp, 4
    lw $ra, 0($sp)
    sw $t4, 0($sp)
    jr $ra

  addToSum:
    add $t4, $t4, $t1
    move $a0, $t4
    li $v0, 1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
    jr $ra
exit: