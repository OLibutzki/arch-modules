package de.libutzki.archmodules;

import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaConstructorCall;
import com.tngtech.archunit.core.domain.JavaMethod;

import de.libutzki.archmodules.sample.library.Event;
import de.libutzki.archmodules.sample.library.EventListener;

public class EventDescriptor implements BuildingBlockDescriptor {

	public static final BuildingBlockDescriptor INSTANCE = new EventDescriptor();

	private EventDescriptor() {
	}

	@Override
	public String name() {
		return "Event";
	}

	@Override
	public Stream<JavaClass> filter(final Stream<JavaClass> javaClasses) {
		return javaClasses
				.filter(javaClass -> javaClass.isAnnotatedWith(Event.class));
	}

	@Override
	public Stream<RelationshipDescriptor> relationshipDescriptorsFor(final JavaClass javaClasses) {
		return Stream.of(new EventEmitterDescriptor(), new EventHandlerDescriptor());
	}

	public static class EventEmitterDescriptor implements RelationshipDescriptor {

		@Override
		public String name() {
			return "Event Emitter";
		}

		@Override
		public Stream<JavaClass> relationshipClassesOf(final JavaClass javaClass) {
			return javaClass.getConstructorCallsToSelf()
					.stream()
					.map(JavaConstructorCall::getOriginOwner);
		}

	}

	public static class EventHandlerDescriptor implements RelationshipDescriptor {

		@Override
		public String name() {
			return "Event Handler";
		}

		@Override
		public Stream<JavaClass> relationshipClassesOf(final JavaClass javaClass) {
			return javaClass.getMethodsWithParameterTypeOfSelf()
					.stream()
					.filter(method -> method.isAnnotatedWith(EventListener.class))
					.filter(method -> method.getRawParameterTypes().get(0).equals(javaClass))
					.map(JavaMethod::getOwner);
		}

	}

}
