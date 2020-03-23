import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class OthelloTests {
  Playground game;
  Player player;
  int score;

  @Before
  public void Init() {
    game = new Playground();
    player = game.ComputerPlayer;
  }

  @Test
  public void testValidMove() {
    // Arrange
    Position pos = new Position(3, 4);

    // Act
    boolean valid = game.ValidMove(game.board, pos, player);

    // Should not be possible if board is empty
    assertEquals(false, valid);
  }

  @Test
  public void AfterInit_GetValidMoves() {
    game.Init();
    List<Position> list = game.GetValidMoves(game.board, player);
    assertTrue(list != null);
    assertTrue(list.size() == 4);
  }

  @Test
  public void AfterInit_ScoreBoard() {
    MiniMax miniMax = new MiniMax(game);
    game.Init();
    game.board[0][0] = game.ComputerPlayer.tile;
    int score = miniMax.CalculateScore(game.board, player);
    assertTrue(score == 3);
  }

  @Test
  public void Test_Minimax() {
    // Arrange
    MiniMax sut = new MiniMax(game);
    int depth = 1;
    game.Init();

    // Act
    int bestIndex = 0;
    int bestScore = -1;
    List<Position> validMoves = game.GetValidMoves(game.board, game.ComputerPlayer);

    for (int i = 0; i < validMoves.size(); i++) {
      // Beräkna score för givet drag
      int movescore = sut.minimax(game.board, validMoves.get(i), game.ComputerPlayer, 0, depth, true);
      if (movescore > bestScore) {
        bestScore = movescore;
        bestIndex = i;
      }
    }

    Position bestMove = validMoves.get(bestIndex);

    // Assert
    assertTrue(bestMove.x == 3 && bestMove.y == 2);
  }
}