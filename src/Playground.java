import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class Playground {
  public static final int COLUMNS = 8;
  public static final int ROWS = 8;
  public static final int NOTILE = 0;
  public static final int DEPTH = 2;

  public Player HumanPlayer;
  public Player ComputerPlayer;
  public Player winner = null;

  public int[][] board;
  public MiniMax miniMax;

  public Playground() {
    miniMax = new MiniMax(this);

    // Player-uppställning
    HumanPlayer = new Player(Player.HUMAN, Color.WHITE);
    ComputerPlayer = new Player(Player.COMPUTER, Color.BLACK);
  }

  public void Init() {

    // Startuppställning
    board = new int[COLUMNS][ROWS];

    // Standard
    board[3][4] = ComputerPlayer.tile;
    board[4][4] = HumanPlayer.tile;
    board[4][3] = ComputerPlayer.tile;
    board[3][3] = HumanPlayer.tile;

    // Test
    // for (int x = 0; x <= 7; x++) {
    //   board[x][0] = HumanPlayer.tile;
    // }
    // for (int y = 1; y < 7; y++) {
    //   for (int x = 0; x <= 7; x++) {
    //     board[x][y] = ComputerPlayer.tile;
    //   }
    // }
  }

  public Player Opponent(Player player) {
    if (player == HumanPlayer) {
      return ComputerPlayer;
    } else {
      return HumanPlayer;
    }
  }

  // Returnera tillåtna drag, givet board och spelare
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

  // Kontrollera given riktning om draget är tillåtet för given spelare
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

      if ((_x > COLUMNS - 1) || (_x < 0) || (_y > ROWS - 1) || (_y < 0)) {
        // kört in i väggen -> sluta
        return false;
      }
      if (scoreboard[_x][_y] == Opponent(player).tile) {
        // brickan är av motståndaren -> fortsätt
        opponentHit = true;
        tilesToSwitch.add(new Position(_x, _y));
      }
      if (scoreboard[_x][_y] == player.tile && opponentHit) {
        // brickan är vår egen. Vänd våra samlade brickor och returnera true
        if (switchTiles) {
          for (Position tile : tilesToSwitch) {
            scoreboard[tile.x][tile.y] = player.tile;
          }
        }
        return true;
      }
      if (scoreboard[_x][_y] == 0 || scoreboard[_x][_y] == player.tile && !opponentHit) {
        // Ingen bricka alls eller bara egna brickor
        return false;
      }
    }

    return false;
  }

  // Är draget (pos) möjligt att göra?
  public boolean ValidMove(int[][] playBoard, Position pos, Player player) {
    if (pos.x >= COLUMNS || pos.x < 0 || pos.y >= ROWS || pos.y < 0)
      return false;

    if (playBoard[pos.x][pos.y] == 0)
      return CheckAllDirections(playBoard, pos, player, false);
    else
      return false;
  }

  // Kontrollera i alla riktningar. Om switchTiles är satt kommer alla brickor vändas också annars returneras 
  // bara om draget är giltigt.
  public boolean CheckAllDirections(int[][] playBoard, Position pos, Player player, Boolean switchTiles) {
    boolean validMove = false;
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        if (!(x == 0 && y == 0)) {
          validMove = CheckDirection(playBoard, pos, x, y, player, switchTiles) || validMove;
        }
      }
    }

    return validMove;
  }

  // Gör drag (pos) och vänd på alla brickor mellan brickan du lägger
  // och annan egen bricka, i horisontellt led, vertikalt led och snett (nio olika
  // riktningar)
  public int[][] PlayMove(int[][] playboard, Position pos, Player player) {
    playboard[pos.x][pos.y] = player.tile;
    CheckAllDirections(playboard, pos, player, true);
    return playboard;
  }

  // Spelaren har valt ett ställe att spela sin bricka. Kontrollera att det går
  // att spela draget och låt sedan datorn spela sitt drag.
  public void PlayComputerMove() {
    // Beräkna dators bästa drag
    MiniMax miniMax = new MiniMax(this);
    Position bestMove = miniMax.CalculateBestMove(ComputerPlayer, DEPTH);
    PlayMove(board, bestMove, ComputerPlayer);
  }

  public void PlayHumanMove(Position pos) {
    board = PlayMove(board, pos, HumanPlayer);
  }

  public boolean GameEnded() {
    // Finns inga platser kvar
    boolean noPlaceLeft = true;
    for (int y = 0; y < ROWS; y++) {
      if (noPlaceLeft) {
        for (int x = 0; x < COLUMNS; x++) {
          if (board[x][y] == 0) {
            noPlaceLeft = false;
            break;
          }
        }
      }
      else {
        break;
      }
    }
    if (noPlaceLeft) return true;

    // Bara vita eller bara svarta?
    for (int p = 1; p <= 2; p++) {
      boolean checkPlayer = true;
      for (int y = 0; y < ROWS; y++) {
        if (checkPlayer) {
          for (int x = 0; x < COLUMNS; x++) {
            if (board[x][y] != p && board[x][y] != 0) {
              checkPlayer = false;
              break;
            }
          }
        } else {
          break;
        }
      }

      if (checkPlayer) {
        winner = p == HumanPlayer.tile ? HumanPlayer : ComputerPlayer;
        return true;
      }
    }
    return false;
  }

  public boolean PossibleMovesExist(Player player) {
    return GetValidMoves(board, player).size() > 0;
  }

  public int CountTiles(Player player) {
    int count = 0;

    for (int y = 0; y < ROWS; y++) {
      for (int x = 0; x < COLUMNS; x++) {
        if (board[x][y] == player.tile) {
          count++;
        }
      }
    }
   
    return count;
  }
}