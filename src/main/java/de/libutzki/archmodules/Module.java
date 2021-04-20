package de.libutzki.archmodules;

import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Module {

	private final String name;
	@EqualsAndHashCode.Exclude
	private final Set<ArchDocClass> classes = new HashSet<>();

	Module(final String name) {
		this.name = name;
	}

	void assignClass(final ArchDocClass archDocClass) {
		addClass(archDocClass);
		archDocClass.setModule(this);
	}

	void addClass(final ArchDocClass archDocClass) {
		classes.add(archDocClass);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
