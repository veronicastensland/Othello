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

// TODO:
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

public class Main extends Application {

    int colNum = 8;
    int rowNum = 8;

    int tileSize = 100;
    Color p1Color = Color.WHITE;
    Color p2Color = Color.BLACK;
    int[][] board = new int[colNum][rowNum];

    public void drawBoard(Pane gameBoard, int[][] board) {
        for (int y = 0; y < rowNum; y++) {
            for (int x = 0; x < colNum; x++) {
                Rectangle tile = new Rectangle(x*tileSize, y*tileSize, tileSize, tileSize);
                tile.setFill(null);
                tile.setStroke(Color.BLACK);

                if (board[x][y] != 0) {
                    Color playerColor = null;
                    if (board[x][y] == 1) {
                        playerColor = p1Color;
                    } else if (board[x][y] == 2) {
                        playerColor = p2Color;
                    }

                    Circle c = new Circle(x * tileSize + (tileSize/2), y * tileSize + (tileSize/2), tileSize/2);
                    c.setFill(playerColor);
                    gameBoard.getChildren().addAll(c);
                }
                gameBoard.getChildren().addAll(tile);
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception{
        // Init
        Pane gameBoard = new Pane();
        int[][] board = new int[colNum][rowNum];
        //gameBoard.setGridLinesVisible(true);

        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent e) {
                double posX = e.getX();
                double posY = e.getY();

                System.out.println("Pos x = " + posX + "\n" + "Pos y = " + posY);

                int x = (int)Math.floor(posX/100);
                int y = (int)Math.floor(posY/100);

                board[x][y] = 1;
                drawBoard(gameBoard, board);

            }
        });

        // Testuppställning
        board[3][4] = 2;
        board[4][4] = 1;
        board[4][3] = 2;
        board[3][3] = 1;

        drawBoard(gameBoard, board);


        stage.setTitle("Othello");
        Scene scene = new Scene(gameBoard, 800, 800, Color.GREEN);
        stage.setScene(scene);
        stage.show();

//        boolean abortGame = false;
//
//        while(!abortGame)
//        {
//            // Spelare 1 lägger
//            makeMove(player1);
//            // Spelare 2 lägger
//            makeMove(ai);
//
//            win = checkForWin(board);
//            drawBoard(gameBoard, board);
//        }
    }

    private boolean checkForWin(int[][] board) {
        return false;
    }
}