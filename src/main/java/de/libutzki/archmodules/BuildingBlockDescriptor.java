package de.libutzki.archmodules;

import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;

public interface BuildingBlockDescriptor {

	String name();

	Stream<JavaClass> filter(Stream<JavaClass> javaClasses);

	Stream<RelationshipDescriptor> relationshipDescriptorsFor(JavaClass javaClasses);
}
