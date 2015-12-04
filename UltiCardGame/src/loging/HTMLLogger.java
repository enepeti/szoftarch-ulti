package loging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLLogger implements Logger {

	private static File logfile = new File("..\\WebContent\\log.txt");

	@Override
	public void log(String message) {
		try{
			if(!logfile.exists()) {
				logfile.createNewFile();
			}
			
			FileWriter fileWriter = new FileWriter(logfile, true);
			BufferedWriter bw = new BufferedWriter(fileWriter);
			bw.write(message);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
