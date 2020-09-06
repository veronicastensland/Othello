import java.util.List;

public class MiniMax {

    Playground playground;
    static int calcCount = 0;
    int maxDepth = 2;

    public MiniMax(Playground pg, int md) {
        playground = pg;
        calcCount = 0;
        maxDepth = md;
    }

    // Beräkna bästa tänkbara drag för given spelare
    public Position CalculateBestMove(Player player) {
        int bestIndex = 0;
        int bestScore = -1;
        List<Position> validMoves = playground.GetValidMoves(playground.board, player);

        System.out.print("Calculate best move with depth " + maxDepth + ":");

        for (int i = 0; i < validMoves.size(); i++) {
            // Beräkna score för givet drag
            int movescore = minimax(playground.board, validMoves.get(i), player, 0, true);
            if (movescore > bestScore) {
                bestScore = movescore;
                bestIndex = i;
            }
        }

        Position bestMove = validMoves.get(bestIndex);
        System.out.println(
                "\nBest score: " + bestScore + " Best move: " + bestMove + " Total moves investigated: " + calcCount);

        return bestMove;
    }

    // Returns the optimal value a maximizer can obtain. depth is current depth in
    // game tree. isMax is true if current move is of maximizer, else false
    public int minimax(int[][] board, Position move, Player player, int h, Boolean isMaximizingPlayer) {

        int[][] tempBoard = DeepCopy(board);
        playground.PlayMove(tempBoard, move, player);

        // Terminating condition. i.e leaf node is reached
        if (h == maxDepth) {
            calcCount++;
            int score = CalculateScore(tempBoard, player);
            System.out.print(" " + move + ":" + score);
            return score;
        }

        // If current move is maximizer, find the maximum attainable value
        if (isMaximizingPlayer) {
            int bestScore = -1;
            List<Position> validMoves = playground.GetValidMoves(tempBoard, player);

            for (Position m : validMoves) {
                // Beräkna score för givet drag
                int movescore = minimax(tempBoard, m, player, h + 1, !isMaximizingPlayer);
                if (movescore > bestScore) {
                    bestScore = movescore;
                }
            }
            return bestScore;
        }
        // Else (If current move is Minimizer), find the minimum attainable value
        else {
            int bestScore = Playground.COLUMNS * Playground.ROWS;
            List<Position> validMoves = playground.GetValidMoves(tempBoard, playground.Opponent(player));

            for (Position m : validMoves) {
                // Beräkna score för givet drag
                int movescore = minimax(tempBoard, m, player, h + 1, !isMaximizingPlayer);
                if (movescore < bestScore) {
                    bestScore = movescore;
                }
            }
            return bestScore;
        }
    }

    // Kopiera arrayen med alla värden
    private int[][] DeepCopy(int[][] deepBoard) {
        int[][] deepCopy = new int[Playground.COLUMNS][Playground.ROWS];
        for (int y = 0; y < Playground.ROWS; y++) {
            for (int x = 0; x < Playground.COLUMNS; x++) {
                deepCopy[x][y] = deepBoard[x][y];
            }
        }
        return deepCopy;
    }

    // Beräkna score för given spelare och board
    public int CalculateScore(int[][] scoreboard, Player player) {
        int score = 0;
        for (int y = 0; y < Playground.ROWS; y++) {
            for (int x = 0; x < Playground.COLUMNS; x++) {
                if (scoreboard[x][y] == player.tile) {
                    score++;
                }
            }
        }
        return score;
    }
}
