public class Driver5
{
   static public void main(String[] args)
   {
      String[] name = { "Simmons", "Hamel", "Clark", "Rolfe",
                        "Bickerstaff", "Inoue", "Steele", "Schimpf",
                        "Imamura", "Putnam", "Taylor",
                        "Capaul", "Steiner",
                        "Lemelin", "Pickett", "Peters", "Kamp"
                      };
      LinkedList5 list = new LinkedList5();

      for (int k = 0; k < name.length; k++)
          list.addLast(name[k]);
      System.out.println ("Printing in reverse:");
      list.listReverse();
      System.out.println ("\nCalling with empty list:");
      list.clear();
      list.listReverse();
      System.out.println ("\nFinished with Driver5");
   }
}