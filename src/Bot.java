abstract class Bot {
    protected String[][] gameState;
    protected abstract int[] move(String[][] gameState);

    protected float objectiveFunction(int[] addedMark){
        return objectiveFunction(addedMark, "O");
    }
    protected float objectiveFunction(int[] addedMark, String player){
        // contoh simple objective func
        float val = 0;
        String enemy = player == "X"? "O" : "X";
        int x = addedMark[0];
        int y = addedMark[1];

        if (x!=0 && gameState[x-1][y] == enemy){
            val++;
        }
        if (x!=7 && gameState[x+1][y] == enemy){
            val++;
        }
        if (y!=0 && gameState[x][y-1] == enemy){
            val++;
        }
        if (y!=7 && gameState[x][y+1] == enemy){
            val++;
        }
        return val;
    }
}