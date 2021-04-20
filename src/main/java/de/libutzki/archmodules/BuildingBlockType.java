package de.libutzki.archmodules;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor(staticName = "of")
@Value
public final class BuildingBlockType {
	private final String name;

}
