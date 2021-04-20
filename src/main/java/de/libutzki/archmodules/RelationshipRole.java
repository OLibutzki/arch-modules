package de.libutzki.archmodules;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor(staticName = "of")
@Value
public class RelationshipRole {
	private final String name;
}
