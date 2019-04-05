package MessageBody.EnumMessageBody;

import java.util.Random;

public enum ListOfSymbol {
    MSFT,
    APPL,
    SAMS,
    XIAM,
    HYUW;

    public static ListOfSymbol getRandomSymbol() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
