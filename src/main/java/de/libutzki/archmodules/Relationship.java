package de.libutzki.archmodules;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Relationship {

	@Nonnull
	private String name;

	@Nonnull
	private JavaClass source;

	private String sourceModule;

	@Nonnull
	private BuildingBlock targetBuildingBlock;

	public Optional<String> getSourceModule() {
		return Optional.ofNullable(sourceModule);
	}

	@Override
	public String toString() {
		return source.getName() + " (" + getSourceModule().map(moduleName -> "module: " + moduleName).orElse("no module") + ") " + name + " " + targetBuildingBlock;
	}
}
