package main;

import java.util.Iterator;

public class Main {

	public static void main(String[] args) {

		MarkovGenerator generator = new MarkovGenerator(4, args);

		//generator.dumpCounts();
		//generator.dumpWords();
		//return;

		Iterator<String> chains = generator.iterator();

		for (int i = 0; i < 5000 && chains.hasNext(); ++i) {
			System.out.print(chains.next());
			if (i % 10 == 9) {
				System.out.println();
			} else {
				System.out.print(" ");
			}
		}

	}
}
