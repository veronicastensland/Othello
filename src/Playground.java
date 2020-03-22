import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Playground {
  public static int COLUMNS = 8;
  public static int ROWS = 8;
  public static int NOTILE = 0;
  public static int DEPTH = 1;

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

    for (int x = 1; x < 3; x++) {
      board[x][4] = ComputerPlayer.tile;
    }

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

  // Beräkna bästa tänkbara drag för given spelare
  public Position CalcBestMove(Player player, int depth) {
    List<Position> validMoves = GetValidMoves(board, player);
    int[] validScores = new int[validMoves.size()];

    // Beräkna score för givet drag
    for (int i = 0; i < validMoves.size(); i++) {
      int[][] tempBoard = DeepCopy(board);
      PlayMove(tempBoard, validMoves.get(i), player);
      validScores[i] = CalculateScore(tempBoard, player);
    }

    Position bestMove = CalculateBestMove(depth, validScores, validMoves);
    return bestMove;
  }

  // Return index of best move
  public Position CalculateBestMove(int depth, int validScores[], List<Position> validMoves) {
    int h = MiniMax.log2(validScores.length);
    int optimal = minimax(depth, 0, true, validScores, h);
    for (int i = 0; i < validScores.length; i++) {
      if (validScores[i] == optimal)
        return validMoves.get(i);
    }
    return new Position(-1, -1);
  }

  // Kopiera arrayen med alla värden
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

  // Är draget (pos) möjligt att göra?
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

  // Gör drag (pos) och vänd på alla brickor mellan brickan du lägger
  // och annan egen bricka, i horisontellt led, vertikalt led och snett (nio olika
  // riktningar)
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

  // Spelaren har valt ett ställe att spela sin bricka
  public void TryPlayMove(Pane gameBoard, Position pos) {
    if (ValidMove(board, pos, HumanPlayer)) {
      // Spela spelarens drag
      PlayMove(board, pos, HumanPlayer);
      // Beräkna dators bästa drag
      Position bestMove = CalcBestMove(ComputerPlayer, DEPTH);
      PlayMove(board, bestMove, ComputerPlayer);
    }
  }

  // Returns the optimal value a maximizer can obtain.
  // depth is current depth in game tree.
  // nodeIndex is index of current node in scores[].
  // isMax is true if current move is of maximizer, else false
  // scores[] stores leaves of Game tree.
  // h is maximum height of Game tree
  public int minimax(int depth, int nodeIndex, boolean isMax, int scores[], int h) {
    // Terminating condition. i.e leaf node is reached
    if (depth == h)
      return scores[nodeIndex];

    // If current move is maximizer, find the maximum attainable value
    if (isMax)
      return Math.max(minimax(depth + 1, nodeIndex * 2, false, scores, h),
          minimax(depth + 1, nodeIndex * 2 + 1, false, scores, h));

    // Else (If current move is Minimizer), find the minimum attainable value
    else
      return Math.min(minimax(depth + 1, nodeIndex * 2, true, scores, h),
          minimax(depth + 1, nodeIndex * 2 + 1, true, scores, h));
  }

}