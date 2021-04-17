package de.libutzki.archmodules;

public final class ArchDocs {

	private ArchDocs() {
	}

	static final BuildingBlockDescriptor.BuildingBlockDescriptorWithName buildingBlock(final String name) {
		return new BuildingBlockDescriptor.BuildingBlockDescriptorWithName(name);
	}

	static final RelationshipDescriptor.RelationshipDescriptorWithName relationship(final String name) {
		return new RelationshipDescriptor.RelationshipDescriptorWithName(name);
	}
}
