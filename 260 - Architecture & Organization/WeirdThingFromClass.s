main:
lui $t0, 0xFFFF
again:
	lw $t1, 0($t0)
	andi $t1, $t1, 1
	beq $t1, $0, again
	lw $t1, 4($t0)
againx:
	lw $t2, 8($t0)
	andi $t2, $t2, 1
	beq $t2, $0, againx
	sw $t1, 12($t0)
	j again

li  $v0, 10
syscall