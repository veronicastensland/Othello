import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class Playground {
  public static int COLUMNS = 8;
  public static int ROWS = 8;
  public static int NOTILE = 0;

  public Player HumanPlayer;
  public Player ComputerPlayer;

  public int[][] board;

  public Playground() {
    board = new int[COLUMNS][ROWS];
  }

  public void Init() {
    // Player-uppställning
    HumanPlayer = new Player(Player.HUMAN, Color.WHITE);
    ComputerPlayer = new Player(Player.COMPUTER, Color.BLACK);

    // Startuppställning
    board[3][4] = ComputerPlayer.tile;
    board[4][4] = HumanPlayer.tile;
    board[4][3] = ComputerPlayer.tile;
    board[3][3] = HumanPlayer.tile;
  }

  public Player Opponent(Player player) {
    if (player == HumanPlayer) {
      return ComputerPlayer;
    } else {
      return HumanPlayer;
    }
  }

  public Position calcBestMove(Player player) {
    int bestx = 0;
    int besty = 0;
    Position bestMove = new Position(bestx, besty);

    // TODO GetValidMoves
    List<Position> validMoves = new ArrayList<Position>();
    // TODO bestMove = MiniMax(ValidMoves);

    return bestMove;
  }

  public boolean checkDirection(int x, int y, int xIncStep, int yIncStep, Player player, Boolean switchTiles) {

    int xOffset = 0;
    int yOffset = 0;
    boolean noWallHit = true;
    boolean opponentHit = false;

    List<Position> tilesToSwitch = new ArrayList<Position>();

    while (noWallHit) {
      xOffset += xIncStep;
      yOffset += yIncStep;
      int _x = x + xOffset;
      int _y = y + yOffset;

      if ((_x > COLUMNS - 1) || (_x <= 0) || (_y > ROWS - 1) || (_y <= 0)) {
        // kört in i väggen -> sluta
        return false;
      }
      if (board[_x][_y] == Opponent(player).tile) {
        // brickan är av motståndaren -> fortsätt
        opponentHit = true;
        tilesToSwitch.add(new Position(_x, _y));
      }
      if (board[_x][_y] == player.tile && opponentHit) {
        // brickan är vår egen. Har vi vänt någon motståndares -> returnera true
        if (switchTiles) {
          for (Position tile : tilesToSwitch) {
            board[tile.x][tile.y] = player.tile;
          }
        }
        return true;
      }
      if (board[_x][_y] == 0 || board[_x][_y] == player.tile && !opponentHit) {
        return false;
      }
    }

    return false;
  }

  public boolean validMove(int x, int y, Player player) {
    return checkAllDirections(x, y, player, false);
  }

  // Kontrollerar om du får lägga på given plats
  public boolean checkAllDirections(int x, int y, Player player, Boolean switchTiles) {
    // x-led
    for (int ii = -1; ii <= 1; ii++) {
      // y-led
      for (int jj = -1; jj <= 1; jj++) {
        if (!(ii == 0 && jj == 0)) {
          if (checkDirection(x, y, ii, jj, player, switchTiles))
            return true;
        }
      }
    }

    return false;
  }

  public void playMove(int x, int y, Player player) {
    board[x][y] = player.tile;
    checkAllDirections(x, y, player, true);
  }

}