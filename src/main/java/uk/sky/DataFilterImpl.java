package uk.sky;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemanand.rajamani
 * created on 26/11/20
 */
public class DataFilterImpl implements DataFilterer {

	@Override
	public List<Extract> filterByCountry(Reader source, String country) {

		if (source == null) {
			throw new IllegalArgumentException("source cannot be empty!");
		}
		if (country == null || country.isBlank()) {
			throw new IllegalArgumentException("country cannot be null or empty!");
		}

		return readAndTransform(source).stream().filter(extract -> extract.getCountry().equals(country)).collect(Collectors.toList());
	}

	@Override
	public List<Extract> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) {

		Collection<Extract> filteredCountries = filterByCountry(source, country);

		return filteredCountries.stream().filter(extract -> extract.getResponseTime().longValue() > limit).collect(Collectors.toList());
	}

	@Override
	public List<Extract> filterByResponseTimeAboveAverage(Reader source) {
		if (source == null) {
			throw new IllegalArgumentException("source cannot be empty!");
		}
		List<Extract> list = readAndTransform(source);
		Double average = calculateAverage(list);
		return list.stream().filter(extract -> extract.getResponseTime() > average).collect(Collectors.toList());
	}

	private Double calculateAverage(List<Extract> list) {
		return list.stream().mapToDouble(Extract::getResponseTime).summaryStatistics().getAverage();
	}

	private List<Extract> readAndTransform(Reader source) {

		List<Extract> list = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(source)) {

			String line;
			int count = 0;

			while ((line = reader.readLine()) != null) {
				if (count == 0) {
					count++;
					continue;
				}
				String[] values = line.split(",");
				Extract ex = new Extract(Long.valueOf(values[0]), values[1], Double.valueOf(values[2]));
				list.add(ex);
			}

		} catch (Exception exception) {
			throw new IllegalStateException("Failed to read characters from source! " + exception.getMessage());
		}
		return list;
	}
}
