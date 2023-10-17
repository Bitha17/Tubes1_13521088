import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class GeneticAlgorithmBot extends Bot{
    private final int POPULATION_SIZE = 4;

    public GeneticAlgorithmBot(String player) {
        super(player);
    }

    @Override
    public int[] move(String[][] gameState, int roundsLeft) {
        this.gameState = gameState;
        this.roundsLeft = roundsLeft;
        return geneticAlgorithm();
    }

    private long TIMEOUT = 5 * 1000000000;
    private int fitVal = 5;
    private int[] geneticAlgorithm(){
        int[][][] population = initializePopulation(gameState, roundsLeft);
        long startTime = System.nanoTime();
        int[] current = new int[2];
        current = population[0][0];
        float bestVal = fitnessFunction(population[0]);
        for (int[][] actions : population) {
            if (fitnessFunction(actions) > bestVal) {
                bestVal = fitnessFunction(actions);
                current = actions[0];
            }
        } 
        while (bestVal < fitVal && System.nanoTime() - startTime < TIMEOUT){
            population = Arrays.copyOf(mutatePopulation(reproducePopulation(selection(population))),population.length);
            for (int[][] actions : population) {
                if (fitnessFunction(actions) > bestVal) {
                    bestVal = fitnessFunction(actions);
                    current = actions[0];
                }
            } 
        }
        System.out.println(current[0] + " " + current[1]);
        return current;
    }

    private int[][][] crossOver(int[][] state1, int[][] state2) {
        int length = state1.length;
        
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

    private int[][][] initializePopulation(String[][] gameState, int roundsLeft) {
        roundsLeft = roundsLeft >= 6 ? 6 : roundsLeft;
        int lengthState = roundsLeft * 2;
        int[][][] population = new int[POPULATION_SIZE][lengthState][2];
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
    
        for (int k = 0; k < POPULATION_SIZE; k++) {
            for (int i = 0; i < lengthState; i++) {
                int x, y;
                String coordinate;
                do {
                    x = (int) (Math.random() * boardSize);
                    y = (int) (Math.random() * boardSize);
                    coordinate = x + "," + y;
                } while (usedCoordinates.contains(coordinate) || prohibitedCoordinates.contains(coordinate));
    
                usedCoordinates.add(coordinate);
                population[k][i][0] = x;
                population[k][i][1] = y;
            }
        }
    
        return population;
    }

    private int[][][] selection(int[][][] population) {
        float[] probabilities = new float[population.length];
        float sumFitness = 0;

        for (int i = 0; i < population.length; i++) {
            probabilities[i] = fitnessFunction(population[i]);
            sumFitness += probabilities[i];
        }

        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sumFitness;
        }

        Arrays.sort(population, (a, b) -> Float.compare(fitnessFunction(b), fitnessFunction(a)));

        int selectedCount = 0;
        int index = 0;
        Random random = new Random();
        int randomValue = random.nextInt(101);

        int[][][] selectedPopulation = new int[population.length][population[0].length][2];
        
        //check the order probability?
        for (int i = 0; i < population.length; i++) {
            if (randomValue >= selectedCount && randomValue < selectedCount + probabilities[i] * 100) {
                selectedPopulation[index] = population[i];
                index++;
            }
            selectedCount += probabilities[i] * 100;
        }

        return Arrays.copyOf(selectedPopulation, population.length);
    }

    private int[][][] mutatePopulation(int[][][] population) {
        int[][][] mutatedPopulation = new int[population.length][][];
        Random random = new Random();

        Set<String> usedCoordinates = new HashSet<>();
        for (int i = 0; i < gameState.length; i++) {
            for (int j = 0; j < gameState[i].length; j++) {
                if (gameState[i][j].equals("X") || gameState[i][j].equals("O")) {
                    usedCoordinates.add(i + "," + j);
                }
            }
        }

        Set<String> prohibitedCoordinates = new HashSet<>(Arrays.asList("0,6", "0,7", "1,6", "1,7", "6,0", "6,1", "7,0", "7,1"));

        for (int i = 0; i < population.length; i++) {
            int[][] currentState = population[i];
            int mutationIndex = random.nextInt(currentState.length);

            int[][] newState = new int[currentState.length][2];
            for (int j = 0; j < currentState.length; j++) {
                if (j == mutationIndex) {
                    // if the index is mutated
                    int newX, newY;
                    String coordinate;
                    do {
                        newX = random.nextInt(8);
                        newY = random.nextInt(8);
                        coordinate = newX + "," + newY;
                    } while (usedCoordinates.contains(coordinate) || prohibitedCoordinates.contains(coordinate));
                    usedCoordinates.add(coordinate);
                    newState[j][0] = newX;
                    newState[j][1] = newY;
                } else {
                    // the other state that not mutated
                    newState[j][0] = currentState[j][0];
                    newState[j][1] = currentState[j][1];
                }
            }

            mutatedPopulation[i] = newState;
        }

        return mutatedPopulation;
    }

    private int[][][] reproducePopulation(int[][][] selectedPopulation) {
        int[][][] offspringPopulation = new int[selectedPopulation.length][selectedPopulation[0].length][2];
        
        for (int i = 0; i < selectedPopulation.length; i += 2) {
            if (i + 1 < selectedPopulation.length) {
                // Get two parents from the selected population
                int[][] parent1 = selectedPopulation[i];
                int[][] parent2 = selectedPopulation[i + 1];
                
                // Apply crossover to create two children
                int[][][] children = crossOver(parent1, parent2);
                
                // Add the children to the offspring population
                offspringPopulation[i] = children[0];
                offspringPopulation[i + 1] = children[1];
            }
        }
        
        return offspringPopulation;
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
                val += (tempState[i][j] == "O" ? 1 : tempState[i][j] == "X" ? -1 : 0);
            }
        }
        return val;
    }
}
