package de.libutzki.archmodules;

import java.util.function.Predicate;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(value = AccessLevel.PACKAGE)
public final class BuildingBlockDescriptor {
	private final BuildingBlockType type;
	private final Predicate<? super JavaClass> selector;

	static class BuildingBlockDescriptorWithType {

		final BuildingBlockType type;

		BuildingBlockDescriptorWithType(final BuildingBlockType type) {
			this.type = type;
		}

		public BuildingBlockDescriptor definedBy(final Predicate<? super JavaClass> selector) {
			return new BuildingBlockDescriptor(type, selector);
		}
	}

	@Override
	public String toString() {
		return type.toString();
	}
}
