package simulation;

import config.XMLParser;
import elements.Cell;
import elements.Grid;

public class GameOfLifeSimulation extends Simulation {
    public static final int LIVE = 1;
    public static final int DEAD = 2;

    private XMLParser myXMLParser;
    private int minPopulationThreshold;
    private int maxPopulationThreshold;


    public GameOfLifeSimulation(Grid grid) {
        super(grid);
        myXMLParser = new XMLParser("Game of Life", grid.getMyConfigFile());
        minPopulationThreshold = (int) myXMLParser.getSimulationParameter1();
        maxPopulationThreshold = (int) myXMLParser.getSimulationParameter2();
    }

    @Override
    public void analyzeCells() {
        for(Cell cell: getGrid()){
                int liveNeighborsCount = countLiveNeighbors(cell.getMyNeighbors());
                if (cell.getState() == LIVE) {
                    if (liveNeighborsCount < minPopulationThreshold || liveNeighborsCount > maxPopulationThreshold) {
                        cell.setMyNextState(DEAD);
                    }
                } else if (cell.getState() == DEAD) {
                    if (liveNeighborsCount == maxPopulationThreshold) {
                        cell.setMyNextState(LIVE);
                    }
                }
            }
        }


    private int countLiveNeighbors(Cell[] neighbors) {
        int liveNeighborsCount = 0;
        for (Cell neighbor : neighbors) {
            if (neighbor.getState() == LIVE) {
                liveNeighborsCount++;
            }
        }
        return liveNeighborsCount;
    }
}
