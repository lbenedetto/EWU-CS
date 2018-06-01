package task4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserManagerTest {

	@Test
	void testA() {
		var interceptor = new PrintStreamInterceptor();
		System.setOut(interceptor);
		ParserManager.main(new String[]{"blocks/testA/testA.blk", "testA"});
		String actual = interceptor.getString();
		String expected = "<component identifier=\"a.myHull\">\n" +
				"<size>\n" +
				"<triple x=\"6.000000\" y=\"4.000000\" z=\"3.000000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"0.000000\" y=\"0.000000\" z=\"-1.500000\"/>\n" +
				"</socket>\n" +
				"</component>\n" +
				"# component [a.myHull] {\n" +
				"# top\n" +
				"-3.000000 -2.000000 3.000000\n" +
				"3.000000 -2.000000 3.000000\n" +
				"3.000000 2.000000 3.000000\n" +
				"-3.000000 2.000000 3.000000\n" +
				"-3.000000 -2.000000 3.000000\n\n" +
				"# bottom\n" +
				"-3.000000 -2.000000 0.000000\n" +
				"3.000000 -2.000000 0.000000\n" +
				"3.000000 2.000000 0.000000\n" +
				"-3.000000 2.000000 0.000000\n" +
				"-3.000000 -2.000000 0.000000\n\n\n" +
				"# } component [a.myHull]\n";
		assertEquals(expected, actual);
	}

	@Test
	void testB() {
		var interceptor = new PrintStreamInterceptor();
		System.setOut(interceptor);
		ParserManager.main(new String[]{"blocks/testB/testB.blk", "testB"});
		String actual = interceptor.getString();
		String expected = "<component identifier=\"b.myHull\">\n" +
				"<size>\n" +
				"<triple x=\"6.000000\" y=\"4.000000\" z=\"3.000000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"0.000000\" y=\"0.000000\" z=\"-1.500000\"/>\n" +
				"</socket>\n" +
				"<connections>\n" +
				"<mount>\n" +
				"<component identifier=\"b.myTurret\">\n" +
				"<size>\n" +
				"<triple x=\"2.000000\" y=\"2.000000\" z=\"1.000000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"0.000000\" y=\"0.000000\" z=\"-0.500000\"/>\n" +
				"</socket>\n" +
				"</component>\n" +
				"<ball>\n" +
				"<triple x=\"-1.000000\" y=\"0.000000\" z=\"1.500000\"/>\n" +
				"</ball>\n" +
				"</mount>\n" +
				"</connections>\n" +
				"</component>\n" +
				"# component [b.myHull] {\n" +
				"# top\n" +
				"-3.000000 -2.000000 3.000000\n" +
				"3.000000 -2.000000 3.000000\n" +
				"3.000000 2.000000 3.000000\n" +
				"-3.000000 2.000000 3.000000\n" +
				"-3.000000 -2.000000 3.000000\n\n" +
				"# bottom\n" +
				"-3.000000 -2.000000 0.000000\n" +
				"3.000000 -2.000000 0.000000\n" +
				"3.000000 2.000000 0.000000\n" +
				"-3.000000 2.000000 0.000000\n" +
				"-3.000000 -2.000000 0.000000\n\n\n" +
				"# subcomponents {\n" +
				"# component [b.myTurret] {\n" +
				"# top\n" +
				"-2.000000 -1.000000 4.000000\n" +
				"0.000000 -1.000000 4.000000\n" +
				"0.000000 1.000000 4.000000\n" +
				"-2.000000 1.000000 4.000000\n" +
				"-2.000000 -1.000000 4.000000\n\n" +
				"# bottom\n" +
				"-2.000000 -1.000000 3.000000\n" +
				"0.000000 -1.000000 3.000000\n" +
				"0.000000 1.000000 3.000000\n" +
				"-2.000000 1.000000 3.000000\n" +
				"-2.000000 -1.000000 3.000000\n\n\n" +
				"# } component [b.myTurret]\n" +
				"# } subcomponents\n" +
				"# } component [b.myHull]\n";
		assertEquals(expected, actual);
	}

	@Test
	void testC() {
		var interceptor = new PrintStreamInterceptor();
		System.setOut(interceptor);
		ParserManager.main(new String[]{"blocks/testC/testC.blk", "testC"});
		String actual = interceptor.getString();
		String expected = "<component identifier=\"c.myHull\">\n" +
				"<size>\n" +
				"<triple x=\"6.000000\" y=\"4.000000\" z=\"3.000000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"0.000000\" y=\"0.000000\" z=\"-1.500000\"/>\n" +
				"</socket>\n" +
				"<connections>\n" +
				"<mount>\n" +
				"<component identifier=\"c.myTurret\">\n" +
				"<size>\n" +
				"<triple x=\"2.000000\" y=\"2.000000\" z=\"1.000000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"0.000000\" y=\"0.000000\" z=\"-0.500000\"/>\n" +
				"</socket>\n" +
				"<connections>\n" +
				"<mount>\n" +
				"<component identifier=\"c.myGun\">\n" +
				"<size>\n" +
				"<triple x=\"5.000000\" y=\"0.500000\" z=\"0.500000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"-2.500000\" y=\"0.000000\" z=\"0.000000\"/>\n" +
				"</socket>\n" +
				"</component>\n" +
				"<ball>\n" +
				"<triple x=\"1.000000\" y=\"0.000000\" z=\"0.000000\"/>\n" +
				"</ball>\n" +
				"</mount>\n" +
				"</connections>\n" +
				"</component>\n" +
				"<ball>\n" +
				"<triple x=\"-1.000000\" y=\"0.000000\" z=\"1.500000\"/>\n" +
				"</ball>\n" +
				"</mount>\n" +
				"</connections>\n" +
				"</component>\n" +
				"# component [c.myHull] {\n" +
				"# top\n" +
				"-3.000000 -2.000000 3.000000\n" +
				"3.000000 -2.000000 3.000000\n" +
				"3.000000 2.000000 3.000000\n" +
				"-3.000000 2.000000 3.000000\n" +
				"-3.000000 -2.000000 3.000000\n\n" +
				"# bottom\n" +
				"-3.000000 -2.000000 0.000000\n" +
				"3.000000 -2.000000 0.000000\n" +
				"3.000000 2.000000 0.000000\n" +
				"-3.000000 2.000000 0.000000\n" +
				"-3.000000 -2.000000 0.000000\n\n\n" +
				"# subcomponents {\n" +
				"# component [c.myTurret] {\n" +
				"# top\n" +
				"-2.000000 -1.000000 4.000000\n" +
				"0.000000 -1.000000 4.000000\n" +
				"0.000000 1.000000 4.000000\n" +
				"-2.000000 1.000000 4.000000\n" +
				"-2.000000 -1.000000 4.000000\n\n" +
				"# bottom\n" +
				"-2.000000 -1.000000 3.000000\n" +
				"0.000000 -1.000000 3.000000\n" +
				"0.000000 1.000000 3.000000\n" +
				"-2.000000 1.000000 3.000000\n" +
				"-2.000000 -1.000000 3.000000\n\n\n" +
				"# subcomponents {\n" +
				"# component [c.myGun] {\n" +
				"# top\n" +
				"0.000000 -0.250000 3.750000\n" +
				"5.000000 -0.250000 3.750000\n" +
				"5.000000 0.250000 3.750000\n" +
				"0.000000 0.250000 3.750000\n" +
				"0.000000 -0.250000 3.750000\n\n" +
				"# bottom\n" +
				"0.000000 -0.250000 3.250000\n" +
				"5.000000 -0.250000 3.250000\n" +
				"5.000000 0.250000 3.250000\n" +
				"0.000000 0.250000 3.250000\n" +
				"0.000000 -0.250000 3.250000\n\n\n" +
				"# } component [c.myGun]\n" +
				"# } subcomponents\n" +
				"# } component [c.myTurret]\n" +
				"# } subcomponents\n" +
				"# } component [c.myHull]\n";
		assertEquals(expected, actual);
	}

	@Test
	void testD() {
		var interceptor = new PrintStreamInterceptor();
		System.setOut(interceptor);
		ParserManager.main(new String[]{"blocks/testD/testD.blk", "testD"});
		String actual = interceptor.getString();
		String expected = "<component identifier=\"d.myHull\">\n" +
				"<size>\n" +
				"<triple x=\"6.000000\" y=\"4.000000\" z=\"3.000000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"0.000000\" y=\"0.000000\" z=\"-1.500000\"/>\n" +
				"</socket>\n" +
				"<connections>\n" +
				"<mount>\n" +
				"<component identifier=\"d.myTurret\">\n" +
				"<size>\n" +
				"<triple x=\"2.000000\" y=\"2.000000\" z=\"1.000000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"0.000000\" y=\"0.000000\" z=\"-0.500000\"/>\n" +
				"</socket>\n" +
				"<connections>\n" +
				"<mount>\n" +
				"<component identifier=\"d.myGun\">\n" +
				"<size>\n" +
				"<triple x=\"5.000000\" y=\"0.500000\" z=\"0.500000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"-2.500000\" y=\"0.000000\" z=\"0.000000\"/>\n" +
				"</socket>\n" +
				"</component>\n" +
				"<ball>\n" +
				"<triple x=\"1.000000\" y=\"0.000000\" z=\"0.000000\"/>\n" +
				"</ball>\n" +
				"</mount>\n" +
				"<mount>\n" +
				"<component identifier=\"d.mySensor\">\n" +
				"<size>\n" +
				"<triple x=\"0.500000\" y=\"0.500000\" z=\"0.500000\"/>\n" +
				"</size>\n" +
				"<socket>\n" +
				"<triple x=\"0.000000\" y=\"0.000000\" z=\"-0.250000\"/>\n" +
				"</socket>\n" +
				"</component>\n" +
				"<ball>\n" +
				"<triple x=\"-0.500000\" y=\"0.500000\" z=\"0.500000\"/>\n" +
				"</ball>\n" +
				"</mount>\n" +
				"</connections>\n" +
				"</component>\n" +
				"<ball>\n" +
				"<triple x=\"-1.000000\" y=\"0.000000\" z=\"1.500000\"/>\n" +
				"</ball>\n" +
				"</mount>\n" +
				"</connections>\n" +
				"</component>\n" +
				"# component [d.myHull] {\n" +
				"# top\n" +
				"-3.000000 -2.000000 3.000000\n" +
				"3.000000 -2.000000 3.000000\n" +
				"3.000000 2.000000 3.000000\n" +
				"-3.000000 2.000000 3.000000\n" +
				"-3.000000 -2.000000 3.000000\n\n" +
				"# bottom\n" +
				"-3.000000 -2.000000 0.000000\n" +
				"3.000000 -2.000000 0.000000\n" +
				"3.000000 2.000000 0.000000\n" +
				"-3.000000 2.000000 0.000000\n" +
				"-3.000000 -2.000000 0.000000\n\n\n" +
				"# subcomponents {\n" +
				"# component [d.myTurret] {\n" +
				"# top\n" +
				"-2.000000 -1.000000 4.000000\n" +
				"0.000000 -1.000000 4.000000\n" +
				"0.000000 1.000000 4.000000\n" +
				"-2.000000 1.000000 4.000000\n" +
				"-2.000000 -1.000000 4.000000\n\n" +
				"# bottom\n" +
				"-2.000000 -1.000000 3.000000\n" +
				"0.000000 -1.000000 3.000000\n" +
				"0.000000 1.000000 3.000000\n" +
				"-2.000000 1.000000 3.000000\n" +
				"-2.000000 -1.000000 3.000000\n\n\n" +
				"# subcomponents {\n" +
				"# component [d.myGun] {\n" +
				"# top\n" +
				"0.000000 -0.250000 3.750000\n" +
				"5.000000 -0.250000 3.750000\n" +
				"5.000000 0.250000 3.750000\n" +
				"0.000000 0.250000 3.750000\n" +
				"0.000000 -0.250000 3.750000\n\n" +
				"# bottom\n" +
				"0.000000 -0.250000 3.250000\n" +
				"5.000000 -0.250000 3.250000\n" +
				"5.000000 0.250000 3.250000\n" +
				"0.000000 0.250000 3.250000\n" +
				"0.000000 -0.250000 3.250000\n\n\n" +
				"# } component [d.myGun]\n" +
				"# component [d.mySensor] {\n" +
				"# top\n" +
				"-1.750000 0.250000 4.500000\n" +
				"-1.250000 0.250000 4.500000\n" +
				"-1.250000 0.750000 4.500000\n" +
				"-1.750000 0.750000 4.500000\n" +
				"-1.750000 0.250000 4.500000\n\n" +
				"# bottom\n" +
				"-1.750000 0.250000 4.000000\n" +
				"-1.250000 0.250000 4.000000\n" +
				"-1.250000 0.750000 4.000000\n" +
				"-1.750000 0.750000 4.000000\n" +
				"-1.750000 0.250000 4.000000\n\n\n" +
				"# } component [d.mySensor]\n" +
				"# } subcomponents\n" +
				"# } component [d.myTurret]\n" +
				"# } subcomponents\n" +
				"# } component [d.myHull]\n";
		assertEquals(expected, actual);
	}
}