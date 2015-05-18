package calculations;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import utils.Constants;
import utils.Formulas;
import utils.Logger;
import utils.MatrixModel;
import utils.ReproductionCriteria;
import calculations.GeneticAlg.Matrix.CellValue;


public class GeneticAlg {
	public static final Random RANDOMIZER = new Random();
	public static final String WORST_CSV = "./worst";
	public static final String BEST_CSV = "./best";
	public static final String RESULT_10_TRUE_CSV = "./result_10_true.csv";
	public static final String RESULT_10_FALSE_CSV = "./result_10_false.csv";
	public static final String RESULT_16_TRUE_CSV = "./result_16_true.csv";
	public static final String RESULT_16_FALSE_CSV = "./result_16_false.csv";
	public static final int GENERATIONS_COUNT = 500;
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Matrix oldGeneration = new Matrix(200, 10);
		
		Logger.info("====================MATRIX WITH NAMES====================");
		//Logger.info(matrix.toString());
		
		Logger.info("====================MATRIX WITH VALUES===================");
		//Logger.info(matrix.toStringValues());
		
		Logger.info("====================CALCULATED V===================");
		List<Double> calculatedV = new ArrayList<>(oldGeneration.getRowsCount());		
		for(int i = 0; i < oldGeneration.getRowsCount(); i++) {
			calculatedV.add(Formulas.V(oldGeneration.getRowValues(i)));
		}
		Logger.info("Count of results = " + calculatedV.size());
		//Logger.info(calculatedV.toString());
		
		Logger.info("====================REPRODUCTING POPULATION===================");
		Matrix reproducted10WithMutation = reproductPopulationFirstWay(oldGeneration, true);
		Matrix reproducted10WithoutMutation = reproductPopulationFirstWay(oldGeneration, false);
		savePopulationEdges(reproducted10WithMutation, Paths.get(RESULT_10_TRUE_CSV));
		savePopulationEdges(reproducted10WithoutMutation, Paths.get(RESULT_10_FALSE_CSV));
		//Logger.info(reproducted.toString());
		
		Matrix oldGeneration16 = new Matrix(200, 16);
		Matrix newGeneration16WithMutation = reproductPopulationFirstWay(oldGeneration16, true);
		Matrix newGeneration16WithoutMutation = reproductPopulationFirstWay(oldGeneration16, false);
		savePopulationEdges(newGeneration16WithMutation, Paths.get(RESULT_16_TRUE_CSV));
		savePopulationEdges(newGeneration16WithoutMutation, Paths.get(RESULT_16_FALSE_CSV));
		long end = System.currentTimeMillis();
		Logger.info("All is done! With " + (end - start) + " ms");
		
	}
	
	public static void savePopulationEdges(Matrix bestGeneration, Path pathToFile){
		List<Double> newV = bestGeneration.calculateV();
		int worstInPopulationPosition = findMinPosition(newV);
		int bestInPopulationPosition = findMaxPosition(newV);
		String newWorst = "new worst in population at position = " + worstInPopulationPosition +
				" with fitness = " + newV.get(worstInPopulationPosition) +
				" with characteristics = " + bestGeneration.getRow(worstInPopulationPosition);
		String newBest = "new best in population at position = " + bestInPopulationPosition + 
				" with fitness = " + newV.get(bestInPopulationPosition) +
				" with characteristics = " + bestGeneration.getRow(bestInPopulationPosition);
		
		Logger.info(newWorst);
		Logger.info(newBest);
		
		try {
			IOUtils.write(newWorst + "\n" + newBest, new FileOutputStream(pathToFile.toFile()));
		} catch (Exception e){
			Logger.error(e.getMessage(), e);
		}
	}
 	
	public static int findMinPosition(List<Double> input) {
		Integer minPosition = -1;
		Double minValue = Double.MAX_VALUE;
		for(int i = 0; i < input.size(); i++) {
			if(minValue > input.get(i)) {
				minPosition = i;
				minValue = input.get(i);
			}
		}
		return minPosition;
	}
	
	public static int findMaxPosition(List<Double> input) {
		Integer maxPosition = -1;
		Double minValue = Double.MIN_VALUE;
		for(int i = 0; i < input.size(); i++) {
			if(minValue < input.get(i)) {
				maxPosition = i;
				minValue = input.get(i);
			}
		}
		return maxPosition;
	}
	
	public static Matrix reproductPopulationFirstWay(Matrix inputPopulation, boolean isWithMutation){
		Matrix outputPopulation = new Matrix(inputPopulation);
		List<Double> fitnessVals;
		int generation = 0;
		CSVPopulationParameters populationParams = new CSVPopulationParameters(inputPopulation, outputPopulation);
		while(outputPopulation.getCriteria().isReproductionNeeded()) {
			
			fitnessVals = outputPopulation.calculateV();
			int minPosition = findMinPosition(fitnessVals);
			
			int firstRandomParent = RANDOMIZER.nextInt(outputPopulation.getRowsCount());
			int secondRandomParent = firstRandomParent;
			while(firstRandomParent == secondRandomParent) {
				secondRandomParent = RANDOMIZER.nextInt(outputPopulation.getRowsCount());
			}
			List<CellValue> newChild = bornNewChild(outputPopulation.getRow(firstRandomParent), 
					outputPopulation.getRow(secondRandomParent));
			outputPopulation.setRow(minPosition, newChild);
			
			if(isWithMutation){
				doMutationInPopulation(outputPopulation);
			}
			
			
			populationParams.mark();
			generation++;
		}
		Logger.info("Reproduction successfull! Generations count = " + generation);
		String pattern = "{0}_{1}_{2}.csv";
		String worstPath = MessageFormat.format(pattern, WORST_CSV, isWithMutation, outputPopulation.getColumnsCount());
		String bestPath = MessageFormat.format(pattern, BEST_CSV, isWithMutation, outputPopulation.getColumnsCount());
		populationParams.writeWorstToFile(Paths.get(worstPath));
		populationParams.writeBestToFile(Paths.get(bestPath));
		return outputPopulation;
	}
	
	public static void mutateSubject(Matrix population, Integer subjectPosition) {
		int randomGenePosition = RANDOMIZER.nextInt(population.getColumnsCount());
		int randomMutatedGenePosition = RANDOMIZER.nextInt(Constants.NAMES_POOL.size());
		population.set(subjectPosition, randomGenePosition, 
				new CellValue(Constants.NAMES_POOL.get(randomMutatedGenePosition), Matrix.FITNESS_VALUES.get(randomMutatedGenePosition))
				);
	}
	
	/**
	 * 1% от популяции
	 * @param population
	 */
	public static void doMutationInPopulation(Matrix population){
		int mutateSubjectsCount = Double.valueOf(population.getRowsCount()*0.01).intValue();
		//Logger.info("Count of mutated species = " + mutateSubjectsCount);
		Set<Integer> mutatedSpeciesPositions = new HashSet<>();
		while(mutatedSpeciesPositions.size() < mutateSubjectsCount){
			mutatedSpeciesPositions.add(RANDOMIZER.nextInt(population.getRowsCount()));
		}
		
		for(Integer mutationSubjectPosition : mutatedSpeciesPositions) {
			mutateSubject(population, mutationSubjectPosition);
		}
		
		//Logger.info("Mutation successfull!");
	}
	
	public static List<CellValue> bornNewChild(List<CellValue> firstParent, List<CellValue> secondParent) {
		int geneticInheritanceEdge = RANDOMIZER.nextInt(firstParent.size());
		List<CellValue> child = new ArrayList<>(firstParent.size());
		
		for(int i = 0; i < geneticInheritanceEdge; i++) {
			child.add(firstParent.get(i));
		}
		
		for(int i = child.size(); i < secondParent.size(); i++) {
			child.add(secondParent.get(i));
		}
		
		return child;
	}
	
	public static class Matrix implements MatrixModel<CellValue>{		
		private static final List<Double> FITNESS_VALUES = CalculateI.calculateI(Paths.get(CalculateI.PATH));
		private final int rowsCount;
		private final int columnsCount;
		private final List<List<CellValue>> body;
		private final ReproductionCriteria reproductionCriteria = new MatrixReproductionCriteria();
		
		
		public Matrix(int rowsCount, int columnsCount){
			this.rowsCount = rowsCount;
			this.columnsCount = columnsCount;
			Set<List<CellValue>> tmpBody = generateRandomBody(rowsCount, columnsCount);
			body = new ArrayList<>(tmpBody.size());
			body.addAll(tmpBody);
		}
		
		public Matrix(Matrix matrix) {
			this.rowsCount = matrix.getRowsCount();
			this.columnsCount = matrix.getColumnsCount();
			this.body = new ArrayList<>(rowsCount);
			for(int i = 0; i < rowsCount; i++) {
				body.add(matrix.getRow(i));
			}
		}
		
		private List<CellValue> generateRandomRow(int length) {
			List<CellValue> tmpRow = new ArrayList<>(length);			
			for(int i = 0; i < length; i++) {
				int nextRandomPosition = Double.valueOf(RANDOMIZER.nextInt(Constants.NAMES_POOL.size())).intValue();
				tmpRow.add(i, new CellValue(Constants.NAMES_POOL.get(nextRandomPosition),
						FITNESS_VALUES.get(nextRandomPosition)));
			}
			
			return tmpRow;
		}
		
		private Set<List<CellValue>> generateRandomBody(int rowsCount, int columnsCount){
			Set<List<CellValue>> tmpBody = new HashSet<>();
			try{
				while(tmpBody.size() < rowsCount) {
					tmpBody.add(generateRandomRow(columnsCount));
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				tmpBody = Collections.emptySet();
			}
			return tmpBody;
		}
		
		@Override
		public CellValue get(int row, int column) {
			return body.get(row).get(column);
		}
		
		public CellValue getCell(int row, int column) {
			return body.get(row).get(column);
		}
		
		@Override
		public CellValue set(int row, int column, CellValue value) {
			CellValue wasInCell = get(row, column);
			body.get(row).set(column, value);
			return wasInCell;
		}
		
		@Override
		public List<CellValue> getRow(int row) {
			return body.get(row);
		}
		
		public List<Double> getRowValues(int row) {
			List<Double> rowValues = new ArrayList<>(getColumnsCount());
			for(CellValue cell : getRow(row)) {
				rowValues.add(cell.getValue());
			}
			return rowValues;
		}
		
		@Override
		public List<CellValue> getColumn(int column) {
			List<CellValue> columnValues = new ArrayList<>(getRowsCount());
			for(List<CellValue> row : body) {
				columnValues.add(row.get(column));
			}
			return columnValues;
		}
		
		public List<Double> getColumnValues(int column) {
			List<Double> columnValues = new ArrayList<>(getColumnsCount());
			List<CellValue> gotColumn = getColumn(column);
			for(CellValue cellValue : gotColumn) {
				columnValues.add(cellValue.getValue());
			}
			return columnValues;
		}
		
		@Override
		public int getRowsCount() {
			return rowsCount;
		}
		
		@Override
		public int getColumnsCount() {
			return columnsCount;
		}
		
		public List<Double> calculateV(){
			List<Double> calculatedV = new ArrayList<>(getRowsCount());		
			for(int i = 0; i < getRowsCount(); i++) {
				calculatedV.add(Formulas.V(getRowValues(i)));
			}
			
			return calculatedV;
		}
		
		public ReproductionCriteria getCriteria(){
			return reproductionCriteria;
		}
		
		@Override
		public String toString(){
			StringBuilder table = new StringBuilder();
			
			for(int i = 0, rows = getRowsCount(); i < rows; i++) {
				table.append(getRow(i))
					.append("\n");
			}
			
			return table.toString();
		}
		
		public String toStringValues(){
			StringBuilder table = new StringBuilder();
			
			for(int i = 0, rows = getRowsCount(); i < rows; i++){
				table.append("[");
				for(CellValue cell : getRow(i)) {
					table.append(cell.getValue())
						.append(", ");
				}
				int currentLength = table.length();
				table.delete(currentLength - 2, currentLength);
				table.append("]\n");
			}
			
			return table.toString();
		}
		
		public static class CellValue {
			private final String name;
			private final Double fitnessValue;
			
			private CellValue(String name, Double fitnessValue){
				this.name = name;
				this.fitnessValue = fitnessValue;
			}
			
			public String getName(){
				return name;
			}
			
			public Double getValue(){
				return fitnessValue;
			}
			
			public String toString(){
				return name;
			}
		}
		
		public class MatrixReproductionCriteria implements ReproductionCriteria {
			private int i;
			@Override
			public boolean isReproductionNeeded() {
				return i++ < GENERATIONS_COUNT;
			}			
		}

		@Override
		public List<CellValue> setRow(int row, List<CellValue> newRow) {
			List<CellValue> oldRow = getRow(row);
			body.set(row, newRow);
			return oldRow;
		}

		@Override
		public List<CellValue> setColumn(int column, List<CellValue> newColumn) {
			List<CellValue> oldColumn = getColumn(column);
			for(int i = 0; i < getRowsCount(); i++) {
				body.get(column).set(i, newColumn.get(i));
			}
			return oldColumn;
		}
		
	}
	
	public static class CSVPopulationParameters{
		private final Matrix newPopulation;
		private List<Double> newV;
		private final List<CustomPoint> worstPoints = new LinkedList<>();
		private final List<CustomPoint> bestPoints = new LinkedList<>();
		private int counter = 1;
		
		public CSVPopulationParameters(Matrix oldPopulation, Matrix newPopulation){
			this.newPopulation = newPopulation;
			List<Double> oldV = oldPopulation.calculateV();
			worstPoints.add(new CustomPoint(0, oldV.get(findMinPosition(oldV))));
			bestPoints.add(new CustomPoint(0, oldV.get(findMaxPosition(oldV))));
		}
		
		public void mark(){
			newV = newPopulation.calculateV();
			int localCounter = counter++;
			worstPoints.add(new CustomPoint(localCounter, newV.get(findMinPosition(newV))));
			bestPoints.add(new CustomPoint(localCounter, newV.get(findMaxPosition(newV))));
		}
		
		private String pointsToCSV(List<CustomPoint> points) {
			StringBuilder tmp = new StringBuilder();
			for(CustomPoint point : points) {
				tmp.append(point)
					.append("\n");
			}
			Logger.info(points.toString());
			return tmp.toString();
		}
		
		private void writeToFile(List<CustomPoint> points, Path pathToFile) {
			try{
				IOUtils.write(pointsToCSV(points), new FileOutputStream(pathToFile.toFile()));
				Logger.info("CSV file saved in " + pathToFile.toAbsolutePath());
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		
		public void writeWorstToFile(Path path) {
			writeToFile(worstPoints, path);
		}
		
		public void writeBestToFile(Path path) {
			writeToFile(bestPoints, path);
		}
		
		private static class CustomPoint{
			private final double x;
			private final double y;
			
			public CustomPoint(double x, double y) {
				this.x = x;
				this.y = y;
			}
			
			public double getX(){
				return x;
			}
			
			public double getY(){
				return y;
			}
			
			@Override
			public String toString(){
				return x + "," + y;
			}
		}
		
	}

}
