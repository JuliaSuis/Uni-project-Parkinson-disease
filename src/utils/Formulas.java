package utils;

import java.text.MessageFormat;
import java.util.List;

public final class Formulas {
	private Formulas(){}
	
	private static Double elementaryProbability(Integer numerator, Integer denumerator) {
		if(numerator < 0 || denumerator < 1) {
			throw new RuntimeException(MessageFormat.format("Bad arguments: elementaryProbability({0}, {1})", numerator, denumerator));
		}
		return numerator.doubleValue()/denumerator.doubleValue();
	}
	
	public static Double elementaryLogProbability(Integer numerator, Integer denumerator){
		Double probability = elementaryProbability(numerator, denumerator);
		
		if(probability == 0.0){
			return 0.0;
		}
		
		return Math.log(probability)*probability;
	}
	
	public static Double elementaryXProbability(Integer elementary){
		return elementaryProbability(elementary, Constants.ALL_SUBJECTS_COUNT);
	}
	
	public static Double logProbabilityX(Integer elementary) {
		return elementaryLogProbability(elementary, Constants.ALL_SUBJECTS_COUNT);
	}	
	
	public static Double elementaryYProbability(Integer y) {
		if(y != 0 && y != 1) {
			throw new RuntimeException("Unknown Y probability!");
		}
		
		return y == 0 ? 
				elementaryProbability(48, Constants.ALL_SUBJECTS_COUNT) :
				elementaryProbability(147, Constants.ALL_SUBJECTS_COUNT);			
	}
	
	public static Double logProbabilityY() {
		return -(elementaryYProbability(0) + elementaryYProbability(1));
	}
	
	public static Double jointProbability(Integer x, Integer y){
		return elementaryXProbability(x)*elementaryYProbability(y);
	}
	
	public static Double jointLogProbability(Integer x, Integer y) {
		Double p = jointProbability(x, y);
		if(p == 0) {
			return 0.0;
		}
		
		return p*Math.log(p);
	}
	
	public static Double H(List<Integer> xList) {
		double sum = 0.0;
		
		for(Integer x : xList) {
			sum += logProbabilityX(x);
		}
		
		return -sum;
	}
	
	
	public static Double H(List<Integer> xList, List<Integer> yList) {
		double sum = 0.0;
		
		for(Integer x : xList) {
			for(Integer y : yList) {
				sum += jointLogProbability(x, y);
			}
		}		
		
		return sum;
	}
	
	public static Double I(List<Integer> x, List<Integer> y) {
		return H(x) + logProbabilityY() - H(x,y);
	}
	
	public static Double V(List<Integer> x, List<Integer> y) {
		return (1/x.size())*I(x,y);
	}
	
	public static Double V(List<Double> iCalculated) {
		Double sum = 0.0;
		
		for(Double i : iCalculated) {
			sum += i;					
		}
		
		return sum/iCalculated.size();
	}
}
