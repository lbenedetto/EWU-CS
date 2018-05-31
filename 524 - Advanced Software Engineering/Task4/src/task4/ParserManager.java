package task4;

import task4.node.NodeComponent;
import task4.node.NodeTriple;
import task4.parser.MvcParser;

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.util.HashMap;

public class ParserManager {
	private static HashMap<String, NodeComponent> components = new HashMap<>();
	private static HashMap<String, NodeComponent> variables = new HashMap<>();
	private static String outFile;

	public static void main(String[] args) {
		if (args.length != 2) {
			throw new RuntimeException("usage: input_filename output_filename ");
		}
		try {
			outFile = args[1];
			MvcParser mvcParser = new MvcParser(args[0], args[1]);
			mvcParser.parse();
		} catch (Exception e) {

		}
	}

	public static void defineVariable(String variableName, NodeComponent nodeComponent) {
		variables.put(variableName, nodeComponent);
	}

	public static void addComponent(NodeComponent nodeComponent) {
		components.put(nodeComponent.getIdentifier(), nodeComponent);
	}

	public static void exportToGnuplot(String variable, NodeTriple triple) {
		NodeComponent node = variables.get(variable);
		if(node == null) throw new RuntimeException("Invalid identifier");
		try {
			String s = node.exportToGnuplot();
			FileWriter fw = new FileWriter(new File(outFile));
			fw.write(s);
			fw.close();
		}catch (Exception e){

		}
	}

	public static void printXML(String variable){
		NodeComponent node = variables.get(variable);
		if(node == null) throw new RuntimeException("Invalid identifier");
		String xml = node.printXML();
		System.out.print(xml);
	}
}
