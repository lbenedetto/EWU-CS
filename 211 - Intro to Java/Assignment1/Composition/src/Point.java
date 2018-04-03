public class Point {
    private int x;
    private int y;

    public Point(int xcord, int ycord) {
        x = xcord;
        y = ycord;
    }

    public Point() {
        x = 0;
        y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public void setXY(int newX, int newY) {
        setY(newY);
        setX(newX);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object o) {
        if (o.getClass().getSimpleName().equals(this.getClass().getSimpleName())) {
            Point that = (Point) o;
            return this.getX() == that.getX() && this.getY() == that.getY();
        }
        return false;
    }
}
