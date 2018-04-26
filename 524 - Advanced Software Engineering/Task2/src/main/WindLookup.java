import interpolation.Lookup3D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class WindLookup {
	private double[][][] direction;
	private double[][][] speed;
	private static double[] ALTITUDES = new double[]{0, 3000, 6000, 9000, 12000, 15000};

	private static String printCube(double[][][] cube) {
		var b = new StringBuilder();
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[i].length; j++) {
				b.append(Arrays.toString(cube[i][j])).append("\n");
			}
			b.append("\n");
		}
		return b.toString();
	}

	WindLookup(String filename) throws IOException {
		direction = new double[6][13][13];
		speed = new double[6][13][13];

		var lines = Files.readAllLines(Paths.get(filename));
		if (lines.size() != 13) throw new RuntimeException("Invalid file format");

		String[] origin = lines.remove(0).split(",");
		double startLat = Double.parseDouble(origin[0]);
		double startLng = Double.parseDouble(origin[1]);

		//Add all the data
		int lat = 1, alt, lng;
		for (String line : lines) {//all values of a given lat
			alt = 0;
			var rows = line.split(" ");
			if (rows.length != 6) throw new RuntimeException("Invalid file format");
			for (String row : rows) {//all values of a lat and alt
				lng = 1;
				var points = row.toCharArray();
				if (points.length != 12) throw new RuntimeException("Invalid file format");
				for (char point : points) {//the value of the given lat alt and lng
					int[] decoded = decodeChar(point);
					direction[alt][lat][lng] = decoded[0];
					speed[alt][lat][lng] = decoded[1];
					lng++;
				}
				alt++;
			}
			lat++;
		}
		//Add the column labels
		for (alt = 0; alt < 6; alt++) {
			speed[alt][0][0] = ALTITUDES[alt];
			for (int i = 1; i < 13; i++) {
				speed[alt][0][i] = convertToDouble(startLng, (12 - i) * 5, 0);
				direction[alt][0][i] = convertToDouble(startLng, (12 - i) * 5, 0);
				speed[alt][i][0] = convertToDouble(startLat, (12 - i) * 5, 0);
				direction[alt][i][0] = convertToDouble(startLat, (12 - i) * 5, 0);
			}
		}
	}

	public double interpolateDirection(double degreeNS,
	                                   double minuteNS,
	                                   double secondNS,
	                                   double degreeEW,
	                                   double minuteEW,
	                                   double secondEW,
	                                   double altitude) {
		double latitude = convertToDouble(degreeNS, minuteNS, secondNS);
		double longitude = convertToDouble(degreeEW, minuteEW, secondEW);
		Lookup3D lookup3D = new Lookup3D(direction);
		double result = lookup3D.resolveDependentVariable(altitude, latitude, longitude);
		return result;
	}

	public double interpolateSpeed(double degreeNS,
	                               double minuteNS,
	                               double secondNS,
	                               double degreeEW,
	                               double minuteEW,
	                               double secondEW,
	                               double altitude) {
		double latitude = convertToDouble(degreeNS, minuteNS, secondNS);
		double longitude = convertToDouble(degreeEW, minuteEW, secondEW);
		return new Lookup3D(speed).resolveDependentVariable(altitude, latitude, longitude);
	}


	private double convertToDouble(double degrees,
	                               double minutes,
	                               double seconds) {
		return degrees + (minutes / 60.0) + (seconds / 3600.0);
	}

	 static int[] decodeChar(char c) {
		if (('A' <= c && c <= 'N') || ('a' <= c && c <= 'z') || c == '.') {
			int x = (c < 'a') ? c - 'A' + 26 : c - 'a';
			if (c == '.') x = -1;

			int s = 10 + (x % 5) * 10;
			int d = x / 5 * 45;
			return new int[]{d, s};
		}
		throw new RuntimeException("Invalid char passed to decodeChar");
	}
}