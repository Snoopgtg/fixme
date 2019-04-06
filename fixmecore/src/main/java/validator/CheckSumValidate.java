package validator;

import MessageBody.CheckSum;

public class CheckSumValidate extends ParentValidator {

    private String recievdMessage;
    private CheckSum checkSum;

    public CheckSumValidate(String recievdMessage) {
        this.recievdMessage = recievdMessage;
    }

    @Override
    public boolean check(String recievdMessage) {

        checkSum.getAndSetValueFromString(recievdMessage);
        if

        return checkNext(recievdMessage);
    }
}
