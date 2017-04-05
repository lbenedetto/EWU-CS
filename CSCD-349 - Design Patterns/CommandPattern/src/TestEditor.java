public class TestEditor {
	public static void main(String[] args) {
	DocumentInvoker myDocument = new DocumentInvoker ("message_to_rudi");
	 myDocument.Write("Stop your messin' around");
	 myDocument.Write("Better think of your future");
	 myDocument.Write("Time you straightened right out");
	 myDocument.Undo(0);
	 System.out.println(myDocument.Read());
	 myDocument.Redo(0);
	 System.out.println(myDocument.Read());
	}
}
