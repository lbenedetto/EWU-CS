public class Line {
    private Point p1;
    private Point p2;
    private int width;
    private String color;


    public Line() {
        this(0, 0, 0, 0, 1, "Black");
    }

    public Line(int x1, int y1, int x2, int y2, int w, String c) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
        width = w;
        color = c;
    }

    public Line(int x1, int y1, int x2, int y2) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
        width = 1;
        color = "Black";
    }

    public static boolean validatePoints(int x, int y) {
        return (x >= 0 && y >= 0);
    }

    public double getLength() {
        return (double)Math.round(Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2)) * 100d) / 100d;
    }

    public String toString() {
        String line1 = "Point1: " + p1.toString();
        String line2 = "Point2: " + p2.toString();
        String line3 = "Length: " + getLength();
        String line4 = "Color: " + color;
        String line5 = "Width: " + width;
        String output = line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5;
        return output;
    }

    public boolean equals(Object o) {
        Line that = (Line) o;
        return (this.width == that.width && this.p1.equals(that.p1) && this.p2.equals(that.p2) && this.getLength() == that.getLength());
    }
}
