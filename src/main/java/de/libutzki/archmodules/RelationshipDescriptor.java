package de.libutzki.archmodules;

import java.util.function.Function;
import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RelationshipDescriptor {
	private final RelationshipRole role;
	private final BuildingBlockType targetBuildingBlockType;
	private final Function<? super JavaClass, Stream<JavaClass>> sourceSelector;

	@RequiredArgsConstructor
	static class RelationshipDescriptorWithRole {

		private final RelationshipRole role;

		public RelationshipDescriptorWithTarget to(final BuildingBlockType targetBuildingBlockType) {
			return new RelationshipDescriptorWithTarget(role, targetBuildingBlockType);
		}
	}

	@RequiredArgsConstructor
	static class RelationshipDescriptorWithTarget {

		private final RelationshipRole role;
		private final BuildingBlockType targetBuildingBlockType;

		public RelationshipDescriptor from(final Function<? super JavaClass, Stream<JavaClass>> sourceSelector) {
			return new RelationshipDescriptor(role, targetBuildingBlockType, sourceSelector);
		}

	}
}
