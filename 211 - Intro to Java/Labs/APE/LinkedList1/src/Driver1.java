public class Driver1
{
   static public void main(String[] args)
   {
      String[] name = { "Simmons", "Hamel", "Clark", "Rolfe",
                        "Bickerstaff", "Inoue", "Steele", "Schimpf",
                        "Imamura", "Putnam", "Taylor",
                        "Capaul", "Steiner",
                        "Lemelin", "Pickett", "Peters", "Kamp"
                      };
      LinkedList1 list = new LinkedList1();
      Object item;

      // Adding in reversed order undone by using addFirst
      for (int k = name.length; k-- > 0; )
         list.addFirst(name[k]);

      System.out.println("Contents of the list:");
      for (int k = 0 ; k < name.length; k++ )
         System.out.println(list.get(k));
      System.out.println("Call with negative index:");
      item = list.get(-1);
      if ( item != null )
         System.out.println("Error:  non-null returned.");
      else
         System.out.println("Returned null reference.");
      System.out.println("Call with too large an index.");
      item = list.get(list.size);
      if ( item != null )
         System.out.println("Error:  non-null returned.");
      else
         System.out.println("Returned null reference.");

      System.out.println ("\nFinished with Driver1");
   }
}