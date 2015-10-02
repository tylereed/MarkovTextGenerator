package helpers;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.Iterables;

import static org.junit.Assert.*;

public class AssertHelper {

	private AssertHelper() {
	}

	private static <E> void assertSequenceHelper(Integer iteration,
			Iterable<E> expected, Iterable<E> actual) {
		ArrayList<E> _expected = new ArrayList<>();
		Iterables.addAll(_expected, expected);

		ArrayList<E> _actual = new ArrayList<>();
		Iterables.addAll(_actual, actual);

		assertEquals(_expected.size(), _actual.size());

		for (int i = 0; i < _expected.size(); ++i) {
			String message;
			if (iteration == null) {
				message = String.format("Items at index %d do not match.", i);
			} else {
				message = String.format("Items at index [%d, %d] do not match",
						iteration, i);
			}
			assertEquals(message, _expected.get(i), _actual.get(i));
		}

	}

	public static <E> void assertSequenceEquals(Iterable<E> expected,
			Iterable<E> actual) {
		assertSequenceHelper(null, expected, actual);
	}

	public static <E extends Collection<T>, F extends Collection<T>, T> void assertIterableOfCollectionEquals(
			Iterable<E> expected, Iterable<F> actual) {
		ArrayList<E> _expected = new ArrayList<>();
		Iterables.addAll(_expected, expected);

		ArrayList<F> _actual = new ArrayList<>();
		Iterables.addAll(_actual, actual);

		assertEquals(_expected.size(), _actual.size());
		for (int i = 0; i < _expected.size(); ++i) {
			assertSequenceHelper(i, _expected.get(i), _actual.get(i));
		}
	}

}
