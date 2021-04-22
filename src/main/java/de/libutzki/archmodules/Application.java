package de.libutzki.archmodules;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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
	public final Set<Module> modules;

	@Builder
	private Application(
			final @NonNull JavaClasses javaClasses,
			final @Singular Set<BuildingBlockDescriptor> buildingBlockDescriptors,
			final @Singular Set<RelationshipDescriptor> relationshipDescriptors,
			final ModuleDescriptor moduleDescriptor) {

		final Set<BuildingBlockType> duplicateBuildingBlockTypes = buildingBlockDescriptors
				.stream()
				.collect(Collectors.groupingBy(BuildingBlockDescriptor::type, Collectors.counting()))
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

		if (moduleDescriptor != null) {
			modules = javaClasses
					.stream()
					.map(javaClass -> moduleDescriptor.moduleName(javaClass)
							.map(moduleName -> {
								final Module module = new Module(moduleName);
								moduleDescriptor.containedClasses(javaClass)
										.map(archDocClassLookup::get)
										.forEach(module::assignClass);
								return module;
							}))
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(toSet());
		} else {
			modules = emptySet();
		}

		buildingBlocks = archDocClassLookup.values()
				.stream()
				.filter(BuildingBlock.class::isInstance)
				.map(BuildingBlock.class::cast)
				.collect(toSet());

		relationships = relationshipDescriptors
				.stream()
				.flatMap(relationshipDescriptor -> toRelationships(relationshipDescriptor, archDocClassLookup))
				.collect(toSet());

	}

	public Set<BuildingBlock> buildingBlocksOf(final BuildingBlockType buildingBlockType) {
		return buildingBlocks
				.stream()
				.filter(buildingBlock -> buildingBlock.getType().equals(buildingBlockType))
				.collect(toSet());
	}

	public Set<Relationship> relationshipsOf(final RelationshipIdentifier relationshipIdentifier) {
		return relationships
				.stream()
				.filter(relationship -> relationship.getIdentifier().equals(relationshipIdentifier))
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
				.filter(buildingBlockDescriptor -> buildingBlockDescriptor.isBuildingBlock(javaClass))
				.collect(collectingAndThen(toList(), descriptors -> {
					if (descriptors.size() == 1) {
						return Optional.of(descriptors.get(0));
					}
					if (descriptors.isEmpty()) {
						return Optional.<DeclarativeBuildingBlockDescriptor>empty();
					}
					throw new IllegalStateException(
							"For " + javaClass + "multiple BuildingBlock Descriptors feel responsible: " + buildingBlockDescriptors.stream().map(Object::toString).collect(Collectors.joining(", ")));
				}))
				.map(buildingBlockDescriptor -> toBuildingBlock(javaClass, buildingBlockDescriptor))
				.orElseGet(() -> toArbitraryClass(javaClass));
	}

	private ArchDocClass toBuildingBlock(final JavaClass javaClass, final BuildingBlockDescriptor buildingBlockDescriptor) {
		return new BuildingBlock(javaClass, buildingBlockDescriptor.type());
	}

	private ArchDocClass toArbitraryClass(final JavaClass javaClass) {
		return new ArbitraryClass(javaClass);
	}

	private Stream<Relationship> toRelationships(final RelationshipDescriptor relationshipDescriptor, final Map<JavaClass, ArchDocClass> archDocClassLookup) {

		return buildingBlocks
				.stream()
				.filter(buildingBlock -> buildingBlock.getType().equals(relationshipDescriptor.targetBuildingBlockType()))
				.flatMap(buildingBlock -> relationshipDescriptor.sources(buildingBlock.getJavaClass())
						.map(sourceJavaClass -> toRelationship(archDocClassLookup.get(sourceJavaClass), buildingBlock, relationshipDescriptor)));

	}

	private Relationship toRelationship(final ArchDocClass sourceClass, final BuildingBlock targetBuildingBlock, final RelationshipDescriptor relationshipDescriptor) {
		return new Relationship(sourceClass, targetBuildingBlock, relationshipDescriptor.identifier());
	}

}
