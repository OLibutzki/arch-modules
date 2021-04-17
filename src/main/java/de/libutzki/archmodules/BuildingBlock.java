package de.libutzki.archmodules;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Builder
@EqualsAndHashCode
public class BuildingBlock {

	@NonNull
	public String name;

	@NonNull
	public JavaClass javaClass;

	@Override
	public String toString() {
		return "<" + name + "> " + javaClass.getName();
	}

}
