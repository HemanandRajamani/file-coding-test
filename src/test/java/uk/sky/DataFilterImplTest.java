package uk.sky;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author hemanand.rajamani
 * created on 26/11/20
 */
class DataFilterImplTest {

	private DataFilterer underTest;
	private Reader source;

	@BeforeEach
	void setUp() throws FileNotFoundException {
		underTest = new DataFilterImpl();
		source = new FileReader("src/test/resources/sample-extract");
	}

	@Test
	void testFilterByCountry_Success() {
		Collection<Extract> result = underTest.filterByCountry(source, "US");
		assertEquals(3, result.size());
		assertEquals(3, result.stream().filter(extract -> extract.getCountry().equals("US")).count());
	}

	@Test
	void testFilterByCountry_WhenSourceIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.filterByCountry(null, "US"));
		assertEquals("source cannot be empty!", exception.getMessage());
	}

	@Test
	void testFilterByCountry_WhenCountryIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.filterByCountry(source, null));
		assertEquals("country cannot be null or empty!", exception.getMessage());
	}

	@Test
	void testFilterByCountry_WhenCountryIsBlank() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.filterByCountry(source, "  "));
		assertEquals("country cannot be null or empty!", exception.getMessage());
	}

	@Test
	void testFilterByCountryResponseTimeAboveLimit_Success() {
		Collection<Extract> result = underTest.filterByCountryWithResponseTimeAboveLimit(source, "US", 600L);
		assertEquals(2, result.size());
		assertEquals(2, result.stream().filter(extract -> extract.getCountry().equals("US")).count());
	}

	@Test
	void testFilterByResponseTimeAboveAverage_Success() {
		Collection<Extract> result = underTest.filterByResponseTimeAboveAverage(source);
		assertEquals(3, result.size());
	}

	@Test
	void testFilterByResponseTimeAboveAverage_InvalidSource() {
		IllegalStateException exception = assertThrows(IllegalStateException.class,
				() -> underTest.filterByResponseTimeAboveAverage(new FileReader("src/test/resources/sample-extract-erroneous")));
		assertTrue(exception.getMessage().contains("Failed to read characters from source!"));
	}

	@Test
	void testFilterByResponseTimeAboveAverage_WhenSourceIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.filterByResponseTimeAboveAverage(null));
		assertEquals("source cannot be empty!", exception.getMessage());
	}
}
