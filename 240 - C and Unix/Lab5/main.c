#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main() {
	char goAgain = 0;
	int upperBound, randomNumber, guess, remainingGuesses;
	do {
		remainingGuesses = 15;
		printf("Please enter a number greater than 99 for A, the upbound: ");
		scanf("%d", &upperBound);
		while (getchar() != '\n');
		srand(time(NULL));
		randomNumber = rand() % (upperBound + 1);
		printf("Answer is: %d\n", randomNumber);
		do {
			printf("You have %d guesses remaining. \n ", remainingGuesses);
			printf("Please enter a guess between 1 & %d inclusive: ", upperBound);
			scanf("%d", &guess);
			while (getchar() != '\n');
			while (guess > upperBound || guess < 1) {  //previous enter out of range, ask user to re-enter
				printf("Please enter a guess between 1 & %d inclusive: ", upperBound);
				scanf("%d", &guess);
				while (getchar() != '\n');
			}
			if (guess > randomNumber) printf("Too High\n");
			else if (guess < randomNumber) printf("Too Low\n");
			else {
				printf("You got it in %d guesses\n", 15 - remainingGuesses);
			}
			remainingGuesses--;
		} while (randomNumber != guess && remainingGuesses > 0);
		do {
			printf("Would you like to play again? (y/n) ");
			scanf(" %c", &goAgain);
		} while (goAgain != 'y' && goAgain != 'n');
		while (getchar() != '\n');
	} while (goAgain == 'y');
	printf("Bye");
	return 0;
}
