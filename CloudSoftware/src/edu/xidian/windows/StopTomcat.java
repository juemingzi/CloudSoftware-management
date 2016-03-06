package edu.xidian.windows;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class StopTomcat {

	public void stop(String uninstallpath) throws IOException {

		File[] fileList = new File(uninstallpath).listFiles();
		if (fileList != null)
			for (File f : fileList) {
				boolean flag = false;
				if (f.isDirectory() && f.getName().contains("tomcat")) {
					File[] files = f.listFiles();
					for (File file : files) {
						if (file.isDirectory()
								&& file.getName().contains("bin")) {
							String command = file.getAbsolutePath()
									+ "\\shutdown.bat";
							WVMScriptExecute we = new WVMScriptExecute();
							we.executeVMScript(command);
							flag = true;
							break;
						}
					}
					if (flag)
						break;

				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

	}

}
