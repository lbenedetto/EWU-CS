#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

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

struct PTE {
	int pageFrame;
	int currentBit;
};

struct PF {
	int pageNum;
};

int main() {
	FILE *f1 = fopen("setup.txt", "r");
	int vas, pageSize, pas;
	readInt(f1, &vas);
	readInt(f1, &pageSize);
	readInt(f1, &pas);
	fclose(f1);
	int numVirtualPages = vas / pageSize;
	int numPhysicalPages = pas / pageSize;

	struct PTE pte[numVirtualPages];
	struct PF pf[numPhysicalPages];

	f1 = fopen("test.txt", "r");
	char *line = readLine(f1);
	while (line != NULL) {
		uint32_t address;
		uint32_t offset = address << (32 - pageSize);
		offset = offset >> (32 - pageSize);

		sscanf(line, "%d", &address);
		free(line);
		printf("Virtual Address: %d", address);

		line = readLine(f1);
	}

}