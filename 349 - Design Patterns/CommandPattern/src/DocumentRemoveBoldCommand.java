
public class DocumentRemoveBoldCommand implements Command {

	private Document editableDoc;
    private int line;

    public DocumentRemoveBoldCommand(Document doc, int line)
    {
        this.editableDoc = doc;
        this.line = line;
        editableDoc.RemoveBold(line);
    }
	
	public void undo() {
		editableDoc.Bold(line);
	}
	
	public void redo() {
		editableDoc.RemoveBold(line);
	}
	public String toString(){
		return "UnBold";
	}
}
