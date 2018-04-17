#include <stdlib.h>

int write(int fd, char *str, int len) {
	/* do nothing */
	return 0;
}
int syscall(int n, ...) // this is correct don’t change anything it is … for the parameter
{
 asm("mov %0, %%ecx\n"
 "mov $1, %%ebx\n"
 "mov $8, %%edx\n"
 "mov $4, %%eax\n"
 "int $0x80\n"
 :
 : "r" ("goodbye\n")
);
 return 0;
}
int main() {
	write(1, "hello\n", 6);
	syscall(1, 1, "goodbye\n", 8);
	exit(0);
}
