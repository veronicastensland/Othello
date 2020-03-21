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
    boolean valid = game.validMove(pos, game.HumanPlayer);
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
    int score = game.calculateScore(game.board, game.ComputerPlayer);
    assertTrue(score == 3);
  }

  @Test
  public void Test_Minimax() {
    int depth = 1;
    List<Position> validMoves = game.GetValidMoves(game.board, game.HumanPlayer);
    List<Integer> validScores = new ArrayList<Integer>();

    for (Position pos : validMoves) {
      int[][] tempBoard = game.playMove(game.board, pos, game.HumanPlayer);
      int s = game.calculateScore(game.board, game.HumanPlayer);
      validScores.add(s);
    }

    MiniMax sut = new MiniMax();
    int svar = sut.minimax(depth, 0, true, validScores.stream().mapToInt(i -> i).toArray(), 1);
    assertTrue(svar > 1);
  }
}