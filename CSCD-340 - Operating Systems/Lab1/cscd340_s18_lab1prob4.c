#include <stdio.h>
#include <stdlib.h>

static int v_init0 = 1;     // 1
int v_uninit0;              // 2

int main(int argc, char *argv[])  // 3 & 4
{
	int v_init1 = 1;       // 5
	int v_uninit1;         // 6

	static int v_init2= 1; // 7
	static int v_uninit2;  // 8

	int *v_ptr = (int *) calloc(32, sizeof(int));  // 9

	free(v_ptr);

	return 0;


}// end main
