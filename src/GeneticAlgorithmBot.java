import java.util.Arrays;
import java.util.Random;

class GeneticAlgorithmBot extends Bot{
    @Override
    public int[] move(String[][] gameState, int roundsLeft) {
        this.gameState = gameState;
        this.roundsLeft = roundsLeft;
        return geneticAlgorithm();
    }

    private long TIMEOUT = 5 * 1000000000;
    private int fitVal = 5;
    private int[] geneticAlgorithm(){
        int[][][] population = initialPopulation();
        long startTime = System.nanoTime();
        int[] current = new int[2];
        current = population[0][0];
        float bestVal = fitnessFunction(population[0]);
        while (bestVal < fitVal && System.nanoTime() - startTime < TIMEOUT){
            for (int[][] actions : population) {
                if (fitnessFunction(actions) > bestVal) {
                    bestVal = fitnessFunction(actions);
                    current = actions[0];
                }
            } 
        }
        return current;
    }

    private int[][][] initialPopulation(){
        return new int[][][]{};
    }

    private int[][][] crossOver(int[][] state1, int[][] state2) {
        int length = 2 * roundsLeft;
        
        int[][] newState1 = new int[length][];
        int[][] newState2 = new int[length][];

        // Perform the crossover by swapping parts of the states
        int crossoverPoint = new Random().nextInt(length - 1) + 1; // Choose a random crossover point

        for (int i = 0; i < length; i++) {
            if (i < crossoverPoint) {
                newState1[i] = Arrays.copyOf(state1[i], state1[i].length);
                newState2[i] = Arrays.copyOf(state2[i], state2[i].length);
            } else {
                newState1[i] = Arrays.copyOf(state2[i], state2[i].length);
                newState2[i] = Arrays.copyOf(state1[i], state1[i].length);
            }
        }

        int[][][] result = new int[2][][];
        result[0] = newState1;
        result[1] = newState2;

        return result;
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
