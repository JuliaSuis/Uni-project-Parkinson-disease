package calculations;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.FilesUtils;
import utils.Formulas;
import utils.Logger;

public class CalculateI {
	public static void main(String[] args) {
		ParsedData parsed = ParsedData.valueOf(Paths.get("./data_sets/minitest.csv"));
		System.out.println(parsed.getAllInFile());
		System.out.println(parsed.getHealthyInFile());
		
		System.out.println("=================RESULT==================");
		List<Double> iValues = CalculateI.calculateI(parsed);
		System.out.println(iValues);
		
	}
	
	public static List<Double> calculateI(ParsedData dataFromFile) {
		int dataSize = dataFromFile.getAllInFile().size();
		List<Double> iValues = new ArrayList<>(dataSize);
		
		for(int i = 0; i < dataSize; i++) {
			iValues.add(Formulas.I(dataFromFile.getAllInFile().get(i), dataFromFile.getHealthyInFile().get(i)));
		}
		return iValues;
	}
	
	private static class ParsedData {
		private final List<List<Integer>> healthyInFile;
		private final List<List<Integer>> allInFile;
		
		private ParsedData (List<List<Integer>> allInFile, List<List<Integer>> healthyInFile){
			this.healthyInFile = healthyInFile;
			this.allInFile = allInFile;
		}
		
		public static ParsedData valueOf(Path pathToFile) {
			List<String> lines = FilesUtils.readFile(pathToFile);
			List<List<Integer>> allInFile = new ArrayList<List<Integer>>(lines.size());
			List<List<Integer>> healthyInFile = new ArrayList<List<Integer>>(lines.size());
			try {
				for(String line : lines){
					String[] splittedLine = line.split(";");

					List<Integer> all = new ArrayList<>(splittedLine.length - 1);
					List<Integer> healthy = new ArrayList<>(splittedLine.length - 1);
					
					for(int i = 1; i < splittedLine.length; i++) {
						String[] cell = splittedLine[i].split("//");
						all.add(Integer.parseInt(cell[0]));
						healthy.add(Integer.parseInt(cell[1]));
					}
					
					allInFile.add(all);
					healthyInFile.add(healthy);
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				allInFile = Collections.emptyList();
				healthyInFile = Collections.emptyList();
			}
			
			return new ParsedData(allInFile, healthyInFile);
		}
		
		public List<List<Integer>> getHealthyInFile() {
			return healthyInFile;
		}
		
		public List<List<Integer>> getAllInFile() {
			return allInFile;
		}
	}

}
