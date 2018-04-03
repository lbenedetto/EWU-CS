.data
  prompt1: .asciiz "\n\nEnter first integer: "
  prompt2: .asciiz "\n\nEnter second integer: "
  prompt3: .asciiz "\nAddition result: "
  prompt4: .asciiz "\nSubtraction result: "
  x: .word 4
  y: .word 4
  a: .word 4
  s: .word 4
.text

main:
  # print
  li $v0, 4
  la $a0, prompt1
  syscall

  # read int
  li $v0, 5
  syscall
  sw $v0, x

  # print
  li $v0, 4
  la $a0, prompt2
  syscall

  # read int
  li $v0, 5
  syscall
  sw $v0, y

  # print
  li $v0, 4
  la $a0, prompt3
  syscall

  la $t0, x
  lw $t0, 0($t0)
  la $t1, y
  lw $t1, 0($t1)

  # compute sum
  add $t2, $t0, $t1
  sw $t2, a

  # print the sum
  li $v0, 4
  move $a0, $t2
  li, $v0, 1
  syscall

  # compute difference
  sub $t2, $t0, $t1
  sw $t2, s

  # print
  li $v0, 4
  la $a0, prompt4
  syscall

  # print the difference
  li $v0, 4
  move $a0, $t2
  li, $v0, 1
  syscall
li $v0, 10
syscall