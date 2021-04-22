package de.libutzki.archmodules;

import java.util.function.Function;
import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeclarativeRelationshipDescriptor implements RelationshipDescriptor {
	private final RelationshipIdentifier identifier;
	private final Function<? super JavaClass, Stream<JavaClass>> sourceSelector;

	@RequiredArgsConstructor
	static class DeclarativeRelationshipDescriptorWithIdentifier {

		private final RelationshipIdentifier identifier;

		public DeclarativeRelationshipDescriptor from(final Function<? super JavaClass, Stream<JavaClass>> sourceSelector) {
			return new DeclarativeRelationshipDescriptor(identifier, sourceSelector);
		}
	}

	@Override
	public RelationshipIdentifier identifier() {
		return identifier;
	}

	@Override
	public String role() {
		return identifier.getRole();
	}

	@Override
	public BuildingBlockType targetBuildingBlockType() {
		return identifier.getBuildingBlockType();
	}

	@Override
	public Stream<JavaClass> sources(final JavaClass targetClass) {
		return sourceSelector.apply(targetClass);
	}

}
