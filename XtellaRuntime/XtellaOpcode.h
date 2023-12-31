#include <stdbool.h>

struct XtellaOpcode {
  int nr;
  int n_params;
  int n_result;
  bool builtin;
  char *docs;
  void (*operation)(void);
};

struct XtellaOpcode create_opcode(int nr, int n_params, int n_result,
                                  bool builtin, char *docs,
                                  void (*operation)(void)) {
  struct XtellaOpcode opcode;
  opcode.nr = nr;
  opcode.n_params = n_params;
  opcode.n_result = n_result;
  opcode.builtin = builtin;
  opcode.docs = docs;
  opcode.operation = operation;
  return opcode;
}

void execute_opcode(struct XtellaOpcode opcode) {
  if (opcode.operation != NULL) {
    opcode.operation();
  } else {
    printf("Error: Operation not defined for opcode %d\n", opcode.nr);
  }
}

char *get_opcode_docs(struct XtellaOpcode opcode) { return opcode.docs; }
