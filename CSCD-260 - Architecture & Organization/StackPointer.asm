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
  la $t1, array
  # $t2 value at ix
  add $t3, $t1, 36
  li $t4, 0
    
  jal search
  
  li $v0, 4
  la $a0, result
  syscall
  
  move $a0, $t4
  li $v0, 1
  syscall
  
  j exit
  
  search:
    addi $sp, $sp, -4
    sw $ra 0($sp)
  loop:
    bgt $t1,$t3,return
    lw $t2, 0($t1)
    bgt $t0, $t2, increment
      jal addToSum
    increment:
    addi $t1, $t1, 4
    j loop
    return:
    lw $ra, 0($sp)
    addi $sp, $sp, 4    
    jr $ra
    
    addToSum:
    add $t4, $t4, $t2
    move $a0, $t4
    li $v0, 1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
    jr $ra
    
    
 exit: