package src.main.java;

public class FamilyTree implements FamilyTreeADT {

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
    private static final int MAX_DEPTH = 3;
    private static final int MAX_SIBLINGS = 3;

    public FamilyTree(String ancestor) {
        this.ancestor = new FamilyTreeNode();
        this.ancestor.name = ancestor;
        this.ancestor.identifier = 1;
        this.familyMemberCount = 1;
        this.current = this.ancestor;
        this.ancestor.depth = 0;
    }

    private FamilyTreeNode furthestSibling(String name, FamilyTreeNode currentNode) throws NotUniqueSiblingException {
        if (name.equalsIgnoreCase(currentNode.name))
            throw new NotUniqueSiblingException();
        if (currentNode.sibling == null)
            return currentNode;
        return furthestSibling(name, currentNode.sibling);
    }

    @Override
    public void addChild(String name) throws NotUniqueSiblingException, MaxDepthExceededException, MaxWidthExceededException {
        if (this.current.partner == null)
            throw new NoPartnerException();

        FamilyTreeNode newChild = new FamilyTreeNode();
        newChild.name = name;
        if (this.current.firstChild == null) {
            if (this.current.depth == MAX_DEPTH)
                throw new MaxDepthExceededException();
            this.current.firstChild = newChild;
            this.current.partner.firstChild = newChild;
            newChild.depth = current.depth + 1;
            newChild.width = 0;
        } else {
            FamilyTreeNode furthest = furthestSibling(name, current.firstChild);
            if (furthest.width == MAX_SIBLINGS)
                throw new MaxWidthExceededException();
            furthest.sibling = newChild;
            newChild.depth = current.depth + 1;
            newChild.width = furthest.width + 1;
        }
        newChild.identifier = this.familyMemberCount + 1;
        this.familyMemberCount += 1;
    }

    public void setCurrentToParent(Integer id){
        FamilyTreeNode parent = findFamilyMember(id);
        if (parent.partner == null)
            throw new NoPartnerException();
    }

    private FamilyTreeNode findFamilyMember(Integer id) throws FamilyMemberNotFoundException {
        FamilyTreeNode familyMember;
        if (id.equals(this.ancestor.identifier)) {
            this.current = this.ancestor;
            return this.ancestor;
        } else if (this.ancestor.partner == null) {
            throw new FamilyMemberNotFoundException();
        } else if (id.equals(this.ancestor.partner.identifier)) {
            this.current = this.ancestor.partner;
            return this.ancestor.partner;
        }
        if (this.ancestor.firstChild == null)
            throw new FamilyMemberNotFoundException();
        familyMember = findFamilyMember(id, this.ancestor.firstChild);
        if (familyMember == null)
            throw new FamilyMemberNotFoundException();

        this.current = familyMember;
        return familyMember;
    }

    private FamilyTreeNode findFamilyMember(Integer id, FamilyTreeNode currentNode) {
        FamilyTreeNode familyMember = null;
        if (id.equals(currentNode.identifier))
            return currentNode;
        else if (currentNode.partner != null && id.equals(currentNode.partner.identifier))
            return currentNode.partner;
        if (currentNode.sibling != null)
            familyMember = findFamilyMember(id, currentNode.sibling);
        if (familyMember != null)
            return familyMember;
        if (currentNode.firstChild != null)
            return findFamilyMember(id, currentNode.firstChild);

        return null;
    }

    @Override
    public void addPartner(String name) throws AlreadyHasPartnerException, FamilyMemberNotFoundException {
        FamilyTreeNode partner = new FamilyTreeNode();
        partner.name = name;
        partner.partner = this.current;
        partner.identifier = this.familyMemberCount + 1;
        partner.firstChild = this.current.firstChild;
        partner.depth = this.current.depth;
        this.current.partner = partner;
        this.familyMemberCount += 1;
    }

    public String setCurrentToPartner(Integer id){
        findFamilyMember(id);
        if (this.current.partner != null)
            throw new AlreadyHasPartnerException();
        return this.current.name;
    }


    private String getFamilyDetails(Integer id, int depth) {
        FamilyTreeNode familyMember = findFamilyMember(id);
        StringBuilder familyDetails = new StringBuilder();

        for (int i = 0; i < depth; i++)
            familyDetails.append("\t");
        familyDetails.append(familyMember.name + " (" + familyMember.identifier + ")");
        if (familyMember.partner != null) {
            familyDetails.append(" partner " + familyMember.partner.name + " (" + familyMember.partner.identifier + ")\n");
        } else
            familyDetails.append(" has no partner\n");

        if (familyMember.firstChild != null) {
            familyDetails.append(getFamilyDetails(familyMember.firstChild.identifier, depth + 1));
        } else if (familyMember.partner != null) {
            for (int i = 0; i < depth + 1; i++)
                familyDetails.append("\t");
            familyDetails.append("no children\n");
        }


        if (familyMember.sibling != null && depth > 0) {
            familyDetails.append(getFamilyDetails(familyMember.sibling.identifier, depth));
        }

        return familyDetails.toString();
    }

    @Override
    public String getFamilyMember(Integer id) {
        return getFamilyDetails(id, 0);
    }

    @Override
    public String toString() {
        return getFamilyDetails(1, 0);
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