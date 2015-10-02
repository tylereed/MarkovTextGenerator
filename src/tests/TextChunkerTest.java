package tests;
import static helpers.AssertHelper.assertIterableOfCollectionEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.TextChunker;

import org.junit.Before;
import org.junit.Test;


public class TextChunkerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIterator_Chunk2() {
		ArrayList<String> words = new ArrayList<>();
		words.add("foo");
		words.add("bar");
		words.add("xyzzy");
		words.add("asdf");
		words.add("zxcv");
		words.add("qwerty");
		
		TextChunker target = new TextChunker(words, 2);
		
		ArrayList<List<String>> expected = new ArrayList<>();
		expected.add(Arrays.asList("foo", "bar"));
		expected.add(Arrays.asList("bar", "xyzzy"));
		expected.add(Arrays.asList("xyzzy", "asdf"));
		expected.add(Arrays.asList("asdf", "zxcv"));
		expected.add(Arrays.asList("zxcv", "qwerty"));
		
		assertIterableOfCollectionEquals(expected, target);
	}

	@Test
	public void testIterator_Chunk3() {
		ArrayList<String> words = new ArrayList<>();
		words.add("foo");
		words.add("bar");
		words.add("xyzzy");
		words.add("asdf");
		words.add("zxcv");
		words.add("qwerty");
		
		TextChunker target = new TextChunker(words, 3);
		
		ArrayList<List<String>> expected = new ArrayList<>();
		expected.add(Arrays.asList("foo", "bar", "xyzzy"));
		expected.add(Arrays.asList("bar", "xyzzy", "asdf"));
		expected.add(Arrays.asList("xyzzy", "asdf", "zxcv"));
		expected.add(Arrays.asList("asdf", "zxcv", "qwerty"));
		
		assertIterableOfCollectionEquals(expected, target);
	}

}
