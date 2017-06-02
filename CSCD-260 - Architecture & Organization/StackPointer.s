.data
  my_array: .word 5, 5, 5, 5, 5, 10, 10, 10, 10, 10
  prompt: .asciiz "Integer value to compare to: "
.text
  main:
  li $v0, 4
  la $a0, prompt
  syscall # print prompt

  li $v0, 5
  syscall # read input

  li $t1, 40   # array size
  la $t2, my_array # array

  addi $sp, $sp, -12 # make room for 3 new values on the stack
  sw $v0, 8($sp)   # store value entered
  sw $t1, 4($sp)   # store array size
  sw $t2, 0($sp)   # store array

  jal checkArray

  exit:
    lw $a0, 0($sp)
    li, $v0, 1
    syscall    # print the int
    addi $sp, $sp, 32
    li  $v0, 10
    syscall

  checkArray:
    lw $t0, 8($sp)   # load input
    lw $t1, 4($sp)   # load size
    lw $t2, 0($sp)   # load array
    li $t3, 0        # load return value

    addi $sp, $sp, -20 # make room for duplicate stack bullshit
    sw $ra, 16($sp) # store return link
    sw $t0, 12($sp) # store input
    sw $t1, 8($sp)  # store size
    sw $t2, 4($sp)  # store array
    sw $t3, 0($sp)  # store return value

    add $t4, $t1, $t2 # calculate stop address

    forLoop:
      bge $t2, $t4, end # &cur == &stop
        lw $t5, 0($t2)
        bge $t0, $t5, increment # if n > v, increment
          jal addval
        increment:
        addi $t2,$t2,4 # increment array by 4 bytes
        j forLoop
    end:
      lw $ra, 16($sp)
      jr $ra

  addval:
    lw $t3, 0($sp) # get the return value from the stack
    add $t3, $t5, $t3 # add the curr value to the ret value
    sw $t3, 0($sp) # store the new ret value back into the stack
    jr $ra # return