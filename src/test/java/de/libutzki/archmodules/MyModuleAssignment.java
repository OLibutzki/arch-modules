package de.libutzki.archmodules;

import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

import de.libutzki.archmodules.sample.library.Module;

class MyModuleAssignment implements ModuleAssignment {
	private final JavaClasses moduleDescriptors;

	MyModuleAssignment(final JavaClasses javaClasses) {
		moduleDescriptors = javaClasses.that(annotatedWith(Module.class));
	}

	@Override
	public ModuleIdentifier getModuleFor(final JavaClass javaClass) {
		return moduleDescriptors.stream()
				.filter(moduleDescriptor -> moduleDescriptor.getPackage().getAllClasses().contains(javaClass))
				.findFirst()
				.map(moduleDescriptor -> ModuleIdentifier.of(moduleDescriptor.getAnnotationOfType(Module.class).value()))
				.orElse(ModuleIdentifier.NONE);
	}

}