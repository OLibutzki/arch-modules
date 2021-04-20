package de.libutzki.archmodules;

import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.EqualsAndHashCode;
import lombok.ToString;

public abstract class ArchDocClass {
	@ToString.Include
	private final JavaClass javaClass;
	@ToString.Include
	private Optional<Module> module = Optional.empty();

	@EqualsAndHashCode.Exclude
	private final Set<Relationship> relationshipsAsSource = new HashSet<>();

	ArchDocClass(final JavaClass javaClass) {
		this.javaClass = javaClass;
	}

	void assignModule(final Module module) {
		setModule(module);
		module.addClass(this);
	}

	void setModule(final Module module) {
		if (this.module.isPresent()) {
			throw new IllegalStateException();
		}
		this.module = Optional.of(module);
	}

	void addRelationshipAsSource(final Relationship relationship) {
		relationshipsAsSource.add(relationship);
	}

	public JavaClass getJavaClass() {
		return javaClass;
	}

	public Optional<Module> getModule() {
		return module;
	}

	public Set<Relationship> getRelationshipsAsSource() {
		return unmodifiableSet(relationshipsAsSource);
	}
}
