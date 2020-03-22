import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

//==============================================
// Spelet Othella implementerat med minimax-algoritmen för att säkerställa bästa drag. 
//==============================================

// TODO Spelet hanterar inte att spelaren inte får lägga
// TODO Vad är slutkriterie för spel?

// Klassen Othello startar spelet och ansvarar för grafiken
public class Othello extends Application {
    private static final int TILE_SIZE = 80;
    private static final int WINDOWSIZE = 700;

    public void DrawBoard(Pane gameBoard, Playground playground) {
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

    // JavaFx kräver en start-metod som tar stage som parameter
    @Override
    public void start(Stage stage) {

        // Init
        Pane gameBoard = new Pane();
        Playground playground = new Playground();
        playground.Init();

        DrawBoard(gameBoard, playground);

        // Datorn inväntar ett drag från den mänskliga spelaren
        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                double posX = e.getX();
                double posY = e.getY();

                int x = (int) Math.floor(posX / TILE_SIZE);
                int y = (int) Math.floor(posY / TILE_SIZE);

                System.out.println("Mouse[x = " + posX + ", y = " + posY + "]  Pos[x = " + x + ", y = " + y + "]");
                Position pos = new Position(x, y);

                playground.TryPlayHumanMove(pos);
                // playground.board[x][y] = playground.HumanPlayer.tile;

                DrawBoard(gameBoard, playground);
            }
        });

        stage.setTitle("Othello");
        Scene scene = new Scene(gameBoard, WINDOWSIZE, WINDOWSIZE, Color.GREEN);
        stage.setScene(scene);
        stage.show();
    }

    // JavaFx förväntar sig även att main-metoden anropar launch.
    public static void main(String[] args) {
        launch(args);
    }
}