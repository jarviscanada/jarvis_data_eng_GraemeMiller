package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {

	
	final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
	
	private String regex;
	private String rootPath;
	private String outFile;
	
	public static void main(String[] args) {

		if(args.length != 3) {
			throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
		}
		
		// Use default logger config
		BasicConfigurator.configure();
		JavaGrepImp javaGrepImp = new JavaGrepImp();
		javaGrepImp.setRegex(args[0]);
		javaGrepImp.setRootPath(args[1]);
		javaGrepImp.setOutFile(args[2]);
		
		try {

			javaGrepImp.process();
		} catch (Exception ex){
			javaGrepImp.logger.error("Error: Unable to process", ex);
		}
	}

	@Override
	public void process() throws IOException {
		logger.debug("Regex set to {}", getRegex());
		logger.debug("Outfile set to {}", getOutFile());
		logger.debug("Rootpath set to {}", getRootPath());
		ArrayList<String> matchedLines = new ArrayList<String>();
		List<File> listedFiles = new ArrayList<File>();
		List<String> lines = new ArrayList<String>();
		listedFiles = listFiles(getRootPath());
		for(File file : listedFiles) {
			lines = readLines(file);
			for(String line : lines) {
				if (containsPattern(line)) {
					matchedLines.add(line);
				}
			}
		}
		writeToFile(matchedLines);
	}

	@Override
	public List<File> listFiles(String rootDir) {
		File path = new File(rootDir);
		List<File> files = new ArrayList<File>();
		if (path.isDirectory()) {
	        File[] fileArray = path.listFiles();
	        if (fileArray != null) {
	            for (File file : fileArray) {
	                if (file.isDirectory()) {
	                    files.addAll(listFiles(file.getAbsolutePath()));
	                } else {
	                    files.add(file);
	                }
	            }
	        }
	    }
		return files;
	}

	@Override
	public List<String> readLines(File inputFile) {
		List<String> lines = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			String line;
			while((line = reader.readLine()) != null) {
				lines.add(line);
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	@Override
	public boolean containsPattern(String line) {
		Pattern pattern = Pattern.compile(getRegex(), Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(line);
	    boolean check = matcher.find();
		if(check) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void writeToFile(List<String> lines) throws IOException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(getOutFile()));
			for(String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getRootPath() {
		return rootPath;
	}

	@Override
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
		
	}

	@Override
	public String getRegex() {
		return regex;
	}

	@Override
	public void setRegex(String regex) {
		this.regex = regex;
		
	}

	@Override
	public String getOutFile() {
		return outFile;
	}

	@Override
	public void setOutFile(String outFile) {
		this.outFile = outFile;
		
	}

}
