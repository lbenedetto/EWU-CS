/**
 * The file that contains main is meant to
 * test your generic linked class using a basic
 * set of functions.
 *
 * @note This file will never be changed
 */
#include "./utils/myUtils.h"
#include "./utils/fileUtils.h"
#include "./linkedlist/listUtils.h"
#include "./linkedlist/linkedList.h"
#include "./linkedlist/requiredIncludes.h"

int main()
{

   int total = 5, choice;
   FILE * fin = NULL;

   LinkedList * intList = linkedList();
   buildListTotal(intList, total, stdin, buildInt);

   LinkedList * movieList = linkedList();
   fin = openInputFileType_Prompt("movies");
   total = readTotal(fin);
   buildListTotal(movieList, total, fin, buildTypeMovie);
   fclose(fin);

   do
   {
      choice = menu();

      switch(choice)
      {

	// MyInt
         case 1: printList(intList, printInt);
                 break;

         case 2: addFirst(intList, buildNode(stdin, buildInt));
                 break;

         case 3: addLast(intList, buildNode(stdin, buildInt));
                 break;

         case 4: sort(intList, compareInt);
                 break;

         case 5: removeFirst(intList, cleanInt);
		 break;

	 case 6: removeItem(intList, buildNode(stdin, buildInt), cleanInt, compareInt);
                 break;


	// Movies
	 case 7: printList(movieList, printTypeMovie);
                 break;

         case 8: addFirst(movieList, buildNode(stdin, buildTypeMovie_Prompt));
                 break;

         case 9: addLast(movieList, buildNode(stdin, buildTypeMovie_Prompt));
                 break;

	 case 10: removeLast(movieList, cleanTypeMovie);
		  break; 

         case 11: removeItem(movieList, buildNode(stdin, buildTypeMovie_Prompt), cleanTypeMovie, compareMovie);
                 break;

      }// end switch

   }while(choice != 12);

   clearList(intList, cleanInt);
   free(intList);
   intList = NULL;

   clearList(movieList, cleanTypeMovie);
   free(movieList);
   movieList = NULL;

   printf("Program Ended\n");

   return 0;

}// end main
