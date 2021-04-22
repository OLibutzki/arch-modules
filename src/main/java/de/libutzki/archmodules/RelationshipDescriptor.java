package de.libutzki.archmodules;

import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;

public interface RelationshipDescriptor {

	RelationshipIdentifier identifier();

	String role();

	BuildingBlockType targetBuildingBlockType();

	Stream<JavaClass> sources(JavaClass targetClass);
}
