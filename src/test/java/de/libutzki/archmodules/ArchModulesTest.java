package de.libutzki.archmodules;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public class ArchModulesTest {

	@Test
	void test() {

		final JavaClasses javaClasses = new ClassFileImporter().importPackages("de.libutzki.archmodules.sample");

		final MyModuleAssignment moduleAssignment = new MyModuleAssignment(javaClasses);
		final Application application = Application.builder()
				.javaClasses(javaClasses)
				.buildingBlockDescriptor(EventDescriptor.INSTANCE)
				.moduleAssignment(moduleAssignment)
				.build();

		final Set<BuildingBlock> buildingBlocks = application.getBuildingBlocks();

		buildingBlocks.forEach(buildingBlock -> {
			System.out.println("<" + buildingBlock.getName() + "> " + buildingBlock.getJavaClass().getName());
			buildingBlock.getRelationships().forEach(relationship -> {
				System.out.println(relationship.getName() + ":");
				relationship.getJavaClasses().forEach(javaClass -> {
					System.out.println("  " + javaClass.getName() + "(Module: " + moduleAssignment.getModuleFor(javaClass).getModuleName() + ")");
				});
			});
		});
	}
}
