Can extract the @JsonProperty name by referencing the getter of the corresponding field using a method reference in Java 8.

Example:

```java
public class PropertyNameExtractorTest {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static final String RENAMED_PROPERTY = "renamed_property";

	@Test
	public void extractsPropertyName_FromJsonPropertyAnnotationValue() throws Exception {
		String extractedProperty = PropertyNameExtractor.extractPropertyName(MAPPER, DummyClass.class, DummyClass::getRenamedProperty);
		assertThat(extractedProperty).isEqualTo(RENAMED_PROPERTY);
	}

	public static class DummyClass {
		@JsonProperty(RENAMED_PROPERTY)
		private String renamedProperty;

		public String getRenamedProperty() {
			return renamedProperty;
		}
	}
```

The implementation uses Jackson to resolve the property name as it would be used when serializing the object and has no logic to do so itself. Also, Objenesis is used to avoid any constructor calls on any involved objects.

If no attached property can be found, a `NoSuchElementException` is thrown.

Be aware that this is a proof of concept implementation and no performance considerations have been made. Please test thoroughly before using in production!

This implementation *should* be thread-safe, but since I'm not a Objenesis/cglib expert it would be nice if someone who is more knowledgeable about these frameworks could confirm it.
