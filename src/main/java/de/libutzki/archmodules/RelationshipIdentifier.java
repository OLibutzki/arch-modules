package de.libutzki.archmodules;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor(staticName = "of")
@Value
public class RelationshipIdentifier {

	private final String role;
	private final BuildingBlockType buildingBlockType;
}
