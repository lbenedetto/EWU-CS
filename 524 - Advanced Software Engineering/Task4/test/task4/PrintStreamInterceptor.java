package task4;

import java.io.PrintStream;

public class PrintStreamInterceptor extends PrintStream {
	private StringBuilder out;

	PrintStreamInterceptor() {
		super(System.out);
		out = new StringBuilder();
	}

	@Override
	public void print(String x) {
		out.append(x);
	}

	@Override
	public void println(String x) {
		out.append(x);
		out.append("\n");
	}

	String getString() {
		String o = out.toString();
		out = new StringBuilder();
		return o;
	}
}