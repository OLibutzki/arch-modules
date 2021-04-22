package de.libutzki.archmodules;

import java.util.Optional;
import java.util.function.Function;

import com.tngtech.archunit.core.domain.JavaClass;

public final class ArchDocs {

	private ArchDocs() {
	}

	static final DeclarativeBuildingBlockDescriptor.DeclarativeBuildingBlockDescriptorWithType buildingBlock(final BuildingBlockType type) {
		return new DeclarativeBuildingBlockDescriptor.DeclarativeBuildingBlockDescriptorWithType(type);
	}

	static final RelationshipDescriptor.RelationshipDescriptorWithIdentifier relationship(final RelationshipIdentifier identifier) {
		return new RelationshipDescriptor.RelationshipDescriptorWithIdentifier(identifier);
	}

	static final DeclarativeModuleDescriptor.DeclarativeModuleDescriptorWithSelector modulesFrom(final Function<? super JavaClass, Optional<String>> selector) {
		return new DeclarativeModuleDescriptor.DeclarativeModuleDescriptorWithSelector(selector);
	}
}
