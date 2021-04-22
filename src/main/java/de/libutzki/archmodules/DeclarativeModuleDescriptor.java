package de.libutzki.archmodules;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeclarativeModuleDescriptor implements ModuleDescriptor {

	private final Function<? super JavaClass, Optional<String>> selector;
	private final Function<? super JavaClass, Stream<JavaClass>> containedClassesSelector;

	@RequiredArgsConstructor
	static class DeclarativeModuleDescriptorWithSelector {

		private final Function<? super JavaClass, Optional<String>> selector;

		public DeclarativeModuleDescriptor containing(final Function<? super JavaClass, Stream<JavaClass>> containedClassesSelector) {
			return new DeclarativeModuleDescriptor(selector, containedClassesSelector);
		}
	}

	@Override
	public String toString() {
		return "ModuleDescriptor";
	}

	@Override
	public Optional<String> moduleName(final JavaClass javaClass) {
		return selector.apply(javaClass);
	}

	@Override
	public Stream<JavaClass> containedClasses(final JavaClass javaClass) {
		return containedClassesSelector.apply(javaClass);
	}
}
