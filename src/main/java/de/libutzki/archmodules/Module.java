package de.libutzki.archmodules;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Module {

	private final String name;
	@EqualsAndHashCode.Exclude
	private final Set<ArchDocClass> classes = new HashSet<>();

	Module(final String name) {
		this.name = name;
	}

	void assignClass(final ArchDocClass archDocClass) {
		addClass(archDocClass);
		archDocClass.setModule(this);
	}

	void addClass(final ArchDocClass archDocClass) {
		classes.add(archDocClass);
	}

	public String getName() {
		return name;
	}

	public Set<BuildingBlock> buildingBlocks() {
		return classes
				.stream()
				.filter(BuildingBlock.class::isInstance)
				.map(BuildingBlock.class::cast)
				.collect(Collectors.toSet());
	}

	public Set<BuildingBlock> buildingBlocksOf(final BuildingBlockType buildingBlockType) {
		return buildingBlocks()
				.stream()
				.filter(buildingBlock -> buildingBlock.getType().equals(buildingBlockType))
				.collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return name;
	}

}
