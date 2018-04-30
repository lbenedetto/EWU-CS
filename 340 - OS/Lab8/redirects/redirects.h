#ifndef LAB8_REDIRECT_H
#define LAB8_REDIRECT_H

#include "../process/process.h"
#include "../utils/utils.h"
#include "../command/command.h"

#define std_in 0
#define std_out 1
#define child 0

bool checkRedirects(char *s, int *inCount, int *outCount);

void redirectIt(char *s, bool isIn, bool isOut);

#endif //LAB8_REDIRECT_H
