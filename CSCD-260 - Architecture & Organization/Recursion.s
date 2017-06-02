.data
	name: .asciiz "Lars Benedetto\n"
	divider: .asciiz "\nBackwards:\n"
.text
	main:
	la $t0, name # load address of array
	addi $sp, $sp, -4 # make room on stack
	sw $t0, 0($sp) #store array on stack
	jal printForwards # this also loads all the letters onto the stack
	j exit
	
	printForwards: #(4($sp) letter, 0($sp) : Name Array)
		lw $t0, 0($sp) #load array into t0
		lb $t1, 0($t0) #load curr letter from array

		move $a0, $t1
		li $v0, 11
		syscall # print the current letter
		li $t2, '\n' #load newline
		beq $t1, $t2, return
			addi $t0, $t0, 1 #increment array
			addi $sp, $sp, -12 # make room for return link, letter and array
			sw $ra, 8($sp) # store return link
			sw $t1, 4($sp) # store letter
			sw $t0, 0($sp) # store array
			jal printForwards
		return:
			lw $ra, 8($sp) # load return link
			lw $t0, 4($sp) # load letter
			addi $sp, $sp, 12 # dump the stack

			move $a0, $t0 #begin print
			li $v0, 11
			syscall # print the current letter

			jr $ra

	exit:
		li $v0, 10
		syscall
