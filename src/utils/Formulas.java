package utils;

import java.text.MessageFormat;
import java.util.List;

public final class Formulas {
	private Formulas(){}
	
	public static Double logProbabilityDouble(Integer elementary, Integer all) {
		double probability;
		if(all == 0 || elementary == 0) {
			Logger.warning(MessageFormat.format("logProbabilityDouble({0}, {1}) => 0", all, elementary));
			probability = 0.0;
		} else {
			double p = elementary.doubleValue()/all.doubleValue();
			probability = Math.log(p)*p;
		}
		
		return probability;
	}
	
	public static Double intervalProbability(Integer all, Integer healthyCount) {
		if(all <= 0 || healthyCount < 0 || all - healthyCount < 0) {
			Logger.warning("Bad arguments! all = " + all + " healthy = " + healthyCount);
			return 0.0;
		}

		return -(logProbabilityDouble(all - healthyCount, all) +
				logProbabilityDouble(healthyCount, all));
	}
	
	public static Double H(List<Integer> input) {
		double sum = 0.0;
		for(Integer peopleCountInInterval : input) {
			sum += intervalProbability(Constants.ALL_SUBJECTS_COUNT, peopleCountInInterval);;
		}
		
		return -1*sum;
	}
	
	
	public static Double H(List<Integer> allInterval, List<Integer> healthyInterval) {
		double sum = 0.0;
		for (Integer healthyPeopleInInerval : healthyInterval) {
			for(Integer allPeopleInInterval : allInterval) {
				sum += intervalProbability(allPeopleInInterval, healthyPeopleInInerval);
			}
		}
		return -1*sum;
	}
	
	public static Double I(List<Integer> all, List<Integer> healthy) {
		return H(all) + Constants.H_Y - H(all,healthy);
	}
	
	public static Double V(List<Integer> x, List<Integer> y) {
		return (1/x.size())*I(x,y);
	}
}
