public class Driver4 {
    static public void main(String[] args) {
        String[] name = {"Simmons", "Hamel", "Clark", "Rolfe",
                "Bickerstaff", "Inoue", "Steele", "Schimpf",
                "Imamura", "Putnam", "Taylor",
                "Capaul", "Steiner",
                "Lemelin", "Pickett", "Peters", "Kamp"
        };
        LinkedList4 list = new LinkedList4();
        int k, n;
        Object item;

        n = name.length / 2;
        for (k = 0; k < n; k++)
            list.addLast(name[k]);
        System.out.println("Initial list");
        list.listForward();
        System.out.println("\nTesting remove middle");
        k = n / 2;
        if (list.remove(name[k]))
            System.out.printf("Success in position %d (%s)\n", k, name[k]);
        else
            System.out.printf("Failed to remove existing node %s\n", name[k]);
        if (list.remove(name[k - 1]))
            System.out.printf("Success in position %d (%s)\n", k - 1, name[k - 1]);
        else
            System.out.printf("Failed to remove existing node %s\n", name[k - 1]);
        System.out.println("\nRemove from the front.");
        if (list.remove(name[0]))
            System.out.printf("Success in position 0 (%s)\n", name[0]);
        else
            System.out.printf("Failed to remove existing node %s\n", name[0]);
        System.out.println("\nRemove from the back.");
        k = n - 1;
        if (list.remove(name[k]))
            System.out.printf("Success in position %d (%s)\n", list.size(), name[k]);
        else
            System.out.printf("Failed to remove existing node %s\n", name[k]);
        System.out.printf("\nRemaining list of size %d:\n", list.size());
        list.listForward();

        System.out.print("\nLooking for Love");
        if (list.remove("Love"))
            System.out.println(" --- ERROR:  success claimed");
        else
            System.out.println(" in one of the wrong places");
        System.out.printf("\nFinal state of size %d:\n", list.size());
        list.listForward();

        System.out.println("\nFinished with Driver4");
    }
}