
public class DocumentWriteCommand implements Command {

	private Document editableDoc;
    private String text;

    public DocumentWriteCommand(Document doc, String text)
    {
        this.editableDoc = doc;
        this.text = text;
        editableDoc.Write(text);
    }
	
	public void undo() {
		editableDoc.Erase(text);
	}
	
	public void redo() {
		editableDoc.Write(text);
	}
	public String toString(){
		return "Write";
	}
}
