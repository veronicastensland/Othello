public class Position {
  public int x;
  public int y;

  public Position(int _x, int _y) {
    x = _x;
    y = _y;
  }

  @Override
  public String toString() {
    return String.format("(" + x + ", " + y + ")");
  }
}