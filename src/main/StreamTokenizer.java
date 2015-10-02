package main;

import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Splits a Stream of Strings on whitspace, and returns each word,
 * filtering out any "word" that is entirely punctuation
 * @author Tyler
 */
public class StreamTokenizer implements Iterable<String> {
	
	private static Pattern whitespaceSplitter;
	
	private static Pattern hasAlphaCharacter;
	
	private Stream<String> input;
	
	static {
		whitespaceSplitter = Pattern.compile("\\s+");
		hasAlphaCharacter = Pattern.compile("\\w");
	}
	
	public StreamTokenizer(Stream<String> input) {
		this.input = input;
	}

	@Override
	public Iterator<String> iterator() {
		return input
		  .flatMap(whitespaceSplitter::splitAsStream)
		  .filter(s -> s.length() != 0)
		  .filter(s -> hasAlphaCharacter.matcher(s).lookingAt())
		  .iterator();
	}

}
