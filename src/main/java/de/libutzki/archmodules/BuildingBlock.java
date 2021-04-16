package de.libutzki.archmodules;

import java.util.Set;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class BuildingBlock {

	@NonNull
	String name;

	@NonNull
	JavaClass javaClass;

	@Singular
	Set<Relationship> relationships;

}
