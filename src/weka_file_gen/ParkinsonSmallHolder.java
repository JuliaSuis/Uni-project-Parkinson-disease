package weka_file_gen;

public class ParkinsonSmallHolder {
	private String name;
	private DoubleHolder otherData;
	
	public ParkinsonSmallHolder(String name, double[] otherData){
		this.name = name;
		this.otherData = new DoubleHolder(otherData);
	}
	
	public ParkinsonSmallHolder(String name, DoubleHolder otherData){
		this.name = name;
		this.otherData = otherData;
	}
	
	public String getName(){
		return name;
	}
	
	public double[] getOtherData(){
		return otherData.getDoubles();
	}
	
	public String toString(){		
		return name + "," + otherData.toString();
	}

}
