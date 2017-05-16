import s17cs350task1.*;

public class Tester {
	public static void main(String[] args) throws Exception {
		ComponentBox boxA = new ComponentBox("A", new Dimension3D (1 , 2, 3), true);
		ComponentBox boxB = new ComponentBox("B", new Dimension3D( 4, 6 , 8), false);
		ComponentBox boxC = new ComponentBox("C", new Dimension3D(10, 20, 30), false);
		ComponentBox boxD = new ComponentBox("D", new Dimension3D( 5, 15, 25), false);
		ComponentBox boxE = new ComponentBox("E", new Dimension3D( 7, 14, 21), false);
		boxA.connectChild(new Connector(boxE, new Point3D( 4, 5, 6)));
		boxA.connectChild(new Connector(boxB, new Point3D( 3, 2, 1)));
		boxB.connectChild(new Connector(boxC, new Point3D( 9, 8, 7)));
		boxB.connectChild(new Connector(boxD, new Point3D(10, 12, 14)));
		ExporterXML exporter = new ExporterXML();
		System.out.println(boxA.export(exporter));

	}
}
