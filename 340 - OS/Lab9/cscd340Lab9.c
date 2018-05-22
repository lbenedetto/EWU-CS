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

void readInt(FILE *fin, uint32_t *i) {
	char *temp = readLine(fin);
	sscanf(temp, "%d", i);
	free(temp);
}

struct PTE {
	uint32_t pageFrame;
	uint32_t currentBit;
};

struct PF {
	uint32_t pageNum;
};

uint32_t countBits(uint32_t number) {
	uint32_t count = 31;
	uint32_t i;
	for (i = (uint32_t) 1 << (uint32_t) 31; i != 0; i >>= 1, count--)
		if ((number & i) != 0) return count;
}

int main() {
	FILE *f1 = fopen("setup.txt", "r");
	uint32_t vas, pageSize, pas;
	readInt(f1, &vas);
	readInt(f1, &pageSize);
	readInt(f1, &pas);
	fclose(f1);
	uint32_t numVirtualPages = vas / pageSize;
	uint32_t numPhysicalPages = pas / pageSize;

	struct PTE pte[numVirtualPages];
	struct PF pf[numPhysicalPages];
	uint32_t frameNumber = 0;

	printf("Enter filename: ");
	char *filename = readLine(stdin);
	f1 = fopen(filename, "r");
	free(filename);
	char *line = readLine(f1);
	while (line != NULL) {
		uint32_t virtualAddress;
		uint32_t pageNumber;
		uint32_t pageFrame;
		uint32_t physicalAddress;
		sscanf(line, "%d", &virtualAddress);
		free(line);

		uint32_t numBitsPS = countBits(pageSize);

		pageNumber = virtualAddress >> numBitsPS;
		if (pte[pageNumber].currentBit > 0) {
			pageFrame = pte[pageNumber].pageFrame;
			pf[pageFrame].pageNum = pageNumber;
		} else {
			pageFrame = frameNumber;
			pte[pageNumber].pageFrame = pageFrame;
			pte[pageNumber].currentBit = 1;
			pf[pageFrame].pageNum = pageNumber;
			frameNumber = (frameNumber + 1) % numPhysicalPages;
		}

		physicalAddress = virtualAddress << (32 - numBitsPS) >> (32 - numBitsPS);
		physicalAddress = physicalAddress | (pageFrame << numBitsPS);

		printf("Virtual Address: %d\n", virtualAddress);
		printf("Page Number: %d\n", pageNumber);
		printf("Page Frame Number: %d\n", pageFrame);
		printf("Physical Address: %d\n", physicalAddress);
		printf("=========================\n");
		line = readLine(f1);
	}
}