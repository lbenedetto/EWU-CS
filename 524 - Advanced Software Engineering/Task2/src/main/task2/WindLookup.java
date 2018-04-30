package task2;

import task2.interpolation.Lookup3D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WindLookup {
	private double[][][] direction;
	private double[][][] speed;
	private static double[] ALTITUDES = new double[]{
			0,
			3000,
			6000,
			9000,
			12000,
			15000
	};

	public WindLookup(String filename) throws IOException {
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
			speed[alt][0][0] = ALTITUDES[ALTITUDES.length - alt - 1];
			direction[alt][0][0] = speed[alt][0][0];
			for (int i = 1; i < 13; i++) {
				speed[alt][0][i] = convertToDecimal(startLng, (12 - i) * 5, 0);
				speed[alt][i][0] = convertToDecimal(startLat, (12 - i) * 5, 0);
				direction[alt][0][i] = speed[alt][0][i];
				direction[alt][i][0] = speed[alt][i][0];
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
		double latitude = convertToDecimal(degreeNS, minuteNS, secondNS);
		double longitude = convertToDecimal(degreeEW, minuteEW, secondEW);
		Lookup3D lookup3D = new Lookup3D(direction);
		return lookup3D.resolveDependentVariable(altitude, latitude, longitude);
	}

	public double interpolateSpeed(double degreeNS,
	                               double minuteNS,
	                               double secondNS,
	                               double degreeEW,
	                               double minuteEW,
	                               double secondEW,
	                               double altitude) {
		double latitude = convertToDecimal(degreeNS, minuteNS, secondNS);
		double longitude = convertToDecimal(degreeEW, minuteEW, secondEW);
		return new Lookup3D(speed).resolveDependentVariable(altitude, latitude, longitude);
	}


	private double convertToDecimal(double degrees,
	                                double minutes,
	                                double seconds) {
		return degrees + (minutes / 60.0) + (seconds / 3600.0);
	}

	private static int[] decodeChar(char c) {
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