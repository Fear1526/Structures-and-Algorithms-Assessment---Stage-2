package src.main.java;

public interface FamilyTreeADT {

    public class NotUniqueSiblingException extends RuntimeException {
    }

    public class AlreadyHasPartnerException extends RuntimeException {
    }

    public class FamilyMemberNotFoundException extends RuntimeException {
    }

    public class NoPartnerException extends RuntimeException {
    }

    public class MaxDepthExceededException extends RuntimeException {
    }

    public class MaxWidthExceededException extends RuntimeException {
    }

    public abstract void addChild(String name) throws NotUniqueSiblingException, MaxDepthExceededException;

    public abstract String getFamilyMember(Integer id);

    public abstract void addPartner(String name) throws AlreadyHasPartnerException, FamilyMemberNotFoundException;
}
