import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class XtellaType {
	private boolean callable;
	private boolean immediate;
	private boolean builtin;
	private String name;
	private XtellaType parent;
	private XtellaMethod initializeMethod;
	private XtellaMethod destroyMethod;
	private XtellaMethod assignMethod;
	private XtellaMethod binaryOpsMethod;
	private XtellaMethod unaryOpsMethod;
	private Map<String, XtellaValue> fields;
	private Map<String, XtellaMethod> methods;
}

public class XtellaValue {
	enum ValueType {
		VARIABLE,
		INTEGER_LITERAL,
		FLOAT_LITERAL,
		STRING_LITERAL,
		FILE_HANDLE,
		UNARY_OPERATOR,
		BINARY_OPERATOR,
	}

	private String name;
	private Object value;
}

public class XtellaMethod {}


