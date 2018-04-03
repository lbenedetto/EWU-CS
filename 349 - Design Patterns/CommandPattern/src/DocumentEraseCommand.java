
public class DocumentEraseCommand implements Command {

	private Document editableDoc;
    private String text;

    public DocumentEraseCommand(Document doc, String text)
    {
        this.editableDoc = doc;
        this.text = text;
        editableDoc.Erase(text);
    }
	
	public void undo() {
		editableDoc.Write(text);
	}
	
	public void redo() {
		editableDoc.Erase(text);
	}
	public String toString(){
		return "Erase";
	}

}
