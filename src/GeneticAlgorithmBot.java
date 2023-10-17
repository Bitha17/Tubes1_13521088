class GeneticAlgorithmBot extends Bot{
    @Override
    public int[] move(String[][] gameState, int roundsLeft) {
        this.gameState = gameState;
        this.roundsLeft = roundsLeft;
        return GeneticAlgorithm();
    }

    private long TIMEOUT = 5 * 1000000000;
    private int[] GeneticAlgorithm(){
        long startTime = System.nanoTime();
        while (System.nanoTime() - startTime < TIMEOUT){
             
        }
        return new int[]{};
    }

    private float fitnessFunction(int[][] actions){
        String[][] tempState = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempState[i][j] = gameState[i][j];
            }
        }
        int n = 0;
        for (int[] action : actions) {
            int x = action[0];
            int y = action[1];
            String player, enemy;
            if (n%2 == 0) {
                player = "O";
                enemy = "X";
            } else {
                player = "X";
                enemy = "O";
            }
            tempState[x][y] = player;
            if (x!=0 && gameState[x-1][y] == enemy){
                tempState[x-1][y] = player;
            }
            if (x!=7 && gameState[x+1][y] == enemy){
                tempState[x+1][y] = player;
            }
            if (y!=0 && gameState[x][y-1] == enemy){
                tempState[x][y-1] = player;
            }
            if (y!=7 && gameState[x][y+1] == enemy){
                tempState[x][y+1] = player;
            }
            n++;
        }
        float val = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                val += (tempState[i][j] == "O" ? 1 : -1);
            }
        }
        return val;
    }
}
