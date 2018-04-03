public class Driver0
{
   static public void main(String[] args)
   {
      String[] name = { "Simmons", "Hamel", "Clark", "Rolfe",
                        "Bickerstaff", "Inoue", "Steele", "Schimpf",
                        "Imamura", "Putnam", "Taylor",
                        "Capaul", "Steiner",
                        "Lemelin", "Pickett", "Peters", "Kamp"
                      };
      LinkedList0 list = new LinkedList0();

      for (int k = name.length; k-- > 0; )
         list.addFirst(name[k]);

      System.out.printf("List (%d items)\n", list.size());
      list.listForward();
      list.clear();
      System.out.printf("\nList (%d items)\n", list.size());
      list.listForward();
      System.out.println ("\nFinished with Driver0");
   }
}