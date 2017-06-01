.data
	name: .asciiz "Lars Benedetto"
	divider: .asciiz "\nBackwards:\n"
.text
	main:
	la $s0, name
	li $t0, 0

	jal printForwards

	li $v0, 4
	la $a0, divider
	syscall

	jal printReverse

	j exit
	
	printForwards:
		lb $t1, 0($s0)
		move $a0, $t1
		li $v0, 11
		syscall

		addi $s0, $s0, 1
		addi $t0, $t0, 1

		beq $t0, 14, return
			addi $sp, $sp, -4
			sw $ra, 0($sp)
			jal printForwards

		return:
			lw $ra, 0($sp)
			addi $sp, $sp, 4
			jr $ra

	printReverse:
		lb $t1, 0($s0)
		move $a0, $t1
		li $v0, 11
		syscall

		subi $s0, $s0, 1
		subi $t0, $t0, 1
		blt $t0, 0, return1
			addi $sp, $sp, -4
			sw $ra, 0($sp)
			jal printReverse
		return1:
			lw $ra, 0($sp)
			addi $sp, $sp, 4
			jr $ra
	exit:
		li $v0, 10
		syscall
