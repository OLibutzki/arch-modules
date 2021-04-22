package de.libutzki.archmodules;

import static de.libutzki.archmodules.ArchDocs.buildingBlock;
import static de.libutzki.archmodules.ArchDocs.relationship;

import java.util.Optional;
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
				.relationshipDescriptor(relationship(RelationshipIdentifier.of("handles", event)).from(this::eventHandlers))
				.relationshipDescriptor(relationship(RelationshipIdentifier.of("emits", event)).from(this::eventEmitters))
				.moduleDescriptor(ArchDocs.modulesFrom(this::module).containing(this::containing))
				.build();

		application.buildingBlocks.forEach(System.out::println);
		application.relationships.forEach(System.out::println);
	}

	private Stream<JavaClass> containing(final JavaClass javaClass) {
		return javaClass.getPackage().getAllClasses().stream();
	}

	private Optional<String> module(final JavaClass javaClass) {
		return javaClass.isAnnotatedWith(Module.class) ? Optional.of(javaClass.getAnnotationOfType(Module.class).value()) : Optional.empty();
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
