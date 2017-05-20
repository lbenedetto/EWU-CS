package s17cs350task1;

import javafx.concurrent.Task;

public class ExporterXML extends A_Exporter {
	public ExporterXML() {
		super();
		output.append("<components>\n");
	}

	@Override
	void addPoint(String id, Point3D p) {
		if (isComponentClosed()) throw new TaskException("Cannot add points outside of a component tag");
		output.append(String.format(
				"\t\t<point id=\"%s\" x=\"%s\" y=\"%s\" z=\"%s\"/>\n",
				id, p.getX(), p.getY(), p.getZ()));
	}

	@Override
	void closeComponentNode(String id) {
		if (isComponentClosed()) throw new TaskException("Cannot close node if it is already closed");
		if (!getLastID().equals(id)) throw new TaskException("Closing tag mismatch");
		output.append("\t</component>\n");
		closeComponent();
	}

	@Override
	void openComponentNode(String id) {
		if (!isComponentClosed()) throw new TaskException("Cannot open new node until previous node is closed");
		output.append(String.format("\t<component id=\"%s\" isRoot=\"true\">\n", id));
		openComponent(id);
	}

	@Override
	void openComponentNode(String id, String idParent) {
		if (!isComponentClosed()) throw new TaskException("Cannot open new node until previous node is closed");
		output.append(String.format("\t<component id=\"%s\" isRoot=\"false\" idParent=\"%s\">\n", id, idParent));
		openComponent(id);
	}

	@Override
	public void closeExport() {
		if (isClosed()) throw new TaskException("Cannot close export, export is already closed");
		output.append("</components>");
		super.closeExport();
	}
}
