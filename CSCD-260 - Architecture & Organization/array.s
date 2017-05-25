.data
  prompt: .asciiz "Enter number to search for: "
  numbers: .word 1, 1, 1, 1, 3, 3, 34, 5, 5, 6, 6, 4, 3
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

  # $t0 int to search for
  # $t1 index
  # $t2 number of occurances
  # $t3 the array
  # $t4 the value at the ix

  li $t1, 0
  li $t2, 0
  la $t3, numbers
  loop:
    bgt $t1,12,exit # for 0 through 12
      lw $t4, 0($t3) # put the value at ix in $t5
      bne $t0, $t4, L1 # if != jump to incrementer
        addi $t2, $t2, 1 # if == increment num occurances
      L1: addi $t1,$t1,1 # increment ix by 1
      addi $t3, $t3, 4 # increment address by 4
  j loop

  exit:
    # print the number of occurances
    li $v0, 4
    move $a0, $t2
    li, $v0, 1
    syscall
li $v0, 10
syscall
