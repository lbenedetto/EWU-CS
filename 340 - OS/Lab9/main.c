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
	uint32_t count = 32;
	for (uint32_t i = (uint32_t) 1 << (uint32_t) 31; i != 0; i >>= 1, count--)
		if ((number & i) != 0) return count - 1;
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

	struct PTE *pte[numVirtualPages];
	struct PF *pf[numPhysicalPages];
	uint32_t frameNumber = 0;

	f1 = fopen("test.txt", "r");
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
		if (pte[pageNumber] != NULL) {
			pageFrame = pte[pageNumber]->pageFrame;
			pf[pageFrame]->pageNum = pageNumber;
		} else {
			pageFrame = frameNumber;

			struct PTE newPTE = {pageFrame, 0};
			struct PF newPF = {pageNumber};
			pte[pageNumber] = &newPTE;
			pf[pageFrame] = &newPF;
			frameNumber = (frameNumber + 1) % numPhysicalPages;
		}


		printf("Virtual Address: %d\n", virtualAddress);
		printf("Page Number: %d\n", pageNumber);
		printf("Page Frame Number: %d\n", pageFrame);
		printf("Physical Address: %d\n", physicalAddress);
		line = readLine(f1);
	}

}