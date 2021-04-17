package de.libutzki.archmodules;

import java.util.Optional;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class BuildingBlock {

	@NonNull
	private final String name;

	private final String module;

	@NonNull
	private final JavaClass javaClass;

	public Optional<String> getModule() {
		return Optional.ofNullable(module);
	}

	@Override
	public String toString() {
		return "<" + name + "> " + javaClass.getName() + " (" + getModule().map(moduleName -> "module: " + moduleName).orElse("no module") + ")";
	}

}
