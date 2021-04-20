package de.libutzki.archmodules;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Relationship {

	private final ArchDocClass source;
	private final BuildingBlock target;
	private final RelationshipRole role;

	Relationship(final ArchDocClass source, final BuildingBlock target, final RelationshipRole role) {
		this.source = source;
		this.source.addRelationshipAsSource(this);
		this.target = target;
		this.target.addRelationshipAsTarget(this);
		this.role = role;
	}

	public ArchDocClass getSource() {
		return source;
	}

	public BuildingBlock getTarget() {
		return target;
	}

	public RelationshipRole getRole() {
		return role;
	}

	@Override
	public String toString() {
		return source + " " + role.getName() + " " + target;
	}
}
