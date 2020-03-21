import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class OthelloTests {
  Playground game;
  Player player;
  int score;

  @Before
  public void Init() {
    game = new Playground();
    // player = new Player(3, Color.PINK);
  }

  @Test
  public void testValidMove() {
    Position pos = new Position(3, 4);
    boolean valid = game.ValidMove(game.board, pos, player);
    assertEquals(false, valid);
  }

  @Test
  public void AfterInit_GetValidMoves() {
    game.Init();
    List<Position> list = game.GetValidMoves(game.board, game.HumanPlayer);
    assertTrue(list != null);
    assertTrue(list.size() == 4);
  }

  @Test
  public void AfterInit_ScoreBoard() {
    game.Init();
    game.board[0][0] = game.ComputerPlayer.tile;
    int score = game.CalculateScore(game.board, game.ComputerPlayer);
    assertTrue(score == 3);
  }

  @Test
  public void Test_Minimax_Drivercode() {
    // The number of elements in scores must be a power of 2.
    int scores[] = { 3, 5, 2, 9, 12, 5, 23, 23 };
    int n = scores.length;
    MiniMax calc = new MiniMax();
    int h = MiniMax.log2(n);
    int res = calc.minimax(0, 0, true, scores, h);
    assertTrue(res == 12);
  }

  @Test
  public void Test_Minimax() {
    // Arrange
    MiniMax sut = new MiniMax();
    int depth = 1;
    game.Init();

    // Act
    List<Position> validMoves = game.GetValidMoves(game.board, game.HumanPlayer);
    List<Integer> validScores = new ArrayList<Integer>();

    for (Position pos : validMoves) {
      int[][] tempBoard = game.PlayMove(game.board, pos, game.HumanPlayer);
      int s = game.CalculateScore(tempBoard, game.HumanPlayer);
      validScores.add(s);
    }

    int scores[] = validScores.stream().mapToInt(i -> i).toArray();
    int h = MiniMax.log2(scores.length);
    int svarIndex = sut.minimax(depth, 0, true, validScores.stream().mapToInt(i -> i).toArray(), h);

    Position bestMove = validMoves.toArray(new Position[validMoves.size()])[svarIndex];

    // Assert
    assertTrue(bestMove.x == 3 && bestMove.y == 5);
  }
}