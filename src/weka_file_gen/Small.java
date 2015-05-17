package weka_file_gen;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import utils.Parser;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;


public class Small {
	public static void main(String... args) throws Exception{
		FastVector attributes = new FastVector();		
		attributes.addElement(new Attribute("name", (FastVector) null));
		attributes.addElement(new Attribute("MDVP:Fo(Hz)"));
		attributes.addElement(new Attribute("MDVP:Fhi(Hz)"));
		attributes.addElement(new Attribute("MDVP:Flo(Hz)"));
		attributes.addElement(new Attribute("MDVP:Jitter(%)"));
		attributes.addElement(new Attribute("MDVP:Jitter(Abs)"));
		attributes.addElement(new Attribute("MDVP:RAP"));
		attributes.addElement(new Attribute("MDVP:PPQ"));
		attributes.addElement(new Attribute("Jitter:DDP"));
		attributes.addElement(new Attribute("MDVP:Shimmer"));
		attributes.addElement(new Attribute("MDVP:Shimmer(dB)"));
		attributes.addElement(new Attribute("Shimmer:APQ3"));
		attributes.addElement(new Attribute("Shimmer:APQ5"));
		attributes.addElement(new Attribute("MDVP:APQ"));
		attributes.addElement(new Attribute("Shimmer:DDA"));
		attributes.addElement(new Attribute("NHR"));
		attributes.addElement(new Attribute("HNR"));
		
		FastVector classValues = new FastVector(2);
		classValues.addElement("0");
		classValues.addElement("1");
		attributes.addElement(new Attribute("class", classValues));
		
		attributes.addElement(new Attribute("RPDE"));
		attributes.addElement(new Attribute("DFA"));
		attributes.addElement(new Attribute("spread1"));
		attributes.addElement(new Attribute("spread2"));
		attributes.addElement(new Attribute("D2"));
		attributes.addElement(new Attribute("PPE"));
		Instances data = new Instances("small", attributes, 0);
		
		List<ParkinsonSmallHolder> doubles = Parser.parseSmall(
				Paths.get("./data_sets/parkinsons_small.data").toFile()
				);
		
		for(ParkinsonSmallHolder holder : doubles) {
			double[] result = new double[1 + holder.getOtherData().length];
			result[0] = data.attribute(0).addStringValue(holder.getName());
			System.arraycopy(holder.getOtherData(), 0, result, 1, holder.getOtherData().length);
			data.add(new Instance(1.0, result));
		}
		
		File targetFile = Paths.get("./small.arff").toFile();
		if (targetFile.exists()) {
			targetFile.delete();
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(targetFile);
		//saver.setDestination(targetFile);
		saver.writeBatch();
		System.out.println("Creation successfull!");
	}

}
