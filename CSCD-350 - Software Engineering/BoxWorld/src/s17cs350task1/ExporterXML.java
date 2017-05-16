package s17cs350task1;

public class ExporterXML extends A_Exporter {
	ExporterXML() {
		super();
	}

	@Override
	void addPoint(String id, Point3D p) {
		output.append(String.format(
				"<point id=\"%s\" x=\"%s\" y=\"%s\" z=\"%s\"/>",
				id, p.getX(), p.getY(), p.getZ()));
	}

	@Override
	void closeComponentNode(String id) {
		output.append("</component>");
	}

	@Override
	void openComponentNode(String id) {
		output.append(String.format("<component id=\"%s\" isRoot=\"true\">", id));
	}

	@Override
	void openComponentNode(String id, String idParent) {
		output.append(String.format("<component id=\"%s\" isRoot=\"false\" idParent=\"%s\">", id, idParent));
	}

	@Override
	public void closeExport() {

	}
}
