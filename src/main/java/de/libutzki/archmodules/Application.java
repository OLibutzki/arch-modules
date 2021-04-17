package de.libutzki.archmodules;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

public class Application {

	public final Set<BuildingBlock> buildingBlocks;
	public final Set<Relationship> relationships;
	public final ModuleAssignment moduleAssignment;

	@Builder
	private Application(
			final @NonNull JavaClasses javaClasses,
			final @Singular Set<BuildingBlockDescriptor> buildingBlockDescriptors,
			final @Singular Set<RelationshipDescriptor> relationshipDescriptors,
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

		relationships = relationshipDescriptors
				.stream()
				.flatMap(buildingBlockDescriptor -> toRelationships(buildingBlockDescriptor))
				.collect(Collectors.toSet());

	}

	private Stream<BuildingBlock> toBuildingBlocks(final BuildingBlockDescriptor buildingBlockDescriptor, final JavaClasses javaClasses) {
		return javaClasses
				.stream()
				.filter(buildingBlockDescriptor.selector)
				.map(javaClass -> toBuildingBlock(javaClass, buildingBlockDescriptor));
	}

	private BuildingBlock toBuildingBlock(final JavaClass javaClass, final BuildingBlockDescriptor buildingBlockDescriptor) {
		return BuildingBlock.builder()
				.name(buildingBlockDescriptor.name)
				.module(moduleAssignment.getModuleNameFor(javaClass).orElse(null))
				.javaClass(javaClass)
				.build();
	}

	private Stream<Relationship> toRelationships(final RelationshipDescriptor relationshipDescriptor) {

		return buildingBlocks
				.stream()
				.filter(buildingBlock -> buildingBlock.getName().equals(relationshipDescriptor.getTargetBuildingBlockName()))
				.flatMap(buildingBlock -> relationshipDescriptor.getSourceSelector().apply(buildingBlock.getJavaClass())
						.map(sourceJavaClass -> toRelationship(sourceJavaClass, buildingBlock, relationshipDescriptor)));

	}

	private Relationship toRelationship(final JavaClass sourceJavaClass, final BuildingBlock targetBuildingBlock, final RelationshipDescriptor relationshipDescriptor) {
		return Relationship.builder()
				.name(relationshipDescriptor.getName())
				.source(sourceJavaClass)
				.sourceModule(moduleAssignment.getModuleNameFor(sourceJavaClass).orElse(null))
				.targetBuildingBlock(targetBuildingBlock)
				.build();
	}
}
