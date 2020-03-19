import org.junit.Before;
import org.junit.Test;

import javafx.scene.paint.Color;

import static org.junit.Assert.*;

import java.util.List;

public class OthelloTests {
  Playground game;
  Player player;
  int score;

  @Before
  public void Init() {
    game = new Playground();
    player = new Player(3, Color.PINK);
  }

  @Test
  public void testValidMove() {
    Position pos = new Position(3, 4);
    boolean valid = game.validMove(pos, player);
    assertEquals(false, valid);
  }

  @Test
  public void AfterInit_GetValidMoves() {
    game.Init();
    List<Position> list = game.GetValidMoves(game.board, game.HumanPlayer);
    assertTrue(list != null);
  }

  @Test
  public void AfterInit_ScoreBoard() {
    game.Init();
    int score = game.calculateScore(game.board, game.HumanPlayer);
    assertTrue(score == 2);
  }

}