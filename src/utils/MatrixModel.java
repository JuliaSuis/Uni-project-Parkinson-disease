package utils;

import java.util.List;

public interface MatrixModel <T> {
	public int getRowsCount();
	public int getColumnsCount();
	public T get(int row, int column);
	public T set(int row, int column, T value);
	public List<T> getRow(int row);
	public List<T> setRow(int row, List<T> newRow);
	public List<T> getColumn(int column);
	public List<T> setColumn(int column, List<T> newColumn);
}
