package sample;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OthelloTests {
  Othello game;
  int score;

  @Before
  public void Init() {
    game = new Othello();
  }

  @Test
  public void testValidMove() {
    boolean valid = game.validMove(3, 4);
    assertEquals(false, valid);
  }
}