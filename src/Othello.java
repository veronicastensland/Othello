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
- TODO Implementera makeMove
    Den ska göra ett drag och vända på alla brickor mellan brickan du lägger
    och annan egen bricka, i horisontellt led, vertikalt led och snett (nio olika riktningar)
- TODO Beräkna bästa drag enligt minimax-algoritmen
- TDOO Implementera evaluateBoard
    Största antal möjliga brickor att vända = bäst. Å andra sidan, kanske dåligt.
    Man bör istället dela in dom i frontier discs och interior dics,
    sedan också ge vissa platser på planen en högre score.
    Hörn = superbra
- TODO validMove
    Håller reda på om draget är möjligt att göra.
- Beginner, intermediate, or expert? *INTE PRIORITET*
 */

public class Othello extends Application {
    private static final int TILE_SIZE = 100;
    private static int LEVEL = 1;

    public void drawBoard(Pane gameBoard, Playground playground) {
        for (int y = 0; y < Playground.ROWS; y++) {
            for (int x = 0; x < Playground.COLUMNS; x++) {
                Rectangle tile = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                tile.setFill(null);
                tile.setStroke(Color.BLACK);

                if (playground.board[x][y] != Playground.NOTILE) {

                    Color playerColor = null;
                    if (playground.board[x][y] == playground.HumanPlayer.tile) {
                        playerColor = playground.HumanPlayer.color;
                    } else if (playground.board[x][y] == playground.ComputerPlayer.tile) {
                        playerColor = playground.ComputerPlayer.color;
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
        Playground playground = new Playground();
        playground.Init();

        drawBoard(gameBoard, playground);

        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                double posX = e.getX();
                double posY = e.getY();

                System.out.println("Pos x = " + posX + "\n" + "Pos y = " + posY);

                int x = (int) Math.floor(posX / 100);
                int y = (int) Math.floor(posY / 100);
                Position pos = new Position(x, y);

                if (playground.validMove(pos, playground.HumanPlayer)) {
                    playground.playMove(playground.board, pos, playground.HumanPlayer);

                    Position bestMove = playground.calcBestMove(playground.ComputerPlayer, LEVEL);
                    playground.playMove(playground.board, bestMove, playground.ComputerPlayer);

                    drawBoard(gameBoard, playground);
                }
            }
        });

        stage.setTitle("Othello");
        Scene scene = new Scene(gameBoard, 800, 800, Color.GREEN);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}