package de.libutzki.archmodules;

import java.util.Optional;
import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;

public interface ModuleDescriptor {
	Optional<String> moduleName(JavaClass javaClass);

	Stream<JavaClass> containedClasses(JavaClass javaClass);
}
