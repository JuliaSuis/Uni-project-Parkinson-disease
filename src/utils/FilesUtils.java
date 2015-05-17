package utils;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

public final class FilesUtils {
	private FilesUtils(){}
	
	public static List<String> readFile(Path pathToFile) {
		List<String> lines;
		try {
			lines = IOUtils.readLines(new FileInputStream(pathToFile.toFile()));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			lines = Collections.emptyList();
		}
		
		return lines;
	}

}
