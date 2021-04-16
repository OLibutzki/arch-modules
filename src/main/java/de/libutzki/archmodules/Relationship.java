package de.libutzki.archmodules;

import java.util.Set;

import javax.annotation.Nonnull;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class Relationship {

	@Nonnull
	String name;

	@Singular
	Set<JavaClass> javaClasses;
}
