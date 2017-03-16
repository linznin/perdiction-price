package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is a driver class to execute the PSO process

import java.io.File;

public class PSODriver implements PSOConstants{
	public static void main(String args[]) {
//		new CsvFile().genRowData("/Users/linznin/tmp/source data/","semiconductor_120_5d");
//		new PSOProcess().execute();
//		scanFolder(new File(DATA_PATH), C1, W);
		parameterScand(new File(DATA_PATH));
	}

	static double[] cvalues = { 1.5, 2, 2.5 };
	static double[] wvalues = { 0.4, 0.5, 0.6 };

	private static void scanFolder(File Path, double c, double w) {
		try {
			for (final File fileEntry : Path.listFiles()) {
				if (!fileEntry.isHidden()) {
					if (fileEntry.isDirectory()) {
						scanFolder(fileEntry,c ,w);
					} else {
						System.out.println("Path = [" + fileEntry.getParentFile().getName() + "]");
						new PSOProcess(fileEntry.getPath(),fileEntry.getParentFile().getName()).execute();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void parameterScand(File path){
		for (double c: cvalues ) {
			for (double w: wvalues) {
				scanFolder( path, c, w );
			}
		}

	}

}