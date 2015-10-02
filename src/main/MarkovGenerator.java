package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.StringJoiner;

import com.google.common.collect.AbstractIterator;

/**
 * Generates Markov chains from text
 * @author Tyler Reed
 */
public class MarkovGenerator implements Iterable<String> {

	private final Map<String, List<String>> words;

	private final int chunkSize;

	/**
	 * Constructor that takes a size of chains and an array of file names
	 * @param chunkSize The size of the chain used to calculate the next item
	 * @param files Array of files to generate the chains from
	 */
	public MarkovGenerator(int chunkSize, String... files) {
		this.chunkSize = chunkSize;

		this.words = new Hashtable<>();

		for (String file : files) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				StreamTokenizer tokenizer = new StreamTokenizer(reader.lines());
				TextChunker chunker = new TextChunker(tokenizer, this.chunkSize);

				loadChain(chunker, this.chunkSize);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Dumps the words, and the next possible set of words to standard out
	 */
	public void dumpWords() {
		dumpWords(System.out);
	}

	/**
	 * Dumps the words, and the next possible set of words, to the parameter
	 * @param out Where to write all the words
	 */
	public void dumpWords(PrintStream out) {
		for (Entry<String, List<String>> chunk : words.entrySet()) {
			out.println(chunk.getKey());
			out.println(String.join(" ", chunk.getValue()));
			out.println();
		}
		out.flush();
	}

	/**
	 * Dumps all counts for the next sizes, in the form of "size\tcounts" to standard out
	 */
	public void dumpCounts() {
		dumpCounts(System.out);
	}

	/**
	 * Dumps all counts for the next sizes, in the form of "size\tcounts" to specified parameter
	 * @param out Where to write all the words
	 */
	public void dumpCounts(PrintStream out) {
		Hashtable<Integer, Integer> counts = new Hashtable<Integer, Integer>();
		for (List<String> nexts : words.values()) {
			Integer size = nexts.size();
			Integer count = counts.getOrDefault(size, 0);
			counts.put(size, ++count);
		}

		out.println(words.size());
		for (Entry<Integer, Integer> count : counts.entrySet()) {
			out.print(count.getKey());
			out.print(" ");
			out.println(count.getValue());
		}
		out.flush();
	}

	/**
	 * Loads wordChunks into the generator, at the specified size
	 * @param wordChunks An iterable of List of Strings
	 * @param groupSize The size of the chains to generate
	 * @return A Map, with a key of preceding words (separated by a pipe "|") with values being a List of Strings of the next words in the chain
	 */
	public Map<String, List<String>> loadChain(Iterable<? extends List<String>> wordChunks, int groupSize) {
		final int chainSize = groupSize - 1;

		for (List<String> chunk : wordChunks) {
			StringJoiner joiner = new StringJoiner("|");
			for (int i = 0; i < chainSize; ++i) {
				joiner.add(chunk.get(i));
			}
			String key = joiner.toString();
			String next = chunk.get(chainSize);

			if (!words.containsKey(key)) {
				List<String> value = new ArrayList<String>();
				words.put(key, value);
			}

			List<String> value = words.get(key);
			value.add(next);
		}

		return words;
	}

	/**
	 * Gets a random String entry from the input parameter
	 * @param source The source from which to pull the data
	 * @return A random entry from the source
	 */
	public String getRandom(Collection<String> source) {
		int size = source.size();

		int index = (int) Math.floor(Math.random() * size);

		int i = 0;
		for (String s : source) {
			if (i == index) {
				return s;
			}
			++i;
		}

		return null;
	}

	@Override
	public Iterator<String> iterator() {
		String seed = getRandom(words.keySet());
		return new MarkovIterator(seed);
	}

	/**
	 * Class that handles the internal logic of generating the chains in the Markov iterator
	 * @author Tyler Reed
	 */
	private class MarkovIterator extends AbstractIterator<String> {

		/**
		 * State in which the iterator hasn't exhausted the seed the iterator starts with
		 */
		private static final int INIT = 0;

		/**
		 * State in which the iterator is picking random chains
		 */
		private static final int GENERATE = 1;

		/**
		 * The entry the iterator was seeded with
		 */
		private String seed;

		/**
		 * The initial items from the seed
		 */
		private String[] initial;

		/**
		 * The location the chain is in in the initial seed array
		 */
		private int index;

		/**
		 * The current state of how to generate the chains
		 */
		private int state;

		/**
		 * Creates a new iterator, pulling the next chain from seed
		 * @param seed The pipe separated initial entries in the chain
		 */
		public MarkovIterator(String seed) {
			this.seed = seed;
			this.index = -1;
			this.initial = seed.split("\\|");
			this.state = INIT;
		}

		/**
		 * Returns the next String in the chain
		 */
		@Override
		protected String computeNext() {

			switch (state) {
			case INIT:
				if (++index < initial.length) {
					return initial[index];
				}
				state = GENERATE;
				// fall through if initial is exhausted
			case GENERATE:
				Optional<String> next = getRandom(words.get(seed));

				if (next.isPresent()) {

					String[] preceding = seed.split("\\|");
					StringJoiner joiner = new StringJoiner("|");
					for (int i = 1; i < preceding.length; ++i) {
						joiner.add(preceding[i]);
					}
					joiner.add(next.get());
					seed = joiner.toString();

					return next.get();
				}

			default:
				return endOfData();
			}

		}

		/**
		 * Chooses a random entry from the input list.
		 * @param possible The items that can be next.
		 * @return The next item in the chain, or an empty Optional if the chain has ended.
		 */
		private Optional<String> getRandom(List<String> possible) {
			if (possible == null || possible.size() == 0) {
				return Optional.empty();
			}

			int random = (int) Math.floor(Math.random() * possible.size());
			return Optional.of(possible.get(random));
		}

	}

}
