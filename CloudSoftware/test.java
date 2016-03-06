import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String result = new String();
		try {
			String[] cmds = new String[] { "/bin/sh", "-c",
					"chkconfig" };

			result="";
			Process process;

			process = Runtime.getRuntime().exec(cmds);

			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); 
			String line;

			while ((line = strCon.readLine()) != null) {
				System.out.println(line.trim());
				
				result = result + line.split("")[1] + "   ";
			}
			System.out.println("System    :   "+result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0x0000802";
			System.out.println("System    :   "+result);
		}

	}

}
