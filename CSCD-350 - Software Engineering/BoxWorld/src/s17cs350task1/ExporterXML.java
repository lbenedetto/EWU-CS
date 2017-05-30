package s17cs350task1;

import javafx.concurrent.Task;

public class ExporterXML extends A_Exporter {
	public ExporterXML() {
		super();
		output.append("<components>\n");
	}

	@Override
	public void addPoint(String id, Point3D p) {
		validateID(id);
		if (p == null) throw new TaskException("Point was null");
		if (isComponentClosed()) throw new TaskException("Cannot add points outside of a component tag");
		output.append(String.format(
				"\t\t<point id=\"%s\" x=\"%s\" y=\"%s\" z=\"%s\"/>\n",
				id, p.getX(), p.getY(), p.getZ()));
	}

	@Override
	public void closeComponentNode(String id) {
		if (isComponentClosed()) throw new TaskException("Cannot close node if it is already closed");
		if (!getLastID().equals(id)) throw new TaskException("Closing tag mismatch");
		output.append("\t</component>\n");
		closeComponent();
	}

	@Override
	public void openComponentNode(String id) {
		validateID(id);
		if (!isComponentClosed()) throw new TaskException("Cannot open new node until previous node is closed");
		output.append(String.format("\t<component id=\"%s\" isRoot=\"true\">\n", id));
		openComponent(id);
	}

	@Override
	public void openComponentNode(String id, String idParent) {
		validateID(idParent);
		validateID(id);
		if (id.equals(idParent)) throw new TaskException("child matches parent");
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

	private void validateID(String id) {
		if (id == null || id.trim().equals("")) throw new TaskException("invalid id");
	}
}
