package platform.dto;

/**
 * Class of data transfer object for rule
 */
public class RuleDTO extends BaseDTO {
    private final boolean myPublic;
    private final boolean myPrivate;
    private final boolean myGlobal;

    private final boolean otherPublic;
    private final boolean otherPrivate;
    private final boolean otherGlobal;

    public RuleDTO(String rule) {
        Character ALLOWED = '1';

        Character own_Public = rule.charAt(2);
        Character own_Private = rule.charAt(1);
        Character own_Global = rule.charAt(0);

        Character notOwn_Public = rule.charAt(5);
        Character notOwn_Private = rule.charAt(4);
        Character notOwn_Global = rule.charAt(3);

        this.myPublic = own_Public.equals(ALLOWED);
        this.myPrivate = own_Private.equals(ALLOWED);
        this.myGlobal = own_Global.equals(ALLOWED);

        this.otherPublic = notOwn_Public.equals(ALLOWED);
        this.otherPrivate = notOwn_Private.equals(ALLOWED);
        this.otherGlobal = notOwn_Global.equals(ALLOWED);
    }

    public boolean isMyPublic() {
        return myPublic;
    }

    public boolean isMyPrivate() {
        return myPrivate;
    }

    public boolean isMyGlobal() {
        return myGlobal;
    }

    public boolean isOtherPublic() {
        return otherPublic;
    }

    public boolean isOtherPrivate() {
        return otherPrivate;
    }

    public boolean isOtherGlobal() {
        return otherGlobal;
    }

    /**
     * Method for get common state access for user not own documents
     * @return name of state
     */
    public String getOtherAccess() {
        if(this.isOtherPublic() && isOtherPrivate() && isOtherGlobal()) {
            return "Full";
        } else if (this.isOtherPublic() && isOtherPrivate() && !isOtherGlobal()) {
            return "PublicAndPrivate";
        } else if (this.isOtherPublic() && !isOtherPrivate() && !isOtherGlobal()) {
            return "OnlyPublic";
        } else if (!this.isOtherPublic() && isOtherPrivate() && !isOtherGlobal()) {
            return "PublicAndGlobal";
        } else {
            return "NotAccess";
        }
    }

    /**
     * Method for get common state access for user own documents
     * @return name of state
     */
    public String getMyAccess() {
        if(this.isMyPublic() && isMyPrivate() && isMyGlobal()) {
            return "Full";
        } else if (this.isMyPublic() && isMyPrivate() && !isMyGlobal()) {
            return "PublicAndPrivate";
        } else if (this.isMyPublic() && !isMyPrivate() && !isMyGlobal()) {
            return "OnlyPublic";
        } else if (!this.isMyPublic() && isMyPrivate() && !isMyGlobal()) {
            return "PublicAndGlobal";
        } else {
            return "NotAccess";
        }
    }
}
