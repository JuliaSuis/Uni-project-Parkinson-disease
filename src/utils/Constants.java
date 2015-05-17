package utils;

import java.util.Arrays;
import java.util.List;

public final class Constants {
	private Constants(){};
	
	public static final int ALL_SUBJECTS_COUNT = 195;
	public static final double H_Y = Formulas.H(Arrays.asList(ALL_SUBJECTS_COUNT, ALL_SUBJECTS_COUNT),
			Arrays.asList(48, 147));
	
	public static final List<String> NAMES_POOL = Arrays.asList(
			"MDVP:Fo(Hz)", "MDVP:Fhi(Hz)", "MDVP:Flo(Hz)",
			"MDVP:Jitter(%)", "MDVP:Jitter(Abs)", "MDVP:RAP,MDVP:PPQ",
			"Jitter:DDP", "MDVP:Shimmer", "MDVP:Shimmer(dB)",
			"Shimmer:APQ3", "Shimmer:APQ5", "MDVP:APQ",
			"Shimmer:DDA", "NHR", "HNR",
			"RPDE", "DFA", "spread1",
			"spread2", "D2");
}
