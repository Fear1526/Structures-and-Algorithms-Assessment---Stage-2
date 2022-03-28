package src.main.java;

public class FamilyTree implements FamilyTreeExceptions {

    static class FamilyTreeNode {
        private String name;
        private Integer identifier;
        private FamilyTreeNode partner;
        private FamilyTreeNode sibling;
        private FamilyTreeNode firstChild;
        private int depth;
        private int width;
    }

    private final FamilyTreeNode ancestor;
    private FamilyTreeNode current;
    private Integer familyMemberCount;
    private static final int MAX_DEPTH = 3;                 // Max number of branches
    private static final int MAX_SIBLINGS = 3;              // Max number of siblings

    public FamilyTree(String ancestor) {
        this.ancestor = new FamilyTreeNode();               // sets up ancestor node
        this.ancestor.name = ancestor;
        this.ancestor.identifier = 1;
        this.familyMemberCount = 1;
        this.current = this.ancestor;
        this.ancestor.depth = 0;
    }

    private FamilyTreeNode furthestSibling(String name, FamilyTreeNode currentNode) throws NotUniqueSiblingException {
        if (name.equalsIgnoreCase(currentNode.name))                // Catches when a sibling to be added isn't unique
            throw new NotUniqueSiblingException();
        if (currentNode.sibling == null)                            // returns currentNode when end of linked list is reached
            return currentNode;
        return furthestSibling(name, currentNode.sibling);          // method calls self if end of linked list is not reached
    }


    public void addChild(String name) throws MaxDepthExceededException, MaxWidthExceededException {
        FamilyTreeNode newChild = new FamilyTreeNode();                 // Sets up new child node
        newChild.name = name;
        newChild.identifier = this.familyMemberCount + 1;
        if (this.current.firstChild == null) {                          // if couple don't already have a child, assign child as firstChild
            if (this.current.depth == MAX_DEPTH)                        // when maximum amount of branches reached throw exception
                throw new MaxDepthExceededException();
            this.current.firstChild = newChild;
            this.current.partner.firstChild = newChild;
            newChild.depth = this.current.depth + 1;
            newChild.width = 0;
        } else {
            FamilyTreeNode furthest = furthestSibling(name, current.firstChild);       // finds furthest sibling in linked list, assign child at end of linked list
            if (furthest.width == MAX_SIBLINGS)                                        // when maximum amount of siblings reached throw exception
                throw new MaxWidthExceededException();
            furthest.sibling = newChild;
            newChild.depth = current.depth + 1;
            newChild.width = furthest.width + 1;
        }
        this.familyMemberCount += 1;                        // increments family count
    }

    public void setCurrentToParent(Integer id) throws NoPartnerException{
        findFamilyMember(id);                       // searches if ID exists and sets that node to current
        if (this.current.partner == null)           // if current doesn't have a partner throw exception
            throw new NoPartnerException();
    }

    private FamilyTreeNode findFamilyMember(Integer id) throws FamilyMemberNotFoundException {
        FamilyTreeNode familyMember;
        if (id.equals(this.ancestor.identifier)) {      // if IDs are the same target ID is ancestor ID then target ID found
            familyMember = this.ancestor;
        } else if (this.ancestor.partner == null) {     // If ancestor does not have a partner throw exception (end of family tree)
            throw new FamilyMemberNotFoundException();
        } else if (id.equals(this.ancestor.partner.identifier)) {   // if ancestors partners ID is target ID then target ID found
            familyMember = this.ancestor.partner;
        } else {
            if (this.ancestor.firstChild == null)               // If ancestor and partner has no children throw exception (end of family tree)
                throw new FamilyMemberNotFoundException();
            familyMember = findFamilyMember(id, this.ancestor.firstChild);      // Call recursive method to find family member ID
            if (familyMember == null)                                           // if returned value from recursive method is null ID not found
                throw new FamilyMemberNotFoundException();
        }

        this.current = familyMember;                                        // Sets up current node
        return familyMember;
    }

    private FamilyTreeNode findFamilyMember(Integer id, FamilyTreeNode currentNode) {       // Recursive method
        FamilyTreeNode familyMember = null;
        if (id.equals(currentNode.identifier))                                                  // ID found when ID matches current nodes ID
            familyMember = currentNode;
        else if (currentNode.partner != null && id.equals(currentNode.partner.identifier))      // ID found when ID matches current nodes partners ID
            familyMember = currentNode.partner;
        else if (currentNode.sibling != null)                                        // if current node has a sibling recall method with the next sibling
            familyMember = findFamilyMember(id, currentNode.sibling);
        if (currentNode.firstChild != null && familyMember == null)                  // if current node has children and family member not found
            familyMember = findFamilyMember(id, currentNode.firstChild);             // recall function with the first child

        return familyMember;            // returns found family member or returns null if not found
    }


    public void addPartner(String name){
        FamilyTreeNode partner = new FamilyTreeNode();                  // Sets up partner node
        partner.name = name;
        partner.partner = this.current;                                 // sets newNodes partner to point at current node
        partner.identifier = this.familyMemberCount + 1;
        partner.firstChild = this.current.firstChild;
        partner.depth = this.current.depth;
        this.current.partner = partner;                                 // sets current nodes partner to point at the new partner node
        this.familyMemberCount += 1;
    }

    public String setCurrentToPartner(Integer id) throws AlreadyHasPartnerException{
        findFamilyMember(id);                                   // searches if ID exists and sets that node to current
        if (this.current.partner != null)                       // throws exception if family member already has a partner
            throw new AlreadyHasPartnerException();
        return this.current.name;
    }


    private String getFamilyDetails(Integer id, int amountOfTabs) {        // recursive method
        FamilyTreeNode familyMember = findFamilyMember(id);         // searches for the node that has this ID
        StringBuilder familyDetails = new StringBuilder();

        for (int tab = 0; tab < amountOfTabs; tab++)             // used to set the depth that the family member is positioned
            familyDetails.append("\t");
        familyDetails.append(familyMember.name + " (" + familyMember.identifier + ")");                 // appends target family member
        if (familyMember.partner != null) {                                                             // appends target family member partner if node not null
            familyDetails.append(" partner " + familyMember.partner.name + " (" + familyMember.partner.identifier + ")\n");
            if (familyMember.firstChild != null)                                                         // when family member has a child, recall self with identifier of first child, increment amountOfTabs
                familyDetails.append(getFamilyDetails(familyMember.firstChild.identifier, amountOfTabs + 1));
            else {
                for (int tab = 0; tab < amountOfTabs + 1; tab++)        // sets the depth "no children" is positioned
                    familyDetails.append("\t");
                familyDetails.append("no children\n");
            }
        } else
            familyDetails.append(" has no partner\n");                      // appends "no partner" if node has no partner

        if (familyMember.sibling != null && amountOfTabs > 0) {                                             // when the family member has a sibling and not the first method call
            familyDetails.append(getFamilyDetails(familyMember.sibling.identifier, amountOfTabs));          // recall self with identifier of next sibling
        }

        return familyDetails.toString();                                // returns collected family tree
    }


    public String getFamilyMember(Integer id) {
        return getFamilyDetails(id, 0);          // Calls method to get family tree starting at a specific ID
    }

    @Override
    public String toString() {
        return getFamilyDetails(1, 0);      // calls method to display entire family tree
    }


    // Code for junit tests
    public boolean contains(String name) {
        return contains(name, this.ancestor);                   // returns result of checking all of the family tree
    }

    private boolean contains(String name, FamilyTreeNode currentNode) {
        if (currentNode.name.equalsIgnoreCase(name))                                // if current node is the target return true
            return true;
        if (currentNode.partner != null && currentNode.partner.name.equalsIgnoreCase(name))        // if current nodes partner is the target return true
            return true;
        boolean result = false;
        if (currentNode.firstChild != null)                             // if current nodes first child is not null
            result = contains(name, currentNode.firstChild);            // method self calls with the first child
        if (!result && currentNode.sibling != null)                     // if current nodes siblings is not null
            result = contains(name, currentNode.sibling);               // method self calls with current nodes sibling

        return result;          // returns result
    }
}