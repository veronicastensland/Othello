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
  public MiniMax miniMax;

  public Playground() {
    board = new int[COLUMNS][ROWS];
    miniMax = new MiniMax();

    // Player-uppställning
    HumanPlayer = new Player(Player.HUMAN, Color.WHITE);
    ComputerPlayer = new Player(Player.COMPUTER, Color.BLACK);
  }

  public void Init() {
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

  public Position calcBestMove(Player player, int depth) {
    int bestx = 0;
    int besty = 0;
    final int maxHeightGameTree = 10;
    Position bestMove = new Position(bestx, besty);

    List<Position> validMoves = GetValidMoves(board, player);
    int[] scores = new int[validMoves.size()];

    for (int i = 0; i < validMoves.size(); i++) {
      int[][] tempBoard = board;
      playMove(validMoves.get(i), player);
      scores[i] = calculateScore(tempBoard, player);
    }

    for (int i = 0; i < validMoves.size(); i++) {
      miniMax.minimax(depth, i, true, scores, maxHeightGameTree);
    }

    return bestMove;
  }

  List<Position> GetValidMoves(int[][] currentBoard, Player player) {
    List<Position> validMoves = new ArrayList<Position>();
    for (int y = 0; y < ROWS; y++) {
      for (int x = 0; x < COLUMNS; x++) {
        Position pos = new Position(x, y);
        if (validMove(pos, player)) {
          validMoves.add(pos);
        }
      }
    }
    return validMoves;
  }

  public boolean checkDirection(int[][] scoreboard, Position pos, int xIncStep, int yIncStep, Player player,
      Boolean switchTiles) {

    int xOffset = 0;
    int yOffset = 0;
    boolean noWallHit = true;
    boolean opponentHit = false;

    List<Position> tilesToSwitch = new ArrayList<Position>();

    while (noWallHit) {
      xOffset += xIncStep;
      yOffset += yIncStep;
      int _x = pos.x + xOffset;
      int _y = pos.y + yOffset;

      if ((_x > COLUMNS - 1) || (_x <= 0) || (_y > ROWS - 1) || (_y <= 0)) {
        // kört in i väggen -> sluta
        return false;
      }
      if (scoreboard[_x][_y] == Opponent(player).tile) {
        // brickan är av motståndaren -> fortsätt
        opponentHit = true;
        tilesToSwitch.add(new Position(_x, _y));
      }
      if (scoreboard[_x][_y] == player.tile && opponentHit) {
        // brickan är vår egen. Har vi vänt någon motståndares -> returnera true
        if (switchTiles) {
          for (Position tile : tilesToSwitch) {
            scoreboard[tile.x][tile.y] = player.tile;
          }
        }
        return true;
      }
      if (scoreboard[_x][_y] == 0 || scoreboard[_x][_y] == player.tile && !opponentHit) {
        return false;
      }
    }

    return false;
  }

  public boolean validMove(Position pos, Player player) {
    return checkAllDirections(pos, player, false);
  }

  // Kontrollerar om du får lägga på given plats
  public boolean checkAllDirections(int[][] scoreboard, Position pos, Player player, Boolean switchTiles) {
    // x-led
    for (int ii = -1; ii <= 1; ii++) {
      // y-led
      for (int jj = -1; jj <= 1; jj++) {
        if (!(ii == 0 && jj == 0)) {
          if (checkDirection(pos, ii, jj, player, switchTiles))
            return true;
        }
      }
    }

    return false;
  }

  public int playMove(int[][] scoreboard, Position pos, Player player) {

    scoreboard[pos.x][pos.y] = player.tile;
    checkAllDirections(pos, player, true);

    int score = calculateScore(board, player);
    return score;
  }

  public int calculateScore(int[][] scoreboard, Player player) {
    int score = 0;
    for (int y = 0; y < ROWS; y++) {
      for (int x = 0; x < COLUMNS; x++) {
        if (board[x][y] == player.tile) {
          score++;
        }
      }
    }
    return score;
  }

}