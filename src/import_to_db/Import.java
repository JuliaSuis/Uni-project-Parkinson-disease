package import_to_db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;

import weka.core.Instances;
import weka.core.converters.DatabaseSaver;


public class Import {
  public static void main(String[] args) throws Exception {
    Instances dataSmall = new Instances(new BufferedReader(new FileReader(
    		Paths.get("C:\\Users\\Юля\\Desktop\\Study hard\\Кафедра\\small.arff").toFile()
    		)));
    dataSmall.setClassIndex(dataSmall.numAttributes() - 1);
    Instances dataBig = new Instances(new BufferedReader(new FileReader(
    		Paths.get("C:\\Users\\Юля\\Desktop\\Study hard\\Кафедра\\big.arff").toFile()
    		)));
    dataBig.setClassIndex(dataBig.numAttributes() - 1);
    
    DatabaseSaver save = new DatabaseSaver();
    save.setUrl("jdbc:postgresql://127.0.0.1:5432/parkinsons_data");
    save.setUser("postgres");
    save.setPassword("123456");
    save.setInstances(dataSmall);
    save.setRelationForTableName(true);
    save.connectToDatabase();
    save.writeBatch();
    save.setInstances(dataBig);
    save.writeBatch();
    
    System.out.println("Import successfull!");
  }
}
