package MessageBody;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendingTime extends MessageParent{

    public SendingTime() {

        super(52);
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS").format(new Date());
        super.setValue(timeStamp);
    }
}

