import java.io.*;
import java.util.Scanner;

public class Lab4Prob2
{
   public static void main(String [] args) throws Exception
   {
      Scanner fin = new Scanner(new File("input.txt"));
      
      String temp = "";
      
      while(fin.hasNext())
      {
         System.out.println(fin.nextLine());
      
      }// end while
      
      fin.close();
   
   }// end main
}// end class