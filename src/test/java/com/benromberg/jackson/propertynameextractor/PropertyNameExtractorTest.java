package com.benromberg.jackson.propertynameextractor;

import static com.benromberg.jackson.propertynameextractor.PropertyNameExtractor.extractPropertyName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PropertyNameExtractorTest {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static final String RENAMED_PROPERTY = "renamed_property";

	@Test
	public void extractsPropertyName_FromJsonPropertyAnnotationValue() throws Exception {
		assertThat(extractPropertyName(MAPPER, DummyClass.class, DummyClass::getRenamedProperty)).isEqualTo(
				RENAMED_PROPERTY);
	}

	@Test
	public void extractsNothing_FromIgnoredProperty() throws Exception {
		assertThatThrownBy(() -> extractPropertyName(MAPPER, DummyClass.class, DummyClass::getIgnoredProperty))
				.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	public void extractsPropertyName_FromJsonPropertyAnnotationValue_ForClassWithNonDefaultConstructor()
			throws Exception {
		assertThat(
				extractPropertyName(MAPPER, DummyClassWithConstructor.class, DummyClassWithConstructor::getRenamedProperty))
				.isEqualTo(RENAMED_PROPERTY);
	}

	public static class DummyClass {
		@JsonProperty(RENAMED_PROPERTY)
		private String renamedProperty;

		@JsonIgnore
		private String ignoredProperty;

		public String getRenamedProperty() {
			return renamedProperty;
		}

		public String getIgnoredProperty() {
			return ignoredProperty;
		}
	}

	public static class DummyClassWithConstructor {
		public DummyClassWithConstructor(@SuppressWarnings("unused") int a) {
		}

		@JsonProperty(RENAMED_PROPERTY)
		private String renamedProperty;

		public String getRenamedProperty() {
			return renamedProperty;
		}
	}
}
