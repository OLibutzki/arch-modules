package de.libutzki.archmodules;

import java.util.function.Predicate;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class BuildingBlockDescriptor {

	public final String name;
	public final Predicate<? super JavaClass> selector;

	static class BuildingBlockDescriptorWithName {

		final String name;

		BuildingBlockDescriptorWithName(final String name) {
			this.name = name;
		}

		public BuildingBlockDescriptor definedBy(final Predicate<? super JavaClass> selector) {
			return new BuildingBlockDescriptor(name, selector);
		}
	}

}
