#include "movie.h"
#include "../utils/fileUtils.h"

/**
* @brief Cleans up all dynamically allocated memory for the movie
*
* Cleans up and frees all the dynamically allocated memory
* In this case title, actors->first, actors->last and the actors array
*
* Each pointer in the specific data type is set to NULL after it has been freed.
*
* @param passedIn - The void * passed in representing the specific movie
*
* @warning - The passed in void * passedIn is checked - exit(-99) if NULL
*/
void cleanTypeMovie(void *ptr) {
    if (ptr == NULL) exit(-99);
    Movie *movie = (Movie *) ptr;

    free(movie->title);
    movie->title = NULL;
    free(movie->actors->first);
    movie->actors->first = NULL;
    free(movie->actors->last);
    movie->actors->last = NULL;
    free(movie->actors);
    movie->actors = NULL;

    free(movie);
    movie = NULL;
}


/**
 * @brief Builds and returns a single movie.
 *
 * Reads the initial data from the file and then builds
 * an object of type movie.
 *
 * @note The carriage return will be removed when reading from the file
 *
 * @param fin - The FILE * representing the open file
 * @return void * - Representing a movie object
 *
 * @warning - The passed in FILE * fin is checked - exit(-99) if NULL
 */
void *buildTypeMovie(FILE *fin) {
    if(fin == NULL) exit(-99);
    Movie *movie = calloc(1, sizeof(*movie));
    movie->title = readLine(fin);
    readInt(fin, &movie->totalActors);
    //movie->actors = (Actor *) calloc(movie->totalActors, sizeof(Actor));//Parameter type mismatch: using size_t for signed values of type int
    movie->actors = (Actor *) malloc(movie->totalActors * sizeof(Actor));
    for (int i = 0; i < movie->totalActors; i++) {
        char *line = readLine(fin);
        movie->actors[i].first = strtok(line, " ");
        movie->actors[i].last = strtok(NULL, " ");
    }
    return (void *) movie;
}


/**
 * @brief Prints the specific movie.
 *
 * Format is title
 * Starting:
 * actor first actor last, actor first actor last
 *
 * @note: price is printed to two decimal places
 *
 * @param passedIn - The void * passed in representing a specific movie
 *
 * @warning - The passed in void * passedIn is checked - exit(-99) if NULL
 */
void printTypeMovie(void *passedIn) {
    if(passedIn == NULL) exit(-99);
    Movie *m = (Movie *) passedIn;
    printf("%s\n", m->title);
    for (int i = 0; i < m->totalActors; i++) {
        printf("\t%s %s", m->actors[i].first, m->actors[i].last);
        if (i - 1 == m->totalActors) printf(",");
        else printf("\n");
    }
}


/**
 * @brief Builds and returns a single movie.
 *
 * Reads the initial data from the keyboard by prompting the user.
 * A movie object is constructed.
 *
 * @note The carriage return will be removed when reading from the keyboard
 *
 * @param fin - The FILE * representing stdin
 * @return void * - Representing a movie object
 *
 * @warning - The passed in FILE * fin is checked - exit(-99) if NULL
 */
void *buildTypeMovie_Prompt(FILE *fin) {
    if(fin == NULL) exit(-99);
    Movie *movie = calloc(1, sizeof(*movie));
    printf("Enter movie title: ");
    movie->title = readLine(fin);
    printf("Enter number of actors: ");
    readInt(fin, &movie->totalActors);
    //movie->actors = (Actor *) calloc(movie->totalActors, sizeof(Actor));//Parameter type mismatch: using size_t for signed values of type int
    movie->actors = (Actor *) malloc(movie->totalActors * sizeof(Actor));
    for (int i = 0; i < movie->totalActors; i++) {
        printf("Enter actor #%d: ", i + 1);
        char *line = readLine(fin);
        movie->actors[i].first = strtok(line, " ");
        movie->actors[i].last = strtok(line, " ");
    }
    return (void *) movie;
}


/**
 * @brief Compares two objects of type movie.
 *
 * movies are compared based on title and if the title are the same then totalActors
 *
 * @note The passed in items will need to be cast to the appropriate movie type.
 *
 * @param p1 - The void * representing an object of type movie
 * @param p2 - The void * representing an object of type movie
 * @return int - Representing order < 0 indicates p1 is less than p2,
 * > 0 indicates p1 is greater than p2 and == 0 indicates p1 == p2 for contents
 *
 * @warning - The passed in void * p1 is checked - exit(-99) if NULL
 * @warning - The passed in void * p2 is checked - exit(-99) if NULL
 */
int compareMovie(const void *p1, const void *p2) {
    if (p1 == NULL || p2 == NULL) exit(-99);
    Movie *m1 = (Movie *) p1;
    Movie *m2 = (Movie *) p2;
    int i = strcmp(m1->title, m2->title);
    if (i == 0)
        i = m1->totalActors - m2->totalActors;
    return i;
}