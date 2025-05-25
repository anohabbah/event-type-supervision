package dev.abbah.supervision.eventtype.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class HexagonalArchitectureTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("dev.abbah.supervision.eventtype");
    }

    @Test
    void domainShouldNotDependOnOtherPackages() {
        noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("..application..", "..adapter..")
                .check(importedClasses);
    }

    @Test
    void applicationShouldNotDependOnAdapters() {
        noClasses()
                .that()
                .resideInAPackage("..application..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("..adapter..")
                .check(importedClasses);
    }

    @Test
    void adaptersShouldNotDependOnEachOther() {
        noClasses()
                .that()
                .resideInAPackage("..adapter.in..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..adapter.out..")
                .check(importedClasses);

        noClasses()
                .that()
                .resideInAPackage("..adapter.out..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..adapter.in..")
                .check(importedClasses);
    }

    @Test
    void servicesShouldOnlyBeAccessedViaTheirInterfaces() {
        classes()
                .that()
                .resideInAPackage("..application.service..")
                .should()
                .onlyBeAccessed()
                .byClassesThat()
                .resideInAPackage("..application.service..")
                .orShould()
                .onlyBeAccessed()
                .byClassesThat()
                .resideInAPackage("..config..")
                .check(importedClasses);
    }

    @Test
    void enforceHexagonalArchitecture() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Domain").definedBy("..domain..")
                .layer("ApplicationPorts").definedBy("..application.port..")
                .layer("ApplicationServices").definedBy("..application.service..")
                .layer("AdaptersIn").definedBy("..adapter.in..")
                .layer("AdaptersOut").definedBy("..adapter.out..")
                .layer("Config").definedBy("..config..")
                .whereLayer("Domain").mayOnlyBeAccessedByLayers(
                        "ApplicationPorts", "ApplicationServices", "AdaptersIn", "AdaptersOut")
                .whereLayer("ApplicationPorts").mayOnlyBeAccessedByLayers(
                        "ApplicationServices", "AdaptersIn", "AdaptersOut")
                .whereLayer("ApplicationServices").mayOnlyBeAccessedByLayers("Config")
                .whereLayer("AdaptersIn").mayNotBeAccessedByAnyLayer()
                .whereLayer("AdaptersOut").mayNotBeAccessedByAnyLayer()
                .check(importedClasses);
    }
}
