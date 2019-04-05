package MessageBody;

public class BodyLength extends MessageParent{

    public BodyLength() {

        super(0, 9);
    }

    /*
    Body length is the character count starting at tag 35 (included) all the way to tag 10 (excluded). SOH delimiters do count in body length.

    For Example: (SOH have been replaced by'|')

            8=FIX.4.2|9=65|35=A|49=SERVER|56=CLIENT|34=177|52=20090107-18:15:16|98=0|108=30|10=062|
            0 + 0 + 5 + 10 + 10 + 7 + 21 + 5 + 7 + 0 = 65

    Has a Body length of 65.
    The SOH delimiter at the end of a Tag=Value belongs to the Tag.
    */


    public int calculateLengthOfBytesInMessage(String message) {

//      SOH have been replaced by'|'
        message = message.replace('␁', '|');

//      count starting at tag 35 (included)
        message = message.substring(message.indexOf("35"));

//      tag 10 (excluded)
        message = message.substring(0, message.lastIndexOf("10"));
        return message.getBytes().length;
    }
}

