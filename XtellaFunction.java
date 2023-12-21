import java.util.List;

public class XtellaFunction {
    private String name;
    private List<XtellaParameter> parameters;
    private int address;

    public XtellaFunction(String name, List<XtellaParameter> parameters, int address) {
        name = name;
        parameters = parameters;
        address = address;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public int getAddress() {
        return address;
    }

    public int getArity() {
        return parameters.size();
    }

    public void execute(List<Object> arguments) {
       
    }

}

