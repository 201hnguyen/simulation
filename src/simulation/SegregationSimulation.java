package simulation;

import config.XMLSimulationParser;
import elements.Cell;
import elements.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SegregationSimulation extends Simulation {
    private XMLSimulationParser myXMLParser;
    private double mySegregationThreshold;

    private List<Cell> myAvailableCells;

    public SegregationSimulation(Grid grid) {
        super(grid);
        myXMLParser = new XMLSimulationParser(grid.getMyConfigFile());
        myAvailableCells = new ArrayList<>();
        mySegregationThreshold = myXMLParser.getParameters().get("segregation_threshold");
    }

    @Override
    public void analyzeCells() {
        Random random = new Random();
        int id = 0;
        myAvailableCells = super.getGrid().getEmptyCells();
        for (int i = 0; i < super.getGrid().getNumRows(); i++) {
            for (int j = 0; j < super.getGrid().getNumCols(); j++) {
                Cell cell = super.getGrid().getCell(id);
                id++;
                double similarNeighbors = countSimilarNeighbors(cell, cell.getMyNeighbors());
                if (cell.getState()!= 0 && (similarNeighbors / cell.getMyNeighbors().size() < mySegregationThreshold)) {
                    Cell random_cell = myAvailableCells.get(random.nextInt(myAvailableCells.size()));
                    random_cell.setMyIsAvailable(false);
                    random_cell.setMyNextState(cell.getState());
                    cell.setMyNextState(0);
                    myAvailableCells.remove(random_cell);
                    myAvailableCells.add(cell);
                }
            }
        }
    }

    private double countSimilarNeighbors(Cell cell, List<Cell> neighbors){
        int similarNeighborsCount = 0;
        int state = cell.getState();
        for(Cell neighbor: neighbors){
            if(neighbor.getState() == state){
                similarNeighborsCount++;
            }
        }
        return similarNeighborsCount;
    }
}
