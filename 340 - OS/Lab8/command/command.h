#ifndef LAB8_COMMAND_H
#define LAB8_COMMAND_H

#include <stdbool.h>
#include "../pipes/pipes.h"
#include "../process/process.h"
#include "../utils/utils.h"
#include "../linkedlist/linkedList.h"
#include "../linkedlist/listUtils.h"
#include "../redirects/redirects.h"

#define cmd_history 0
#define cmd_bangbang 1
#define cmd_bangN 2
#define cmd_alias 3
#define cmd_unalias 4
#define cmd_cd 5
#define cmd_histcount 6
#define cmd_histfilecount 7
#define cmd_path 8
#define cmd_exec 9
#define CONFIG_FILE ".msshrc"
#define HISTORY_FILE ".msshrc_history"

void handleCommand(char s[], bool isSilent);

void execFile(char *filename, bool isSilent);

void loadHistory();

void saveConfig();

void saveHistory();

void cleanUp();

void init();

char *getStartDir();

#endif //LAB8_COMMAND_H
