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

    // Standard
    board[3][4] = ComputerPlayer.tile;
    board[4][4] = HumanPlayer.tile;
    board[4][3] = ComputerPlayer.tile;
    board[3][3] = HumanPlayer.tile;

    // for (int x = 1; x < 3; x++) {
    // board[x][4] = ComputerPlayer.tile;
    // }

    // for (int y = 1; y < 3; y++) {
    // board[3][y] = ComputerPlayer.tile;
    // }
  }

  public Player Opponent(Player player) {
    if (player == HumanPlayer) {
      return ComputerPlayer;
    } else {
      return HumanPlayer;
    }
  }

  public Position CalcBestMove(Player player, int depth) {
    List<Position> validMoves = GetValidMoves(board, player);
    int[] validScores = new int[validMoves.size()];

    for (int i = 0; i < validMoves.size(); i++) {
      int[][] tempBoard = DeepCopy(board);
      PlayMove(tempBoard, validMoves.get(i), player);
      validScores[i] = CalculateScore(tempBoard, player);
    }

    MiniMax calc = new MiniMax();
    Position bestMove = calc.calculateBestMove(depth, validScores, validMoves);
    return bestMove;
  }

  private int[][] DeepCopy(int[][] deepBoard) {
    int[][] deepCopy = new int[COLUMNS][ROWS];
    for (int y = 0; y < ROWS; y++) {
      for (int x = 0; x < COLUMNS; x++) {
        deepCopy[x][y] = deepBoard[x][y];
      }
    }
    return deepCopy;
  }

  List<Position> GetValidMoves(int[][] currentBoard, Player player) {
    List<Position> validMoves = new ArrayList<Position>();
    for (int y = 0; y < ROWS; y++) {
      for (int x = 0; x < COLUMNS; x++) {
        Position pos = new Position(x, y);
        if (ValidMove(currentBoard, pos, player)) {
          validMoves.add(pos);
        }
      }
    }
    return validMoves;
  }

  public boolean CheckDirection(int[][] scoreboard, Position pos, int xIncStep, int yIncStep, Player player,
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

  public boolean ValidMove(int[][] playBoard, Position pos, Player player) {
    return CheckAllDirections(playBoard, pos, player, false);
  }

  // Kontrollerar om du får lägga på given plats
  public boolean CheckAllDirections(int[][] playBoard, Position pos, Player player, Boolean switchTiles) {
    // x-led
    for (int ii = -1; ii <= 1; ii++) {
      // y-led
      for (int jj = -1; jj <= 1; jj++) {
        if (!(ii == 0 && jj == 0)) {
          if (CheckDirection(playBoard, pos, ii, jj, player, switchTiles))
            return true;
        }
      }
    }

    return false;
  }

  public int[][] PlayMove(int[][] playboard, Position pos, Player player) {
    playboard[pos.x][pos.y] = player.tile;
    CheckAllDirections(playboard, pos, player, true);
    return playboard;
  }

  public int CalculateScore(int[][] scoreboard, Player player) {
    int score = 0;
    for (int y = 0; y < ROWS; y++) {
      for (int x = 0; x < COLUMNS; x++) {
        if (scoreboard[x][y] == player.tile) {
          score++;
        }
      }
    }
    return score;
  }

}