package de.libutzki.archmodules;

public final class ArchDocs {

	private ArchDocs() {
	}

	static final BuildingBlockDescriptor.BuildingBlockDescriptorWithType buildingBlock(final BuildingBlockType type) {
		return new BuildingBlockDescriptor.BuildingBlockDescriptorWithType(type);
	}

	static final RelationshipDescriptor.RelationshipDescriptorWithRole relationship(final RelationshipRole role) {
		return new RelationshipDescriptor.RelationshipDescriptorWithRole(role);
	}
}
