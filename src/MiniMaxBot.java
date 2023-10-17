import java.util.stream.IntStream;

public class MiniMaxBot extends Bot{
    public MiniMaxBot(String player) {
        super(player);
    }
    @Override
    public int[] move(String[][] gameState, int roundsLeft) {
//        System.out.println("minimax");
        this.gameState = gameState;
        this.roundsLeft = roundsLeft;
        return miniMax();
    }

    private long TIMEOUT = 5 * 1000000000;
    private int[] miniMax() {
        long startTime = System.nanoTime();
        int[] bestAction;
        float bestValue = Float.NEGATIVE_INFINITY;
        float alpha = Float.NEGATIVE_INFINITY;
        float beta = Float.POSITIVE_INFINITY;
        roundsLeft--;
        int[][] actions = getActions(this.gameState);
        bestAction = actions[0];
        for (int i = 0; i < actions.length; i++) {
//            System.out.println("action " + actions[i][0] + " " + actions[i][1]);
            if(System.nanoTime() - startTime >= TIMEOUT){
//                System.out.println("timeout " + bestAction[0] + " " + bestAction[1]);
                return bestAction;
            }
            String[][] result = result(this.gameState, actions[i], player);
            float value = minValue(result, alpha, beta);
            if (value > bestValue) {
                bestValue = value;
                bestAction = actions[i];
            }
            alpha = Math.max(alpha, value);
        }
//        System.out.println(bestAction[0] + " " + bestAction[1]);
//        System.out.println("best val: " + bestValue);
        return bestAction;
    }

    private float maxValue(String[][] gameState, float alpha, float beta) {
        if (isTerminal(gameState)) {
//            System.out.println("max Val: " + objectiveFunction(gameState, player));
            return objectiveFunction(gameState, player);
        }
        roundsLeft--;
        float value = Float.NEGATIVE_INFINITY;
        for (int[] action : getActions(gameState)) {
            String[][] result = result(gameState, action, player);
            value = Math.max(value, minValue(result , alpha, beta));
            if (value >= beta) {
                return value;
            }
            alpha = Math.max(alpha, value);
        }
//        System.out.println("max Val: " + value);
        return value;
    }

    private float minValue(String[][] gameState, float alpha, float beta) {
        if (isTerminal(gameState)) {
//            System.out.println("min Val: " + objectiveFunction(gameState, player));
            return objectiveFunction(gameState, player);
        }
        roundsLeft--;
        float value = Float.POSITIVE_INFINITY;
        for (int[] action : getActions(gameState)) {
            String[][] result = result(gameState, action, enemy);
            value = Math.min(value, maxValue(result, alpha, beta));
            if (value <= alpha) {
                return value;
            }
            beta = Math.min(beta, value);
        }
//        System.out.println("min Val: " + value);
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


    private boolean isTerminal(String[][] gameState){
        if (roundsLeft == 0) {
            return true;
        }
        else{
            int countEmpty = 0;
            for (int x = 0; x < 8; x++){
                for (int y = 0; y < 8; y++) {
                    if (gameState[x][y].equals("")) {
                        countEmpty++;
                    }
                }
            }
            return countEmpty == 0;
        }
    }
}
