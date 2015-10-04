package com.benromberg.jackson.propertynameextractor;

import java.util.function.Function;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;

public class PropertyNameExtractor {
	public static <T, U> String extractPropertyName(ObjectMapper objectMapper, Class<T> clazz, Function<T, U> getter) {
		String getterName = extractGetterName(clazz, getter);
		BeanDescription beanDescription = objectMapper.getSerializationConfig().introspect(SimpleType.construct(clazz));
		return beanDescription.findProperties().stream()
				.filter(property -> property.getGetter().getName().equals(getterName)).findFirst().get().getName();
	}

	private static <T, U> String extractGetterName(Class<T> clazz, Function<T, U> getter) {
		MethodNameRecorder<T> recorder = new MethodNameRecorder<T>(clazz);
		getter.apply(recorder.getObject());
		return recorder.getLastCalledMethodName();
	}
}
