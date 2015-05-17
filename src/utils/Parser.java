package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

import weka_file_gen.DoubleHolder;
import weka_file_gen.ParkinsonSmallHolder;

public final class Parser {
	private Parser(){}
	
	public static List<DoubleHolder> parseBig(File pathToFile){
		List<DoubleHolder> parsed;
		try {
            List<String> lines = IOUtils.readLines(new FileInputStream(pathToFile));
            parsed = new ArrayList<>(lines.size());
            for (String s : lines) {
            	parsed.add(new DoubleHolder(parseDoublesFromStringArray(s.split(","))));
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
            parsed = Collections.emptyList();
        }
		return parsed;
	}
	
	public static List<ParkinsonSmallHolder> parseSmall(File pathToFile) {
		List<ParkinsonSmallHolder> parsed;
		
		try{
			List<String> lines = IOUtils.readLines(new FileInputStream(pathToFile));
            parsed = new ArrayList<>(lines.size());
            
            for(String str : lines) {
            	int firstCommaIndex = str.indexOf(",");
            	String name = str.substring(0, firstCommaIndex);
            	double[] other = parseDoublesFromStringArray(
            					str.substring(firstCommaIndex + 1, str.length()).split(","));
            	
            	ParkinsonSmallHolder smallHolder = new ParkinsonSmallHolder(name, other);
            	
            	parsed.add(smallHolder);
            }
            
		} catch (Exception e) {
			e.printStackTrace(System.err);
			parsed = Collections.emptyList();
		}
		
		return parsed;
	}
	
	private static double[] parseDoublesFromStringArray(String... str){
		if(str == null) {
			return new double[0];
		}
		
		double data[] = new double[str.length];
		int i = 0;
		for(String toParse : str) {
			data[i++] = Double.parseDouble(toParse);
		}
		
		return data;
	}
}
