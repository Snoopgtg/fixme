package MessageBody;

public class CumQty extends MessageParent{

    public CumQty(Integer value) {

        super(value, 14);
    }
    public CumQty() {
        super(14);
    }
}

// якщо були закази до цього з такими акціями то тут відображається сума
// сума попередніх OrderQty і теперішнього