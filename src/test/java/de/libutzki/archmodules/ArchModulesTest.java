package de.libutzki.archmodules;

import static de.libutzki.archmodules.ArchDocs.buildingBlock;
import static de.libutzki.archmodules.ArchDocs.relationship;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaConstructorCall;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import de.libutzki.archmodules.sample.library.Event;
import de.libutzki.archmodules.sample.library.EventListener;

public class ArchModulesTest {

	@Test
	void test() {

		final JavaClasses javaClasses = new ClassFileImporter().importPackages("de.libutzki.archmodules.sample");
		final MyModuleAssignment moduleAssignment = new MyModuleAssignment(javaClasses);

		final Application application = Application.builder()
				.javaClasses(javaClasses)
				.buildingBlockDescriptor(buildingBlock("Event").definedBy(this::event))
				.relationshipDescriptor(relationship("handles").to("Event").from(this::eventHandlers))
				.relationshipDescriptor(relationship("emits").to("Event").from(this::eventEmitters))
				.moduleAssignment(moduleAssignment)
				.build();

		application.buildingBlocks.forEach(System.out::println);
		application.relationships.forEach(System.out::println);
	}

	private boolean event(final JavaClass javaClass) {
		return javaClass.isAnnotatedWith(Event.class);
	}

	private Stream<JavaClass> eventHandlers(final JavaClass javaClass) {
		return javaClass.getMethodsWithParameterTypeOfSelf()
				.stream()
				.filter(method -> method.isAnnotatedWith(EventListener.class))
				.filter(method -> method.getRawParameterTypes().get(0).equals(javaClass))
				.map(JavaMethod::getOwner);
	}

	private Stream<JavaClass> eventEmitters(final JavaClass javaClass) {
		return javaClass.getConstructorCallsToSelf()
				.stream()
				.map(JavaConstructorCall::getOriginOwner);
	}
}
