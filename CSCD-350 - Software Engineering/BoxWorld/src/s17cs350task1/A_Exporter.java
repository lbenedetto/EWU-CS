package s17cs350task1;

public abstract class A_Exporter {
	StringBuilder output;
	boolean isClosed;

	A_Exporter() {
		isClosed = false;
		closeExport();
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
}
