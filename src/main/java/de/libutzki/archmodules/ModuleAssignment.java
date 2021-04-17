package de.libutzki.archmodules;

import java.util.Optional;

import com.tngtech.archunit.core.domain.JavaClass;

@FunctionalInterface
public interface ModuleAssignment {
	Optional<String> getModuleNameFor(JavaClass javaClass);

	ModuleAssignment NoOpModuleAssignment = new NoOpModuleAssignment();

	static class NoOpModuleAssignment implements ModuleAssignment {

		@Override
		public Optional<String> getModuleNameFor(final JavaClass javaClass) {
			return Optional.empty();
		}

	}
}
