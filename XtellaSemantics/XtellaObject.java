import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class XtellaValue {
	enum ValueType {
		VARIABLE,
		INTEGER_LITERAL,
		LIST_LITERAL,
		HASHMAP_LITERAL,
		TUPLE_LITERAL,
		FLOAT_LITERAL,
		STRING_LITERAL,
		FILE_HANDLE,
		RW_MODIFIER,
	}

	private ValueType type;
	private String name;
	private Object value;
}

public class XtellaMethod {
	enum MethodArity {
		UNARY,
		BINARY,
		TERNARY,
	}

	class NamedParam { 
		private String name;
		private XtellaValue value;

		public NamedParam(String name, XtellaValue value)  {
			this.name = name;
			this.value = value;
		}

		public String getName() { return this.name; }
		public XtellaValue getValue() { return this.value; }
	}

	private MethodArity arity;
	private String name;
	private List<String> params;
	private List<NamedParam> namedParams;
	private XtellaChunk bytecodeChunk;

}

public class XtellaObject {
	private boolean callable;
	private boolean builtin;
	private String name;
	private XtellaObject parent;
	private XtellaMethod initializeMethod;
	private XtellaMethod destroyMethod;
	private XtellaMethod assignMethod;
	private XtellaMethod binaryOpsMethod;
	private XtellaMethod unaryOpsMethod;
	private XtellaMethod callMethod;
	private Map<String, XtellaValue> fields;
	private Map<String, XtellaMethod> methods;
}


