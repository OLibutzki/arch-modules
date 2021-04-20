package de.libutzki.archmodules;

import com.tngtech.archunit.core.domain.JavaClass;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class ArbitraryClass extends ArchDocClass {

	ArbitraryClass(final JavaClass javaClass) {
		super(javaClass);
	}

	@Override
	public String toString() {
		return getJavaClass().getName() + " (" + getModule().map(moduleName -> "module: " + moduleName).orElse("no module") + ")";
	}
}
