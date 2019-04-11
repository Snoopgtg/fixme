package validator;

import MessageBody.SenderCompID;
import MessageBody.TargetCompID;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class  DestinationValidator extends ParentValidator {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Map<Integer, Channel> mapOfTargets;
    private Map<Integer, Channel> mapOfSenders;

    private SenderCompID senderCompID;
    private TargetCompID targetCompID;

    public DestinationValidator(Map<Integer, Channel> mapOfSenders, Map<Integer, Channel> mapOfTargets) {

        this.mapOfTargets = mapOfTargets;
        this.mapOfSenders = mapOfSenders;
        this.senderCompID = new SenderCompID();
        this.targetCompID = new TargetCompID();
    }

    @Override
    public boolean check(String receivedMessage) {

        senderCompID.getAndSetValueFromString(receivedMessage);
        Integer sender = Integer.parseInt(senderCompID.getValue().toString());
        if (!mapOfSenders.containsKey(sender)) {
            logger.error("{} - this is sender doesn't exist", String.format("%06d", sender));
            return false;
        }
        targetCompID.getAndSetValueFromString(receivedMessage);
        Integer target = Integer.parseInt(targetCompID.getValue().toString());
        if (!mapOfTargets.containsKey(target)) {
            logger.error("{} - this is destination doesn't exist", String.format("%06d", target));
            return false;
        }
        logger.info("senderCompID - {} and targetCompID - {} are accepted", String.format("%06d", sender),
                String.format("%06d", target));
        return true;
    }
}
