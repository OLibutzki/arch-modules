package de.libutzki.archmodules;

import java.util.function.Function;
import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RelationshipDescriptor {
	private final String name;
	private final String targetBuildingBlockName;
	private final Function<? super JavaClass, Stream<JavaClass>> sourceSelector;

	@RequiredArgsConstructor
	static class RelationshipDescriptorWithName {

		private final String name;

		public RelationshipDescriptorWithTarget to(final String targetBuildingBlockName) {
			return new RelationshipDescriptorWithTarget(name, targetBuildingBlockName);
		}
	}

	@RequiredArgsConstructor
	static class RelationshipDescriptorWithTarget {

		private final String name;
		private final String targetBuildingBlockName;

		public RelationshipDescriptor from(final Function<? super JavaClass, Stream<JavaClass>> sourceSelector) {
			return new RelationshipDescriptor(name, targetBuildingBlockName, sourceSelector);
		}

	}
}
