import javafx.scene.paint.Color;

public class Player {

  public int current;
  public Color color;

  public static final int HUMAN = 1;
  public static final int COMPUTER = 2;

  public Player(int player, Color initColor) {
    current = player;
    color = initColor;
  }
}