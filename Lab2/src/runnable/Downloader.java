package runnable;

import java.io.IOException;

public class Downloader {
	
	public static void main(String[] args) throws IOException {
		System.out.println("*** Runnable ***");
		if(args.length != 2) {
			System.err.println("Please enter homepage and number threads");
			System.exit(1);
		}
		int numThreads = 0;
		try {
			numThreads = Integer.parseInt(args[1]);
			if(numThreads <= 0) {
				System.out.println("Please enter positive nbr");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("Please enter positive nbr");
			System.exit(1);
		}
		WorkMaster master = new WorkMaster(args[0]);
		for(int i = 0; i < numThreads; i++)
			(new Thread(new DownloadWorker(master))).start();
	}
}
