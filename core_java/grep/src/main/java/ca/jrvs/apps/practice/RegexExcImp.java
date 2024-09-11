package ca.jrvs.apps.practice;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc{

	public static void main(String[] args) {
		RegexExcImp test = new RegexExcImp();
		System.out.println("Test 1: " + test.matchJpeg("file.jpeg"));
		System.out.println("Test 2: " + test.matchJpeg("notaJpeg.file"));
		System.out.println("Test 3: " + test.matchIp("0.0.5.456"));
		System.out.println("Test 4: " + test.matchIp("1.4234.2.2.2"));
		System.out.println("Test 5: " + test.isEmptyLine("    "));
		System.out.println("Test 6: " + test.isEmptyLine("It is Not an empty Line"));
	}

	@Override
	public boolean matchJpeg(String filename) {
		Pattern pattern = Pattern.compile("\\w+\\.jp(e){0,1}g$", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(filename);
	    boolean check = matcher.find();
		if(check) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean matchIp(String ip) {
		Pattern pattern = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");
	    Matcher matcher = pattern.matcher(ip);
	    boolean check = matcher.find();
		if(check) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean isEmptyLine(String line) {
		Pattern pattern = Pattern.compile("^\\s*$");
	    Matcher matcher = pattern.matcher(line);
	    boolean check = matcher.find();
		if(check) {
			return true;
		}else {
			return false;
		}
	}

}
