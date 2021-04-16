package de.libutzki.archmodules;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor(staticName = "of")
@Value
public class ModuleIdentifier {

	public static final ModuleIdentifier NONE = new ModuleIdentifier();

	private final String moduleName;

	private ModuleIdentifier() {
		this.moduleName = null;
	}

}
