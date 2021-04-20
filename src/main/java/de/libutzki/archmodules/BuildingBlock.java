package de.libutzki.archmodules;

import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Set;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class BuildingBlock extends ArchDocClass {

	private final BuildingBlockType type;

	@EqualsAndHashCode.Exclude
	private final Set<Relationship> relationshipsAsTarget = new HashSet<>();

	BuildingBlock(final JavaClass javaClass, final BuildingBlockType type) {
		super(javaClass);
		this.type = type;
	}

	void addRelationshipAsTarget(final Relationship relationship) {
		relationshipsAsTarget.add(relationship);
	}

	public BuildingBlockType getType() {
		return type;
	}

	public Set<Relationship> getRelationshipsAsTarget() {
		return unmodifiableSet(relationshipsAsTarget);
	}

	@Override
	public String toString() {
		return "<" + type.getName() + "> " + getJavaClass().getName() + " (" + getModule().map(moduleName -> "module: " + moduleName).orElse("no module") + ")";
	}

}
