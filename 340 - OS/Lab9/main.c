#include <stdio.h>
#include <stdlib.h>

char *readLine(FILE *fin) {
	char *str = NULL;
	size_t size;
	ssize_t chars = getline(&str, &size, fin);
	if (chars > 0) {
		if (str[chars - 1] == '\n') {
			str[chars - 1] = '\0';
		}
		return str;
	}
	free(str);
	return NULL;
}

void readInt(FILE *fin, int *i) {
	char *temp = readLine(fin);
	sscanf(temp, "%d", i);
	free(temp);
}

int main() {
	FILE *f1 = fopen("setup.txt", "r");
	int vas, pageSize, pas;
	readInt(f1, &vas);
	readInt(f1, &pageSize);
	readInt(f1, &pas);
	int numPages = vas / pageSize;

}