package de.libutzki.archmodules.sample.module1;

import de.libutzki.archmodules.sample.library.Module;
import de.libutzki.archmodules.sample.shared.Event1;

@Module("Module 1")
public class Module1Configuration {

	public void someMethod() {
		new Event1();
	}
}
