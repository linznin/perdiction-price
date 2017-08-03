package org.chening.text.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is a driver class to execute the PSO process

import org.chening.text.core.ProSetting;

import java.io.File;

public class PSODriver implements PSOConstants{

	public void run() {

		parameterScand(new File(ProSetting.LDA_DATA_PATH));
	}

	static double[] cvalues = { 1, 1.5, 2, 2.5 };
	static double[] wvalues = { 0.4, 0.5, 0.6, 0.7, 0.8, 0.9 };

	private static void scanFolder(File Path, double c, double w) {
		try {
			for (final File fileEntry : Path.listFiles()) {
				if (!fileEntry.isHidden()) {
					if (fileEntry.isDirectory()) {
						scanFolder(fileEntry,c ,w);
					} else {
						System.out.println("Path = [" + fileEntry.getParentFile().getName() + "]");
						new PSOProcess(fileEntry.getPath(),fileEntry.getParentFile().getName(), c, w).execute();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parameterScand(File path){
		for (double c: cvalues ) {
			for (double w: wvalues) {
				scanFolder( path, c, w );
			}
		}

	}

}