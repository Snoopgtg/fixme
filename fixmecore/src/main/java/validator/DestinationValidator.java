package validator;

import MessageBody.SenderCompID;
import MessageBody.TargetCompID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class DestinationValidator extends ParentValidator {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ArrayList<Integer> listOfDestination;
    private ArrayList<Integer> listOfSenders;
    private SenderCompID senderCompID;
    private TargetCompID targetCompID;

    public DestinationValidator(ArrayList<Integer> listOfDestination) {

        this.listOfDestination = listOfDestination;
        this.senderCompID = new SenderCompID();
        this.targetCompID = new TargetCompID();
    }

    @Override
    public boolean check(String receivedMessage) {

        //буде проблема з айді, мабуть там буду ще літери
        senderCompID.getAndSetValueFromString(receivedMessage);
        Integer des = (Integer) senderCompID.getValue();
        for (Integer elem: listOfDestination){
            if (!des.equals(elem)) {
                logger.error("{} - this is destination doesn't exist\n", des);
                return false;
            }
        }

        targetCompID.getAndSetValueFromString(receivedMessage);
        Integer sender = (Integer) targetCompID.getValue();
        for (Integer elem: listOfSenders){
            if (!sender.equals(elem)) {
                logger.error("{} - this is sender doesn't exist\n", sender);
                return false;
            }
        }
        logger.info("senderCompID - {} and targetCompID - {} are accepted\n", sender, des);
        return true;
    }
}
