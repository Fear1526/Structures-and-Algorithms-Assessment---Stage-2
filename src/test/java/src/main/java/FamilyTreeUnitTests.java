package src.main.java;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.Test;

class FamilyTreeUnitTests {

    private FamilyTree underTest;


    @Test
    void shouldAddAncestor(){
        // given
        underTest = new FamilyTree("James");

        // when
        boolean expected = underTest.contains("James");

        // then
        assertTrue(expected);
    }


    @Test
    void shouldAddPartnerToAncestor(){
        // given
        underTest = new FamilyTree("James");
        underTest.setCurrentToPartner(1);
        underTest.addPartner("Mary");

        // when
        boolean expected = underTest.contains("Mary");

        // then
        assertTrue(expected);
    }

    @Test
    void shouldAddChildToAncestorAndPartner(){
        // given
        underTest = new FamilyTree("James");
        underTest.setCurrentToPartner(1);
        underTest.addPartner("Mary");
        underTest.addChild("John");
        underTest.setCurrentToParent(2);
        underTest.addChild("Amy");

        // when
        boolean expected1 = underTest.contains("John");
        boolean expected2 = underTest.contains("Amy");

        // then
        assertTrue(expected1);
        assertTrue(expected2);
    }

    @Test
    void shouldAddChildToChildAndPartnerOfAncestor(){
        // given
        underTest = new FamilyTree("James");
        underTest.setCurrentToPartner(1);
        underTest.addPartner("Mary");
        underTest.addChild("John");
        underTest.setCurrentToPartner(3);
        underTest.addPartner("Amy");
        underTest.setCurrentToParent(3);
        underTest.addChild("Kelly");
        underTest.setCurrentToParent(4);
        underTest.addChild("Paul");

        // when
        boolean expected1 = underTest.contains("Kelly");
        boolean expected2 = underTest.contains("Paul");

        // then
        assertTrue(expected1);
        assertTrue(expected2);
    }

    @Test
    void shouldThrowNotUniqueSiblingException(){
        // given
        underTest = new FamilyTree("James");
        underTest.setCurrentToPartner(1);
        underTest.addPartner("Mary");
        underTest.addChild("John");

        // then
        assertThrows(FamilyTreeExceptions.NotUniqueSiblingException.class, () -> {
            underTest.addChild("john");
        });
    }

    @Test
    void shouldThrowAlreadyHasPartnerException(){
        // given
        underTest = new FamilyTree("James");
        underTest.setCurrentToPartner(1);
        underTest.addPartner("Mary");
        // then
        assertThrows(FamilyTreeExceptions.AlreadyHasPartnerException.class, () -> {
            underTest.setCurrentToPartner(1);;
        });
        assertThrows(FamilyTreeExceptions.AlreadyHasPartnerException.class, () -> {
            underTest.setCurrentToPartner(2);;
        });
    }

    @Test
    void shouldThrowFamilyMemberNotFoundException(){
        // given
        underTest = new FamilyTree("James");

        // then
        assertThrows(FamilyTreeExceptions.FamilyMemberNotFoundException.class, () -> {
            underTest.setCurrentToParent(2);
        });
    }

    @Test
    void shouldThrowNoPartnerException(){
        // given
        underTest = new FamilyTree("James");
        underTest.setCurrentToPartner(1);
        underTest.addPartner("Mary");
        underTest.setCurrentToParent(2);
        underTest.addChild("John");

        // then
        assertThrows(FamilyTreeExceptions.NoPartnerException.class, () -> {
            underTest.setCurrentToParent(3);
        });
    }
}