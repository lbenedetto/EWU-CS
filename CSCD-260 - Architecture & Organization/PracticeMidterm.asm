.data
  prompt: .asciiz "Enter x value: "
  my_array: .word 10,20,-30,421,-7,6,41,40,39,1,2,-3,42,-7,6,411,400,9,56,65
  N: .word 20
.text
  main:
    jal read_input # read int into $t0
  # t0 is x
  # t1 is the array
  # t2 is N
    la $t1, my_array
    la $t2, N
    jal func
    # print result
    li $v0, 4
    la $a0, N
    li, $v0, 1
    syscall
    j exit
    
  read_input:
    # print
    li $v0, 4
    la $a0, prompt
    syscall

    # read int
    li $v0, 5
    syscall
    move $t0, $v0
    jr $ra
    
  func:
    # take t0,1,2,3 and find the smallest in the array buy bigger than x
    # store that value in t2
    
    # t0 is x
    # t1 is the array
    # t2 is N
    # t3 is ix
    # t4 the smallest
    # t5 is curr
    lw $t4, 0($t1)
    loop:
      bgt $t3,$t2,return # for 0 through N
      lw $t5, 0($t1) # put the value at ix in $t5
      bgt $t0, $t5, L1 # if x > curr, jump L1
      bgt $t5, $t4, L1 # if curr > smallest, jump L1
      move $t4, $t5
      L1: addi $t3,$t3,1 # increment ix by 1
      addi $t1, $t1, 4 # increment address by 4
      j loop
    return:
      sw $t4, N
      jr $ra
exit:
li $v0, 10
syscall