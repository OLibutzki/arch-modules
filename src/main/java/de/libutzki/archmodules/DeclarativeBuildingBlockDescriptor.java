package de.libutzki.archmodules;

import java.util.function.Predicate;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeclarativeBuildingBlockDescriptor implements BuildingBlockDescriptor {
	private final BuildingBlockType type;
	private final Predicate<? super JavaClass> selector;

	static class DeclarativeBuildingBlockDescriptorWithType {

		final BuildingBlockType type;

		DeclarativeBuildingBlockDescriptorWithType(final BuildingBlockType type) {
			this.type = type;
		}

		public DeclarativeBuildingBlockDescriptor definedBy(final Predicate<? super JavaClass> selector) {
			return new DeclarativeBuildingBlockDescriptor(type, selector);
		}
	}

	@Override
	public String toString() {
		return type.toString();
	}

	@Override
	public BuildingBlockType type() {
		return type;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return selector.test(javaClass);
	}
}
