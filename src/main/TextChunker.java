package main;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;

/**
 * Class that transforms an Iterable of Strings, grouping the individual items into a larger group
 * @author Tyler Reed
 *
 */
public class TextChunker implements Iterable<ImmutableList<String>> {

	public final int WORD_SIZE;

	private Iterator<String> source;

	/**
	 * Creates a new TextChunker, that generates the chunks from words, in size wordSize
	 * @param words The source to pull from
	 * @param wordSize The size of the iterated lists
	 */
	public TextChunker(Iterable<String> words, int wordSize) {
		source = words.iterator();
		WORD_SIZE = wordSize;
	}

	@Override
	public Iterator<ImmutableList<String>> iterator() {

		// we are adding a dummy entry to the queue, then pre-populating with
		// WORD_SIZE - 1, so the first iteration of the AbstractIterator isn't a
		// special case
		Queue<String> queue = new LinkedList<>();
		queue.add("");
		for (int i = 0; i < WORD_SIZE - 1 && source.hasNext(); ++i) {
			queue.add(source.next());
		}

		return new AbstractIterator<ImmutableList<String>>() {

			@Override
			protected ImmutableList<String> computeNext() {
				if (source.hasNext()) {
					queue.remove();
					queue.add(source.next());
					return ImmutableList.copyOf(queue);
				} else {
					return endOfData();
				}
			}
		};
		
	}
	
}
