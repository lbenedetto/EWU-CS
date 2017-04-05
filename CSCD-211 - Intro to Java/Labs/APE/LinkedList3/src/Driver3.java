public class Driver3
{
   static public void main(String[] args)
   {
      String[] name = { "Simmons", "Hamel", "Clark", "Rolfe",
                        "Bickerstaff", "Inoue", "Steele", "Schimpf",
                        "Imamura", "Putnam", "Taylor",
                        "Capaul", "Steiner",
                        "Lemelin", "Pickett", "Peters", "Kamp"
                      };
      LinkedList3 list = new LinkedList3();

      for (int k = 0; k < name.length; k++ )
         list.addOrdered(name[k]);

      System.out.printf("List (%d items)\n", list.size());
      list.listForward();

      System.out.println ("\nFinished with Driver3");
   }
}