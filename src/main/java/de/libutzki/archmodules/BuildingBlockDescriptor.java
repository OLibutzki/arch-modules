package de.libutzki.archmodules;

import com.tngtech.archunit.core.domain.JavaClass;

public interface BuildingBlockDescriptor {
	BuildingBlockType type();

	boolean isBuildingBlock(JavaClass javaClass);

}
