package s17cs350task1;

import java.util.ArrayList;
import java.util.List;

public abstract class A_Exporter {
	StringBuilder output;
	private boolean isClosed;
	private String lastID;
	private boolean componentClosed;
	List<String> addedPoints;
	List<String> addedNodes;

	A_Exporter() {
		output = new StringBuilder();
		isClosed = false;
		componentClosed = true;
		lastID = "";
		addedNodes = new ArrayList<>();
	}

	abstract void addPoint(String id, Point3D point);

	abstract void closeComponentNode(String id);

	abstract void openComponentNode(String id);

	abstract void openComponentNode(String id, String idParent);

	public String export() {
		return output.toString();
	}

	public void closeExport() {
		isClosed = true;
	}

	boolean isClosed() {
		return isClosed;
	}

	boolean isComponentClosed() {
		return componentClosed;
	}

	void openComponent(String id) {
		componentClosed = false;
		lastID = id;
		addedPoints = new ArrayList<>();
		addedNodes.add(id);
	}

	void closeComponent() {
		componentClosed = true;
	}

	String getLastID() {
		return lastID;
	}
}
