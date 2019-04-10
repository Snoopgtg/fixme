package MessageBody;

public class CheckSum extends MessageParent{

    public CheckSum() {

        super(0, 10);
    }

    public void calculateAndSetCheckSumInMessage(String message) {

//      SOH have been replaced by'|'
        String replaceString = message.replace('␁', '|');

//      count starting at tag 35 (included)
        replaceString = replaceString.substring(replaceString.indexOf("35"));

//      tag 10 (excluded)
        replaceString = replaceString.substring(0, replaceString.lastIndexOf("10"));
        byte[] bytes = replaceString.getBytes();
        long sumBytes = 0;
        for (byte b : bytes) {
            sumBytes += b;
        }
        sumBytes = sumBytes % 256;
        super.setValue(String.format("%03d", sumBytes));

    }
}

