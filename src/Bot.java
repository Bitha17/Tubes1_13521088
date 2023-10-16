abstract class Bot {
    protected String[][] gameState;
    protected int roundsLeft;
    protected abstract int[] move(String[][] gameState, int roundsLeft);

    protected float objectiveFunction(int[] addedMark){
        return objectiveFunction(addedMark, "O");
    }

    protected float objectiveFunction(int[] addedMark, String player){
        float val = 0;
        String enemy = player == "X"? "O" : "X";
        int x = addedMark[0];
        int y = addedMark[1];
        String[][] tempState = gameState;

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
        
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                val += k(tempState[i][j], player)*a(tempState[i][j], getNeighbors(tempState, i, j), player);
            }
        }

        return val;
    }

    protected float objectiveFunction(String player){
        float val = 0;
        String[][] tempState = this.gameState;
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                val += k(tempState[i][j], player)*a(tempState[i][j], getNeighbors(tempState, i, j), player);
            }
        }
        return val;
    }

    private float k(String mark, String player) {
        return ((mark.equals(player)) ? 1 : (mark.equals("")) ? -0.5f : -1);
    }
    
    private float a(String mark, String[] neighbors, String player) {
        int playerCount = 0;
        int emptyCount = 0;
    
        for (String neighbor : neighbors) {
            if (neighbor.equals("")) {
                emptyCount++;
            } else if (neighbor.equals(player)) {
                playerCount++;
            }
        }
    
        if (mark.equals("")) {
            switch (playerCount) {
                case 0:
                    return 0;
                case 1:
                    return 1.1f;
                case 2:
                    return 1.2f;
                case 3:
                    return 1.3f;
                case 4:
                    return 1.4f;
                default:
                    return 0;
            }
        } else {
            switch (emptyCount) {
                case 4:
                    return 1;
                case 3:
                    return 1.1f;
                case 2:
                    return 1.2f;
                case 1:
                    return 1.3f;
                case 0:
                    return 1.4f;
                default:
                    return 0;
            }
        }
    }
    

    private String[] getNeighbors(String[][] tempState, int i, int j) {
        String[] neighbors = new String[4];

        neighbors[0] = (j > 0) ? tempState[i][j - 1] : null; // Left neighbor
        neighbors[1] = (j < tempState[i].length - 1) ? tempState[i][j + 1] : null; // Right neighbor
        neighbors[2] = (i > 0) ? tempState[i - 1][j] : null; // Top neighbor
        neighbors[3] = (i < tempState.length - 1) ? tempState[i + 1][j] : null; // Bottom neighbor
        
        return neighbors;
    }
}