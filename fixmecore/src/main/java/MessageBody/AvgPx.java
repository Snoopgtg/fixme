package MessageBody;

public class AvgPx extends MessageParent{

    public AvgPx(Integer value) {

        super(value, 6);

    }
    public AvgPx() {
        super(6);
    }

    public void setAvgPx(Integer avgPx) {
        super.setValue(avgPx);
    }

    @Override
    public void getAndSetValueFromString(String recievedMessage) {
        String tag = String.valueOf("44");
        String replaceSOH = recievedMessage.replace('␁', '|');
        replaceSOH = replaceSOH.substring(replaceSOH.indexOf(tag));
        replaceSOH = replaceSOH.substring(0, replaceSOH.indexOf('|'));
        super.setValue(replaceSOH.substring(replaceSOH.indexOf('=') + 1));;
    }
}

// якщо були закази до цього з такими акціями то тут відображається сума
// ціни попередніх Price і теперішньої
