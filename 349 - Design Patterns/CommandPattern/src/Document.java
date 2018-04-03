import java.util.ArrayList;

public class Document {
	private ArrayList textArray;
	private String name;

	public Document(String name) {
		this.name = name;
		textArray = new ArrayList();
	}

	public void Write(String text) {
		textArray.add(text);
	}

	public void Erase(String text) {
		textArray.remove(text);
	}

	public void Bold(int number) {
		StringBuffer txt = new StringBuffer();
		txt.append("<b>" + textArray.get(number) + "</b>");
		textArray.set(number, txt.toString());
	}

	public void RemoveBold(int number) {
		String txt = (String) textArray.get(number);
		txt = txt.substring(3, txt.length() - 4);
		textArray.set(number, txt);
		System.out.println(txt.toString());
	}

	public void Erase(int number) {
		textArray.remove(number);
	}

	public String Read() {
		StringBuffer txt = new StringBuffer();
		txt.append("---- Contents of: " + name + " ----\n");
		for (int i = 0; i < textArray.size(); i++) {
			txt.append(i + ":" + (String) textArray.get(i) + "\n");
		}
		return txt.toString();
	}
}