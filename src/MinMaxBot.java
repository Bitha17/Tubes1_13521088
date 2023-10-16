class MinMaxBot extends Bot{

    @Override
    public int[] move(String[][] gameState) {
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}
