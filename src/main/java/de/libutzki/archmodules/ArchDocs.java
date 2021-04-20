package de.libutzki.archmodules;

public final class ArchDocs {

	private ArchDocs() {
	}

	static final BuildingBlockDescriptor.BuildingBlockDescriptorWithType buildingBlock(final BuildingBlockType type) {
		return new BuildingBlockDescriptor.BuildingBlockDescriptorWithType(type);
	}

	static final RelationshipDescriptor.RelationshipDescriptorWithIdentifier relationship(final RelationshipIdentifier identifier) {
		return new RelationshipDescriptor.RelationshipDescriptorWithIdentifier(identifier);
	}
}
