public class Driver2
{
   static public void main(String[] args)
   {
      String[] name = { "Simmons", "Hamel", "Clark", "Rolfe",
                        "Bickerstaff", "Inoue", "Steele", "Schimpf",
                        "Imamura", "Putnam", "Taylor",
                        "Capaul", "Steiner",
                        "Lemelin", "Pickett", "Peters", "Kamp"
                      };
      LinkedList2 list = new LinkedList2();

      for (int k = 0; k < name.length; k++ )
         list.addLast(name[k]);

      System.out.printf("List (%d items)\n", list.size());
      list.listForward();

      System.out.println ("\nFinished with Driver2");
   }
}