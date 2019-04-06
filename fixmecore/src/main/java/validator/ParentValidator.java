package validator;

public abstract class ParentValidator {

    private ParentValidator next;

    public ParentValidator linkWith(ParentValidator next) {

        this.next = next;
        return next;
    }

    public abstract boolean check(String recievdMessage);

    protected boolean checkNext(String recievdMessage) {
        if (next == null) {
            return true;
        }
        return next.check(recievdMessage);
    }
}
