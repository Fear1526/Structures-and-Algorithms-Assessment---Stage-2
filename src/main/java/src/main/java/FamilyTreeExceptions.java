package src.main.java;

public interface FamilyTreeExceptions {

    public class NotUniqueSiblingException extends RuntimeException {
    }

    public class AlreadyHasPartnerException extends RuntimeException {
    }

    public class FamilyMemberNotFoundException extends RuntimeException {
    }

    public class NoPartnerException extends RuntimeException {
    }
}
