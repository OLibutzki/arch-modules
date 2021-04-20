package de.libutzki.archmodules;

import java.util.function.Predicate;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class BuildingBlockDescriptor {

	public final BuildingBlockType type;
	public final Predicate<? super JavaClass> selector;

	static class BuildingBlockDescriptorWithType {

		final BuildingBlockType type;

		BuildingBlockDescriptorWithType(final BuildingBlockType type) {
			this.type = type;
		}

		public BuildingBlockDescriptor definedBy(final Predicate<? super JavaClass> selector) {
			return new BuildingBlockDescriptor(type, selector);
		}
	}

}
