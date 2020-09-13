import java.util.List;

public class MiniMax {

    Playground playground;
    int maxDepth = 2;
    Boolean pruning;
    Boolean showMoves;

    static int calcCount = 0;
    static int prunes = 0;

    public MiniMax(Playground pg, int md, Boolean usePruning, Boolean debugShowMoves) {
        playground = pg;
        maxDepth = md;
        pruning = usePruning;
        showMoves = debugShowMoves;

        calcCount = 0;
        prunes = 0;
    }

    // Beräkna bästa tänkbara drag för given spelare
    public Position CalculateBestMove(Player player) {
        int bestIndex = 0;
        int bestScore = -1;
        List<Position> validMoves = playground.GetValidMoves(playground.board, player);

        System.out.print("Calculate best move with depth " + maxDepth + ":");

        for (int i = 0; i < validMoves.size(); i++) {
            // Beräkna score för givet drag
            int movescore = minimax(playground.board, validMoves.get(i), player, 0, true, -1,
                    Playground.COLUMNS * Playground.ROWS);
            if (movescore > bestScore) {
                bestScore = movescore;
                bestIndex = i;
            }
        }

        Position bestMove = validMoves.get(bestIndex);
        System.out.println("\nBest score: " + bestScore + ", Best move: " + bestMove + " (Total moves investigated: "
                + calcCount + ", prunes: " + prunes + ")");

        return bestMove;
    }

    // Returns the optimal value a maximizer can obtain. depth is current depth in
    // game tree. isMax is true if current move is of maximizer, else false
    public int minimax(int[][] board, Position move, Player player, int depth, Boolean isMaximizingPlayer, int alpha,
            int beta) {

        int[][] tempBoard = DeepCopy(board);
        playground.PlayMove(tempBoard, move, player);

        // Terminating condition. i.e leaf node is reached
        if (depth == maxDepth) {
            calcCount++;
            int score = CalculateScore(tempBoard, player);
            if (showMoves) {
                System.out.print(" " + move + ":" + score);
            }
            return score;
        }

        // If current move is maximizer, find the maximum attainable value
        if (isMaximizingPlayer) {
            int bestScore = -1;
            List<Position> validMoves = playground.GetValidMoves(tempBoard, player);

            for (Position m : validMoves) {
                // Beräkna score för givet drag
                int movescore = minimax(tempBoard, m, player, depth + 1, !isMaximizingPlayer, alpha, beta);
                if (movescore > bestScore) {
                    bestScore = movescore;
                }
                // alpha = alpha > bestScore ? alpha : bestScore;
                if (alpha < bestScore) {
                    alpha = bestScore;
                }
                if (beta <= alpha && pruning) {
                    prunes++;
                    break;
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
                int movescore = minimax(tempBoard, m, player, depth + 1, !isMaximizingPlayer, alpha, beta);
                if (movescore < bestScore) {
                    bestScore = movescore;
                }
                if (beta > bestScore) {
                    beta = bestScore;
                }
                if (beta <= alpha && pruning) {
                    prunes++;
                    break;
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
                    // Ge mer score för kant eller hörn
                    if (x == 0 || x == Playground.COLUMNS - 1) {
                        score += 2;
                    }
                    if (y == 0 || y == Playground.ROWS - 1) {
                        score += 2;
                    }
                    score++;
                }
            }
        }
        return score;
    }
}
