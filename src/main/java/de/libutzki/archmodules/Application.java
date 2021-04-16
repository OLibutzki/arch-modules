package de.libutzki.archmodules;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

@Value
public class Application {

	private final Set<BuildingBlock> buildingBlocks;
	private final ModuleAssignment moduleAssignment;

	@Builder
	private Application(
			final @NonNull JavaClasses javaClasses,
			final @Singular Set<BuildingBlockDescriptor> buildingBlockDescriptors,
			final ModuleAssignment moduleAssignment) {
		if (moduleAssignment != null) {
			this.moduleAssignment = moduleAssignment;
		} else {
			this.moduleAssignment = ModuleAssignment.NoOpModuleAssignment;
		}

		buildingBlocks = buildingBlockDescriptors
				.stream()
				.flatMap(buildingBlockDescriptor -> toBuildingBlocks(buildingBlockDescriptor, javaClasses))
				.collect(Collectors.toSet());
	}

	private Stream<BuildingBlock> toBuildingBlocks(final BuildingBlockDescriptor buildingBlockDescriptor, final JavaClasses javaClasses) {
		return buildingBlockDescriptor.filter(javaClasses.stream())
				.map(javaClass -> toBuildingBlock(javaClass, buildingBlockDescriptor));
	}

	private BuildingBlock toBuildingBlock(final JavaClass javaClass, final BuildingBlockDescriptor buildingBlockDescriptor) {
		return BuildingBlock.builder()
				.name(buildingBlockDescriptor.name())
				.javaClass(javaClass)
				.relationships(buildingBlockDescriptor.relationshipDescriptorsFor(javaClass)
						.map(relationshipDescriptor -> toRelationship(relationshipDescriptor, javaClass))
						.collect(Collectors.toSet()))
				.build();
	}

	private Relationship toRelationship(final RelationshipDescriptor relationshipDescriptor, final JavaClass javaClass) {
		return Relationship.builder()
				.name(relationshipDescriptor.name())
				.javaClasses(relationshipDescriptor
						.relationshipClassesOf(javaClass)
						.collect(Collectors.toSet()))
				.build();

	}

}
