import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class XtellaFunction {
    private final Consumer<XtellaVM> functionBody;

    public XtellaFunction(Consumer<XtellaVM> functionBody) {
        this.functionBody = functionBody;
    }

    public void execute(XtellaVM vm) {
        this.functionBody.accept(vm);        
    }

    public Consumer<XtellaVM> getFunctionBody() {
	return this.functionBody;
    }
}

