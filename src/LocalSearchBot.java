class LocalSearchBot extends Bot{
    public LocalSearchBot(String player) {
        super(player);
    }

    @Override
    public int[] move(String[][] gameState, int roundsLeft) {
        this.gameState = gameState;
        return localSearch();
    }

    private long TIMEOUT = 5 * 1000000000;
    private int[] localSearch(){
         int[] current = getRandomAction();
         int nMax = 0;
         long startTime = System.nanoTime();
         while (nMax<=200 && System.nanoTime() - startTime < TIMEOUT){
             int[] neighbor = getRandomAction();
             if (objectiveFunction(neighbor) > objectiveFunction(current)){
                 current = neighbor;
             }
             nMax++;
         }
        return current;
    }

    private int[] getRandomAction(){
        int[] newAction = {0,0};
        do {
            newAction[0] = (int) (Math.random()*8);
            newAction[1] = (int) (Math.random()*8);
        } while (this.gameState[newAction[0]][newAction[1]] == "X" || this.gameState[newAction[0]][newAction[1]] == "O");
        return newAction;
    }

    private float schedule(int t){
        return 100-t/15;
    }
}
