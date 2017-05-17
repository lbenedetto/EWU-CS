package s17cs350task1;

public abstract class A_Exporter {
	StringBuilder output;
	private boolean isClosed;
	private boolean componentClosed;

	A_Exporter() {
		output = new StringBuilder();
		isClosed = false;
		componentClosed = true;
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

	public boolean isClosed() {
		return isClosed;
	}

	public boolean isComponentClosed() {
		return componentClosed;
	}

}
