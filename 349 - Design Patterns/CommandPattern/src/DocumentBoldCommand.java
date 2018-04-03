
public class DocumentBoldCommand implements Command {

	private Document editableDoc;
    private int line;

    public DocumentBoldCommand(Document doc, int line)
    {
        this.editableDoc = doc;
        this.line = line;
        editableDoc.Bold(line);
    }
	
	public void undo() {
		editableDoc.RemoveBold(line);
	}
	
	public void redo() {
		editableDoc.Bold(line);
	}
	
	public String toString(){
		return "Bold";
	}
}
