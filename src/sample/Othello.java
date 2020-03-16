package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/*
- Implementera makeMove
    Den ska göra ett drag och vända på alla brickor mellan brickan du lägger
    och annan egen bricka, i horisontellt led, vertikalt led och snett (nio olika riktningar)
- Beräkna bästa drag enligt minimax-algoritmen
- Implementera evaluateBoard
    Största antal möjliga brickor att vända = bäst. Å andra sidan, kanske dåligt.
    Man bör istället dela in dom i frontier discs och interior dics,
    sedan också ge vissa platser på planen en högre score.
    Hörn = superbra
- validMove
    Håller reda på om draget är möjligt att göra.
    Beginner, intermediate, or expert? *INTE PRIORITET*
 */

public class Othello extends Application {
    private static final int COLUMNS = 8;
    private static final int ROWS = 8;
    private static final int TILE_SIZE = 100;
    private int PLAYER1 = 1;
    private int PLAYER2 = 2;

    Color p1Color = Color.WHITE;
    Color p2Color = Color.BLACK;
    int[][] board = new int[COLUMNS][ROWS];

    public void drawBoard(Pane gameBoard, int[][] board) {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Rectangle tile = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                tile.setFill(null);
                tile.setStroke(Color.BLACK);

                if (board[x][y] != 0) {
                    Color playerColor = null;
                    if (board[x][y] == PLAYER1) {
                        playerColor = p1Color;
                    } else if (board[x][y] == PLAYER2) {
                        playerColor = p2Color;
                    }

                    Circle c = new Circle(x * TILE_SIZE + (TILE_SIZE / 2), y * TILE_SIZE + (TILE_SIZE / 2),
                            TILE_SIZE / 2);
                    c.setFill(playerColor);
                    gameBoard.getChildren().addAll(c);
                }
                gameBoard.getChildren().addAll(tile);
            }
        }
    }

    @Override
    public void start(Stage stage) {
        // Init
        Pane gameBoard = new Pane();
        board = new int[COLUMNS][ROWS];

        // Startuppställning
        board[3][4] = PLAYER2;
        board[4][4] = PLAYER1;
        board[4][3] = PLAYER2;
        board[3][3] = PLAYER1;

        drawBoard(gameBoard, board);

        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                double posX = e.getX();
                double posY = e.getY();

                System.out.println("Pos x = " + posX + "\n" + "Pos y = " + posY);

                int x = (int) Math.floor(posX / 100);
                int y = (int) Math.floor(posY / 100);

                if (validMove(x, y)) {
                    int xx = 0, yy = 0;
                    board[x][y] = PLAYER1;
                    calcBestMove(xx, yy);
                    board[xx][yy] = PLAYER2;
                    drawBoard(gameBoard, board);
                }
            }
        });

        stage.setTitle("Othello");
        Scene scene = new Scene(gameBoard, 800, 800, Color.GREEN);
        stage.setScene(scene);
        stage.show();
    }

    private void calcBestMove(int xx, int yy) {
        int bestx = 0;
        int besty = 0;

        // ValidMoves
        // bestMove = MiniMax(ValidMoves);

        xx = bestx;
        yy = besty;
    }

    public boolean checkDirection(int x, int y, int ii, int jj) {
        int i = 0;
        int j = 0;
        boolean noWallHit = true;
        boolean blackHit = false;

        while (noWallHit) {
            i += ii;
            j += jj;
            int _x = x + i;
            int _y = y + j;

            if ((_x > COLUMNS - 1) || (_x <= 0) || (_y > ROWS - 1) || (_y <= 0)) {
                return false;
            }
            if (board[_x][_y] == PLAYER2) {
                blackHit = true;
            }
            if (board[_x][_y] == PLAYER1 && blackHit) {
                return true;
            }
            if (board[_x][_y] == 0 || board[_x][_y] == PLAYER1 && !blackHit) {
                return false;
            }
        }

        return false;
    }

    // Om du kan vända någon bricka
    public boolean validMove(int x, int y) {

        for (int ii = -1; ii <= 1; ii++) {
            for (int jj = -1; jj <= 1; jj++) {
                if (!(ii == 0 && jj == 0)) {
                    if (checkDirection(x, y, ii, jj))
                        return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}