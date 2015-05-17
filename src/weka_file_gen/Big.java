package weka_file_gen;

import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.IOUtils;

import utils.Parser;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Big {
	public static void main(String... args) throws Exception{
		FastVector attributes = new FastVector();
		Instances data = new Instances("big", attributes, 0);
		attributes.addElement(new Attribute("subject#"));
		attributes.addElement(new Attribute("age"));
		attributes.addElement(new Attribute("sex"));
		attributes.addElement(new Attribute("test_time"));
		attributes.addElement(new Attribute("motor_UPDRS"));
		attributes.addElement(new Attribute("total_UPDRS"));
		attributes.addElement(new Attribute("Jitter(%)"));
		attributes.addElement(new Attribute("Jitter(Abs)"));
		attributes.addElement(new Attribute("Jitter:RAP"));
		attributes.addElement(new Attribute("Jitter:PPQ5"));
		attributes.addElement(new Attribute("Jitter:DDP"));
		attributes.addElement(new Attribute("Shimmer"));
		attributes.addElement(new Attribute("Shimmer(db)"));
		attributes.addElement(new Attribute("Shimmer:APQ3"));
		attributes.addElement(new Attribute("Shimmer:APQ5"));
		attributes.addElement(new Attribute("Shimmer:APQ11"));
		attributes.addElement(new Attribute("Shimmer:DDA"));
		attributes.addElement(new Attribute("NHR"));
		attributes.addElement(new Attribute("HNR"));
		attributes.addElement(new Attribute("RPDE"));
		attributes.addElement(new Attribute("DFA"));
		attributes.addElement(new Attribute("PPE"));
		
		List<DoubleHolder> doubles = Parser.parseBig(
				Paths.get("C:\\Users\\Юля\\Desktop\\Study hard\\Кафедра\\parkinsons_updrs.data").toFile()
				);
		
		for(DoubleHolder holder : doubles) {
			data.add(new Instance(1.0, holder.getDoubles()));
		}
		
		
		
		IOUtils.write(data.toString(), new FileOutputStream(Paths.get("C:\\Users\\Юля\\Desktop\\Study hard\\Кафедра\\big.arff").toFile()));
		System.out.println("Creation successfull!");
	}
}
