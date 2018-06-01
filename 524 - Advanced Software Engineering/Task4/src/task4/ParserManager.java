package task4;

import task4.node.NodeComponent;
import task4.node.NodeTriple;
import task4.parser.MvcParser;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class ParserManager {
	private static HashMap<String, NodeComponent> variables = new HashMap<>();
	private static String outFileName;

	public static void main(String[] args) {
		if (args.length != 2) {
			throw new RuntimeException("usage: input_filename output_filename ");
		}
		try {
			outFileName = args[1];
			MvcParser mvcParser = new MvcParser(args[0], args[1]);
			mvcParser.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void defineVariable(String variableName, NodeComponent nodeComponent) {
		variables.put(variableName, nodeComponent);
	}

	public static void exportToGnuplot(String variable, NodeTriple triple) {
		NodeComponent node = variables.get(variable);
		if (node == null) throw new RuntimeException("Invalid identifier");
		try {
			String gnuPlot = node.exportToGnuplot(triple);
			var fw = new FileWriter(new File(outFileName + ".gnu"));
			fw.write(gnuPlot);
			fw.close();
			System.out.print(gnuPlot);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printXML(String variable) {
		NodeComponent node = variables.get(variable);
		if (node == null) throw new RuntimeException("Invalid identifier");
		try {
			String xml = node.printXML();
			var fw = new FileWriter(new File(outFileName + ".xml"));
			fw.write(xml);
			fw.close();
			System.out.print(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
