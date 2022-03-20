package src.main.java;

public class FamilyTreeTest {

    public static void main(String[] args) {
        String name = Input.getString("Enter the ancestor's name: ");
        FamilyTree familyTree = new FamilyTree(name);
        Integer option;
        do {
            option = getInteger("Enter option: ", "\nError: Option must be a whole number\n");
            try {
                switch (option) {
                    case 0 -> System.out.println("Closing application...");
                    case 1 -> addPartner(familyTree);
                    case 2 -> addChild(familyTree);
                    case 3 -> System.out.println(familyTree);
                    case 4 -> displaySpecificFamilyMember(familyTree);
                    default -> System.out.println("\nError: Invalid Option (0-4)\n");
                }
            } catch (NullPointerException ignored) {
            }
        } while (option != 0);
    }

    private static void addPartner(FamilyTree familyTree) {
        System.out.println(familyTree);
        Integer id = getInteger("Enter ID of the family member you wish to assign a partner: ", "\nError: ID must be a whole number\n");
        try {
            String returnedName = familyTree.setCurrentToPartner(id);
            String partnerName = Input.getString("Enter " + returnedName + "'s partner's name: ");
            familyTree.addPartner(partnerName);
        } catch (FamilyTreeADT.AlreadyHasPartnerException e) {
            System.out.println("\nError: Already has partner\n");
        } catch (FamilyTreeADT.FamilyMemberNotFoundException e) {
            System.out.println("\nError: Family member with ID " + id + " not found\n");
        }
    }

    private static void addChild(FamilyTree familyTree) {
        System.out.println(familyTree);
        Integer id = getInteger("Enter ID of the parent you wish to assign a child: ", "\nError: ID must be a whole number\n");
        try {
            familyTree.setCurrentToParent(id);
            String childName = Input.getString("Enter the child's name: ");
            familyTree.addChild(childName);
        } catch (FamilyTreeADT.NotUniqueSiblingException e) {
            System.out.println("\nError: Child not unique!\n");
        } catch (FamilyTreeADT.NoPartnerException e) {
            System.out.println("\nError: Family member has no partner\n");
        } catch (FamilyTreeADT.FamilyMemberNotFoundException e) {
            System.out.println("\nError: Family member with ID " + id + " not found\n");
        } catch (FamilyTreeADT.MaxDepthExceededException e) {
            System.out.println("\nError: Maximum amount of branches reached\n");
        } catch (FamilyTreeADT.MaxWidthExceededException e) {
            System.out.println("\nError: Maximum amount of siblings reached\n");
        }
    }

    private static void displaySpecificFamilyMember(FamilyTree familyTree) {
        System.out.println(familyTree);
        Integer id = getInteger("Enter ID of the chosen family member: ", "\nError: ID must be a whole number\n");
        try {
            System.out.println(familyTree.getFamilyMember(id));
        } catch (Exception e) {
            System.out.println("\nError: Not valid ID\n");
        }
    }

    private static Integer getInteger(String msg, String errorMsg) {
        Integer num;
        if (msg.contains("option"))
            System.out.println("1: Add Partner\n2: Add Child\n3: Display Family Tree\n4: Display Family Member\n0: Quit\n");
        try {
            num = Input.getInteger(msg);
        } catch (NumberFormatException e) {
            System.out.println(errorMsg);
            num = getInteger(msg, errorMsg);
        }
        return num;
    }
}