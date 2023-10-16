class LocalSearchBot extends Bot{
    @Override
    public int[] move(String[][] gameState) {
        this.gameState = gameState;
        return localSearch();
    }

    private int[] localSearch() {
        int[] current = getRandomAction();
        int t = 0;
        float temperature;
        while (true) {
            t++;
            temperature = schedule(t);
            if (temperature == 0){
                System.out.println(objectiveFunction(current,"O"));
                return current;
            }
            int[] next = getRandomAction();
            float delta = objectiveFunction(next, "O") - objectiveFunction(current, "O");

            if (delta >= 0) current = next;
            else {
                if (Math.exp(delta/temperature)>0.9){
                    current = next;
                }
            }
        }
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
