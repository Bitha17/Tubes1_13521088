import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class GeneticAlgorithmBot extends Bot{
    private final int POPULATION_SIZE = 100;
    
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

    private int[][] initializePopulation(String[][] gameState, int roundsLeft) {
        int[][] population = new int[roundsLeft * 4][2];
        int boardSize = 8;
    
        Set<String> usedCoordinates = new HashSet<>();
        for (int i = 0; i < gameState.length; i++) {
            for (int j = 0; j < gameState[i].length; j++) {
                if (gameState[i][j].equals("X") || gameState[i][j].equals("O")) {
                    usedCoordinates.add(i + "," + j);
                }
            }
        }
        
        Set<String> prohibitedCoordinates = new HashSet<>(Arrays.asList("0,6", "0,7", "1,6", "1,7", "6,0", "6,1", "7,0", "7,1"));
    
        for (int i = 0; i < roundsLeft * 4; i++) {
            int x, y;
            String coordinate;
            do {
                x = (int) (Math.random() * boardSize);
                y = (int) (Math.random() * boardSize);
                coordinate = x + "," + y;
            } while (usedCoordinates.contains(coordinate) || prohibitedCoordinates.contains(coordinate));
    
            usedCoordinates.add(coordinate);
            population[i][0] = x;
            population[i][1] = y;
        }
        return population;
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
