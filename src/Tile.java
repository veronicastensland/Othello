import javafx.scene.paint.Color;

public class Tile {
  public int x;
  public int y;
  public Color c;

  public Tile(int _x, int _y) {
    x = _x;
    y = _y;
  }

  public Tile(int _x, int _y, Color _c) {
    x = _x;
    y = _y;
    c = _c;
  }
}