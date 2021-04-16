package de.libutzki.archmodules;

import com.tngtech.archunit.core.domain.JavaClass;

public interface ModuleAssignment {
	ModuleIdentifier getModuleFor(JavaClass javaClass);

	ModuleAssignment NoOpModuleAssignment = new NoOpModuleAssignment();

	static class NoOpModuleAssignment implements ModuleAssignment {

		@Override
		public ModuleIdentifier getModuleFor(final JavaClass javaClass) {
			return ModuleIdentifier.NONE;
		}

	}
}
