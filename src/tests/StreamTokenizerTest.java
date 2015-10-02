package tests;

import static helpers.AssertHelper.assertSequenceEquals;

import java.util.ArrayList;

import main.StreamTokenizer;

import org.junit.Before;
import org.junit.Test;

public class StreamTokenizerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIterator_SplitWhitespace() {
		ArrayList<String> input = new ArrayList<>();
		input.add("a b c\td e f");
		input.add("g h   i\r\nj  kl");

		ArrayList<String> expected = new ArrayList<>();
		expected.add("a");
		expected.add("b");
		expected.add("c");
		expected.add("d");
		expected.add("e");
		expected.add("f");
		expected.add("g");
		expected.add("h");
		expected.add("i");
		expected.add("j");
		expected.add("kl");

		StreamTokenizer target = new StreamTokenizer(input.stream());

		assertSequenceEquals(expected, target);
	}

	@Test
	public void testIterator_SkipsNoAlpha() {
		ArrayList<String> input = new ArrayList<>();
		input.add("a");
		input.add("-----");
		input.add("b");

		ArrayList<String> expected = new ArrayList<>();
		expected.add("a");
		expected.add("b");

		StreamTokenizer target = new StreamTokenizer(input.stream());

		assertSequenceEquals(expected, target);
	}

	@Test
	public void testIterator_SkipsEmpty() {
		ArrayList<String> input = new ArrayList<>();
		input.add("a");
		input.add("");
		input.add("b");

		ArrayList<String> expected = new ArrayList<>();
		expected.add("a");
		expected.add("b");

		StreamTokenizer target = new StreamTokenizer(input.stream());

		assertSequenceEquals(expected, target);
	}

}
