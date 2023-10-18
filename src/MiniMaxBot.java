import java.util.stream.IntStream;

public class MiniMaxBot extends Bot{
    private long startTime;
    private long TIMEOUT = 5 * 1000000000;

    public MiniMaxBot(String player) {
        super(player);
    }
    @Override
    public int[] move(String[][] gameState, int roundsLeft) {
        this.gameState = gameState;
        this.roundsLeft = roundsLeft;
        this.startTime = System.nanoTime();
        return miniMax();
    }

    private int[] miniMax() {
        int[] bestAction;
        float bestValue = Float.NEGATIVE_INFINITY;
        float alpha = Float.NEGATIVE_INFINITY;
        float beta = Float.POSITIVE_INFINITY;
        int[][] actions = getActions(this.gameState);
        bestAction = actions[0];
        for (int i = 0; i < actions.length; i++) {
            String[][] result = result(this.gameState, actions[i], player);
            float value = minValue(result, alpha, beta, roundsLeft - 1);
            if (value > bestValue) {
                bestValue = value;
                bestAction = actions[i];
            }
            alpha = Math.max(alpha, value);
        }
        return bestAction;
    }

    private float maxValue(String[][] gameState, float alpha, float beta, int roundsLeft) {
        if (roundsLeft == 0 || System.nanoTime() - startTime >= TIMEOUT) {
            return objectiveFunction(gameState, player);
        }
        float value = Float.NEGATIVE_INFINITY;
        for (int[] action : getActions(gameState)) {
            String[][] result = result(gameState, action, player);
            value = Math.max(value, minValue(result , alpha, beta, roundsLeft - 1));
            if (value >= beta) {
                return value;
            }
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    private float minValue(String[][] gameState, float alpha, float beta, int roundsLeft) {
        if (roundsLeft == 0 || System.nanoTime() - startTime >= TIMEOUT) {
            return objectiveFunction(gameState, player);
        }
        float value = Float.POSITIVE_INFINITY;
        for (int[] action : getActions(gameState)) {
            String[][] result = result(gameState, action, enemy);
            value = Math.min(value, maxValue(result, alpha, beta, roundsLeft - 1));
            if (value <= alpha) {
                return value;
            }
            beta = Math.min(beta, value);
        }
        return value;
    }


    private int[][] getActions(String[][] gameState) {
        return IntStream.range(0, 8)
                .boxed()
                .flatMap(x -> IntStream.range(0, 8)
                        .filter(y -> gameState[x][y].equals(""))
                        .mapToObj(y -> new int[]{x, y})
                )
                .toArray(int[][]::new);
    }

    private String[][] result(String[][] gameState, int[] action, String p) {
        String e = p == "X"? "O":"X";
        String[][] newState = new String[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                newState[x][y] = gameState[x][y];
            }
        }
        int x = action[0];
        int y = action[1];
        newState[x][y] = p;
        if (x!=0 && gameState[x-1][y].equals(e)){
            newState[x-1][y] = p;
        }
        if (x!=7 && gameState[x+1][y].equals(e)){
            newState[x+1][y] = p;
        }
        if (y!=0 && gameState[x][y-1].equals(e)){
            newState[x][y-1] = p;
        }
        if (y!=7 && gameState[x][y+1].equals(e)){
            newState[x][y+1] = p;
        }
        return newState;
    }
}
