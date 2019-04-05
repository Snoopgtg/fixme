package MessageBody;

import MessageBody.EnumMessageBody.ListOfSymbol;

public class Symbol extends MessageParent{

    public Symbol() {

        super(55);
        super.setValue(setSybol());
    }

    private String setSybol() {
        return ListOfSymbol.getRandomSymbol().toString();
    }
}

