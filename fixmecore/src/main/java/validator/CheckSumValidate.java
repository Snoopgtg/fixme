package validator;

import MessageBody.CheckSum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class CheckSumValidate extends ParentValidator {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private CheckSum checkSum;

    public CheckSumValidate() {
        this.checkSum = new CheckSum();
    }

    @Override
    public boolean check(String receivedMessage) {

        this.checkSum.calculateAndSetCheckSumInMessage(receivedMessage);
        String res = this.checkSum.getValue().toString();
        this.checkSum.getAndSetValueFromString(receivedMessage);
        if(!res.equals(this.checkSum.getValue())) {
            logger.error("Doesn't validate {} with {}", res, this.checkSum.getValue());
            return false;
        }
        logger.info("{} - check sum is accepted", res);
        return checkNext(receivedMessage);
    }
}
