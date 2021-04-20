package de.libutzki.archmodules;

import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
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
import de.libutzki.archmodules.sample.library.Module;

public class ArchModulesTest {

	@Test
	void test() {

		final JavaClasses javaClasses = new ClassFileImporter().importPackages("de.libutzki.archmodules.sample");

		final BuildingBlockType event = BuildingBlockType.of("Event");
		final Application application = Application.builder()
				.javaClasses(javaClasses)
				.buildingBlockDescriptor(buildingBlock(event).definedBy(this::event))
				.relationshipDescriptor(relationship(RelationshipRole.of("handles")).to(event).from(this::eventHandlers))
				.relationshipDescriptor(relationship(RelationshipRole.of("emits")).to(event).from(this::eventEmitters))
				.moduleAssignment(moduleAssignment(javaClasses))
				.build();

		application.buildingBlocks.forEach(System.out::println);
		application.relationships.forEach(System.out::println);
	}

	private ModuleAssignment moduleAssignment(final JavaClasses javaClasses) {
		return javaClass -> javaClasses.that(annotatedWith(Module.class))
				.stream()
				.filter(moduleDescriptor -> moduleDescriptor.getPackage().getAllClasses().contains(javaClass))
				.findFirst()
				.map(moduleDescriptor -> moduleDescriptor.getAnnotationOfType(Module.class).value());

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
