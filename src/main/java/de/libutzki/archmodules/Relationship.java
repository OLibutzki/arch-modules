package de.libutzki.archmodules;

import javax.annotation.Nonnull;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class Relationship {

	@Nonnull
	public String name;

	@Nonnull
	public JavaClass source;

	@Nonnull
	public BuildingBlock targetBuildingBlock;

	@Override
	public String toString() {
		return source.getName() + " " + name + " " + targetBuildingBlock;
	}
}
