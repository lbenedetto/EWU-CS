package s17cs350task1;

public class ExporterXML extends A_Exporter {
	public ExporterXML() {
		super();
		output.append("<components>\n");
	}

	@Override
	void addPoint(String id, Point3D p) {
		output.append(String.format(
				"\t\t<point id=\"%s\" x=\"%s\" y=\"%s\" z=\"%s\"/>\n",
				id, p.getX(), p.getY(), p.getZ()));
	}

	@Override
	void closeComponentNode(String id) {
		output.append("\t</component>\n");
	}

	@Override
	void openComponentNode(String id) {
		output.append(String.format("\t<component id=\"%s\" isRoot=\"true\">\n", id));
	}

	@Override
	void openComponentNode(String id, String idParent) {
		output.append(String.format("\t<component id=\"%s\" isRoot=\"false\" idParent=\"%s\">\n", id, idParent));
	}

	@Override
	public void closeExport() {
		output.append("</components>");
	}
}
