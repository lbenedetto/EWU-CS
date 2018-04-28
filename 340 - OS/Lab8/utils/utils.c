#include <memory.h>
#include <sys/stat.h>
#include "utils.h"

int doesFileExist(const char *filename) {
	struct stat st;
	int result = stat(filename, &st);
	return result == 0;
}

int isFileEmpty(FILE *fp){
	fseek (fp, 0, SEEK_END);
	long size = ftell(fp);
	fseek(fp, 0, SEEK_SET);

	return 0 == size;
}

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

void strip(char *array) {
	if (array == NULL) {
		perror("array is null");
		exit(-99);
	}

	size_t len = strlen(array), x = 0;

	while (array[x] != '\0' && x < len) {
		if (array[x] == '\r')
			array[x] = '\0';
		else if (array[x] == '\n')
			array[x] = '\0';
		x++;
	}
}

char *concat(char *s1, char *s2) {
	char *s3 = (char *) malloc(1 + strlen(s1) + strlen(s2));
	strcpy(s3, s1);
	strcat(s3, s2);
	return s3;
}