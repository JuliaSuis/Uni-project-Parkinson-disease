package utils;

import java.util.Arrays;

public final class Constants {
	private Constants(){};
	
	public static final int ALL_SUBJECTS_COUNT = 195;
	public static final double H_Y = Formulas.H(Arrays.asList(ALL_SUBJECTS_COUNT, ALL_SUBJECTS_COUNT),
			Arrays.asList(48, 147));
}
