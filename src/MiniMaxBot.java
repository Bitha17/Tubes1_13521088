public class MiniMaxBot extends Bot{
    private String player;
    private String enemy;
    @Override
    public int[] move(String[][] gameState, int roundsLeft) {
        this.gameState = gameState;
        this.player = "O";
        this.enemy = player == "X"? "O" : "X";
        this.roundsLeft = roundsLeft;
        return miniMax();
    }

    private int[] miniMax() {
        int[] bestAction = {0,0};
        float bestValue = Float.NEGATIVE_INFINITY;
        float alpha = Float.NEGATIVE_INFINITY;
        float beta = Float.POSITIVE_INFINITY;
        for (int[] action : getActions()) {
            float value = minValue(this.gameState, alpha, beta);
            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
            alpha = Math.max(alpha, value);
        }
        return bestAction;
    }

    private float maxValue(String[][] gameState, float alpha, float beta) {
        if (isTerminal()) {
            return objectiveFunction(player);
        }
        roundsLeft--;
        float value = Float.NEGATIVE_INFINITY;
        for (int[] action : getActions()) {
            String[][] result = result(this.gameState, action, player);
            value = Math.max(value, minValue(result , alpha, beta));
            if (value >= beta) {
                return value;
            }
            alpha = Math.max(alpha, value);
        }
        return value;
    }

    private float minValue(String[][] gameState, float alpha, float beta) {
        if (isTerminal()) {
            return objectiveFunction(player);
        }
        roundsLeft--;
        float value = Float.POSITIVE_INFINITY;
        for (int[] action : getActions()) {
            String[][] result = result(this.gameState, action, enemy);
            value = Math.min(value, maxValue(result, alpha, beta));
            if (value <= alpha) {
                return value;
            }
            beta = Math.min(beta, value);
        }
        return value;
    }

    private int[][] getActions(){
        int[][] actions = new int[64][2];
        int i = 0;
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++) {
                if (this.gameState[x][y] == "") {
                    actions[i][0] = x;
                    actions[i][1] = y;
                    i++;
                }
            }
        }
        return actions;
    }

    private String[][] result(String[][] gameState, int[] action, String player){
        String[][] newState = gameState;
        newState[action[0]][action[1]] = player;
        return newState;
    }

    private boolean isTerminal(){
        if (roundsLeft == 0) {
            return true;
        }
        else{
            int countEmpty = 0;
            for (int x = 0; x < 8; x++){
                for (int y = 0; y < 8; y++) {
                    if (this.gameState[x][y] == "") {
                        countEmpty++;
                    }
                }
            }
            return countEmpty == 0;
        }
    }
}
