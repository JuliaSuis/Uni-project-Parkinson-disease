package weka_file_gen;

public class DoubleHolder {
	private double[] data;
	
	public DoubleHolder(double[] data) {
		this.data = data;
	}
	
	public double[] getDoubles(){
		return data;
	}
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < data.length; i++) {
			str.append(data[i])
				.append(",");
		}
		str.deleteCharAt(str.length() - 1);
		return str.toString();
	}

}
