package de.libutzki.archmodules;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Relationship {

	private final RelationshipIdentifier identifier;
	private final ArchDocClass source;
	private final BuildingBlock target;

	Relationship(final ArchDocClass source, final BuildingBlock target, final RelationshipIdentifier identifier) {
		this.source = source;
		this.source.addRelationshipAsSource(this);
		this.target = target;
		this.target.addRelationshipAsTarget(this);
		this.identifier = identifier;
	}

	public ArchDocClass getSource() {
		return source;
	}

	public BuildingBlock getTarget() {
		return target;
	}

	public RelationshipIdentifier getIdentifier() {
		return identifier;
	}

	@Override
	public String toString() {
		return source + " " + identifier.getRole() + " " + target;
	}
}
