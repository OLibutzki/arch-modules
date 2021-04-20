package de.libutzki.archmodules;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

	public final Set<ArchDocClass> archDocClasses;
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

		archDocClasses = new HashSet<>(archDocClassLookup.values());

		relationships = relationshipDescriptors
				.stream()
				.flatMap(buildingBlockDescriptor -> toRelationships(buildingBlockDescriptor, archDocClassLookup))
				.collect(Collectors.toSet());

	}

	private Stream<ArchDocClass> toArchDocClasses(final JavaClasses javaClasses, final Set<BuildingBlockDescriptor> buildingBlockDescriptors) {
		return javaClasses
				.stream()
				.map(javaClass -> toArchDocClass(javaClass, buildingBlockDescriptors));
	}

	private ArchDocClass toArchDocClass(final JavaClass javaClass, final Set<BuildingBlockDescriptor> buildingBlockDescriptors) {
		return buildingBlockDescriptors
				.stream()
				.filter(buildingBlockDescriptor -> buildingBlockDescriptor.selector.test(javaClass))
				.findFirst()
				.map(buildingBlockDescriptor -> toBuildingBlock(javaClass, buildingBlockDescriptor))
				.orElseGet(() -> toArbitraryClass(javaClass));
	}

	private ArchDocClass toBuildingBlock(final JavaClass javaClass, final BuildingBlockDescriptor buildingBlockDescriptor) {
		return new BuildingBlock(javaClass, buildingBlockDescriptor.type);
	}

	private ArchDocClass toArbitraryClass(final JavaClass javaClass) {
		return new ArbitraryClass(javaClass);
	}

	private Stream<Relationship> toRelationships(final RelationshipDescriptor relationshipDescriptor, final Map<JavaClass, ArchDocClass> archDocClassLookup) {

		return archDocClasses
				.stream()
				.filter(BuildingBlock.class::isInstance)
				.map(BuildingBlock.class::cast)
				.filter(buildingBlock -> buildingBlock.getType().equals(relationshipDescriptor.getTargetBuildingBlockType()))
				.flatMap(buildingBlock -> relationshipDescriptor.getSourceSelector().apply(buildingBlock.getJavaClass())
						.map(sourceJavaClass -> toRelationship(archDocClassLookup.get(sourceJavaClass), buildingBlock, relationshipDescriptor)));

	}

	private Relationship toRelationship(final ArchDocClass sourceClass, final BuildingBlock targetBuildingBlock, final RelationshipDescriptor relationshipDescriptor) {
		return new Relationship(sourceClass, targetBuildingBlock, relationshipDescriptor.getRole());
	}
}
