class LocalSearchBot implements Bot{
    private String[][] gameState;
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

    private float objectiveFunction(int[] addedMark, String player){
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
    };

}
