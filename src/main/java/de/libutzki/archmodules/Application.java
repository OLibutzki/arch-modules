package de.libutzki.archmodules;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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

		final Set<BuildingBlockType> duplicateBuildingBlockTypes = buildingBlockDescriptors
				.stream()
				.collect(Collectors.groupingBy(BuildingBlockDescriptor::getType, Collectors.counting()))
				.entrySet()
				.stream()
				.filter(count -> count.getValue() > 1)
				.map(Map.Entry::getKey)
				.collect(toSet());

		if (!duplicateBuildingBlockTypes.isEmpty()) {
			throw new IllegalArgumentException(
					"For the following BuildingBlockTypes multiple descriptors have been registered: " + buildingBlockDescriptors.stream().map(Object::toString).collect(Collectors.joining(", ")));
		}

		final Map<JavaClass, ArchDocClass> archDocClassLookup = toArchDocClasses(javaClasses, buildingBlockDescriptors)
				.collect(Collectors.toMap(ArchDocClass::getJavaClass, Function.identity()));

		final Map<String, List<JavaClass>> moduleAssignmentMap = javaClasses.stream()
				.filter(javaClass -> moduleAssignment.getModuleNameFor(javaClass).isPresent())
				.collect(Collectors.groupingBy(javaClass -> moduleAssignment.getModuleNameFor(javaClass).get()));

		moduleAssignmentMap.forEach((moduleName, moduleClasses) -> {
			final Module module = new Module(moduleName);
			moduleClasses
					.stream()
					.map(javaClass -> archDocClassLookup.get(javaClass))
					.forEach(archDocClass -> archDocClass.assignModule(module));
		});

		buildingBlocks = archDocClassLookup.values()
				.stream()
				.filter(BuildingBlock.class::isInstance)
				.map(BuildingBlock.class::cast)
				.collect(toSet());

		relationships = relationshipDescriptors
				.stream()
				.flatMap(buildingBlockDescriptor -> toRelationships(buildingBlockDescriptor, archDocClassLookup))
				.collect(toSet());

	}

	private Stream<ArchDocClass> toArchDocClasses(final JavaClasses javaClasses, final Set<BuildingBlockDescriptor> buildingBlockDescriptors) {
		return javaClasses
				.stream()
				.map(javaClass -> toArchDocClass(javaClass, buildingBlockDescriptors));
	}

	private ArchDocClass toArchDocClass(final JavaClass javaClass, final Set<BuildingBlockDescriptor> buildingBlockDescriptors) {
		return buildingBlockDescriptors
				.stream()
				.filter(buildingBlockDescriptor -> buildingBlockDescriptor.getSelector().test(javaClass))
				.collect(collectingAndThen(toList(), descriptors -> {
					if (descriptors.size() == 1) {
						return Optional.of(descriptors.get(0));
					}
					if (descriptors.isEmpty()) {
						return Optional.<BuildingBlockDescriptor>empty();
					}
					throw new IllegalStateException(
							"For " + javaClass + "multiple BuildingBlock Descriptors feel responsible: " + buildingBlockDescriptors.stream().map(Object::toString).collect(Collectors.joining(", ")));
				}))
				.map(buildingBlockDescriptor -> toBuildingBlock(javaClass, buildingBlockDescriptor))
				.orElseGet(() -> toArbitraryClass(javaClass));
	}

	private ArchDocClass toBuildingBlock(final JavaClass javaClass, final BuildingBlockDescriptor buildingBlockDescriptor) {
		return new BuildingBlock(javaClass, buildingBlockDescriptor.getType());
	}

	private ArchDocClass toArbitraryClass(final JavaClass javaClass) {
		return new ArbitraryClass(javaClass);
	}

	private Stream<Relationship> toRelationships(final RelationshipDescriptor relationshipDescriptor, final Map<JavaClass, ArchDocClass> archDocClassLookup) {

		return buildingBlocks
				.stream()
				.filter(buildingBlock -> buildingBlock.getType().equals(relationshipDescriptor.getTargetBuildingBlockType()))
				.flatMap(buildingBlock -> relationshipDescriptor.getSourceSelector().apply(buildingBlock.getJavaClass())
						.map(sourceJavaClass -> toRelationship(archDocClassLookup.get(sourceJavaClass), buildingBlock, relationshipDescriptor)));

	}

	private Relationship toRelationship(final ArchDocClass sourceClass, final BuildingBlock targetBuildingBlock, final RelationshipDescriptor relationshipDescriptor) {
		return new Relationship(sourceClass, targetBuildingBlock, relationshipDescriptor.getIdentifier());
	}
}
