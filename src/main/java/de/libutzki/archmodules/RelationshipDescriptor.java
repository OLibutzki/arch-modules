package de.libutzki.archmodules;

import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;

public interface RelationshipDescriptor {
	String name();

	Stream<JavaClass> relationshipClassesOf(JavaClass javaClass);
}
