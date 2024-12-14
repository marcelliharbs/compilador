import java.io.BufferedReader;
import java.util.*;

public class Semantico implements Constants {

    private StringBuilder codigo;
    private Stack<String> pilha_tipos;
    private String operador_relacional;
    private Stack<String> pilha_rotulos;
    private List<String> lista_identificadores;
    private Map<String, String> tabela_simbolos;
    private int contadorRotulos;

    public Semantico() {
        codigo = new StringBuilder();
        pilha_tipos = new Stack<>();
        operador_relacional = "";
        pilha_rotulos = new Stack<>();
        lista_identificadores = new ArrayList<>();
        tabela_simbolos = new HashMap<>();
        contadorRotulos = 0;
    }

    public void executeAction(int action, Token token) throws SemanticError {
        System.out.println("Ação #" + action + ", Token: " + token);
        switch (action) {
            case 100:
                acao_semantica100();
                break;
            case 101:
                acao_semantica101();
                break;
            case 102:
                acao_semantica102(token);
                break;
            case 103:
                acao_semantica103(token);
                break;
            case 104:
                acao_semantica104(token);
                break;
            case 105:
                acao_semantica105(token);
                break;
            case 106:
                acao_semantica106(token);
                break;
            case 107:
                acao_semantica107();
                break;
            case 108:
                acao_semantica108();
                break;
            case 109:
                acao_semantica109();
                break;
            case 110:
                acao_semantica110();
                break;
            case 111:
                acao_semantica111();
                break;
            case 112:
                acao_semantica112();
                break;
            case 113:
                acao_semantica113();
                break;
            case 114:
                acao_semantica114();
                break;
            case 115:
                acao_semantica115();
                break;
            case 116:
                acao_semantica116();
                break;
            case 117:
                acao_semantica117();
                break;
            case 118:
                acao_semantica118();
                break;
            case 119:
                acao_semantica119();
                break;
            case 120:
                acao_semantica120();
                break;
            case 121:
                acao_semantica121(token);
                break;
            case 122:
                acao_semantica122();
                break;
            case 123:
                acao_semantica123();
                break;
            case 124:
                acao_semantica124();
                break;
            case 125:
                acao_semantica125();
                break;
            case 126:
                acao_semantica126();
                break;
            case 127:
                acao_semantica127(token);
                break;
            case 128:
                acao_semantica128(token);
                break;
            case 129:
                acao_semantica129(token);
                break;
            case 130:
                acao_semantica130(token);
                break;
            case 131:
                acao_semantica131();
                break;
        }
    }

    private void acao_semantica100() {
        codigo.append(".assembly extern mscorlib {}\n")
                .append(".assembly _codigo_objeto{}\n")
                .append(".module _codigo_objeto.exe\n")
                .append(".class public UNICA{\n")
                .append(".method static public void _principal() {\n")
                .append(".entrypoint\n");
    }

    private void acao_semantica101() {
        codigo.append("ret\n").append("}\n").append("}\n");
    }

    private void acao_semantica102(Token token) throws SemanticError {
        for (String identificador : lista_identificadores) {

            if (tabela_simbolos.containsKey(identificador)) { //arrumar
                throw new SemanticError(identificador + " já declarado");
            }

            String tipo = "";

            if (identificador.startsWith("i_")) {
                tipo = "int64";
            } else if (identificador.startsWith("f_")) {
                tipo = "float64";
            } else if (identificador.startsWith("s_")) {
                tipo = "string";
            } else if (identificador.startsWith("b_")) {
                tipo = "bool";
            }

            tabela_simbolos.put(identificador, tipo);

            codigo.append(".locals (" + tipo + " " + identificador + ")\n");
        }

        lista_identificadores.clear();
    }

    private void acao_semantica103(Token token) throws SemanticError {
        String tipo = pilha_tipos.pop();

        if (tipo.equals("int64")) {
            codigo.append("conv.i8\n");
        }

        int dup = lista_identificadores.size() - 1;
        for (int i = 0; i < dup; i++) {
            codigo.append("dup\n");
        }

        for (String identificador : lista_identificadores) {
            if (!tabela_simbolos.containsKey(identificador)) {
                throw new SemanticError(identificador + " não declarado");
            }

            codigo.append("stloc ").append(identificador).append("\n");
        }

        lista_identificadores.clear();
    }

    private void acao_semantica104(Token token) {
        lista_identificadores.add(token.getLexeme());
    }

    private void acao_semantica105(Token token) throws SemanticError {
        String identificador = token.getLexeme();

        if (!tabela_simbolos.containsKey(identificador)) {
            throw new SemanticError(identificador + " não declarado");
        }

        String tipo = tabela_simbolos.get(identificador);

        codigo.append("call string [mscorlib]System.Console::ReadLine()\n");

        switch (tipo) {
            case "int64":
                codigo.append("call int64 [mscorlib]System.Int64::Parse(string)\n");
                break;
            case "float64":
                codigo.append("call float64 [mscorlib]System.Double::Parse(string)\n");
                break;
            case "bool":
                codigo.append("call bool [mscorlib]System.Boolean::Parse(string)\n");
                break;
            case "string":
                break;
        }

        codigo.append("stloc ").append(identificador).append("\n");
    }

    private void acao_semantica106(Token token) {
        String constante = token.getLexeme();

        codigo.append("ldstr ").append(constante).append("\n");

        codigo.append("call void [mscorlib]System.Console::Write(string)\n");
    }

    private void acao_semantica107() {
        codigo.append("call void [mscorlib]System.Console::WriteLine()\n");
    }

    private void acao_semantica108() {
        String tipo = pilha_tipos.pop();

        switch (tipo) {
            case "int64":
                codigo.append("conv.i8\n")
                        .append("call void [mscorlib]System.Console::Write(int64)\n");
                break;
            case "float64":
                codigo.append("call void [mscorlib]System.Console::Write(float64)\n");
                break;
            case "string":
                codigo.append("call void [mscorlib]System.Console::Write(string)\n");
                break;
            case "bool":
                codigo.append("call void [mscorlib]System.Console::Write(bool)\n");
                break;
        }
    }

    private void acao_semantica109() {
        String rotulo1 = criarNovoRotulo();

        pilha_rotulos.push(rotulo1);

        String rotulo2 = criarNovoRotulo();

        codigo.append("brfalse ").append(rotulo2).append("\n");

        pilha_rotulos.push(rotulo2);
    }

    private String criarNovoRotulo() {
        return "R" + (contadorRotulos++);
    }

    private void acao_semantica110() {
        String rotulo_desempilhado2 = pilha_rotulos.pop();
        String rotulo_desempilhado1 = pilha_rotulos.pop();

        codigo.append("br ").append(rotulo_desempilhado1).append("\n");

        pilha_rotulos.push(rotulo_desempilhado1);

        codigo.append(rotulo_desempilhado2).append(":\n");
    }

    private void acao_semantica111() {
        String rotulo_desempilhado = pilha_rotulos.pop();

        codigo.append(rotulo_desempilhado).append(":\n");
    }

    private void acao_semantica112() {
        String novo_rotulo = criarNovoRotulo();

        codigo.append("brfalse ").append(novo_rotulo).append("\n");

        pilha_rotulos.push(novo_rotulo);
    }

    private void acao_semantica113() {
        String novo_rotulo = criarNovoRotulo();

        codigo.append(novo_rotulo).append(":").append("\n");

        pilha_rotulos.push(novo_rotulo);
    }

    private void acao_semantica114() {
        String rotulo_desempilhado = pilha_rotulos.pop();

        codigo.append("brtrue ").append(rotulo_desempilhado).append("\n");
    }

    private void acao_semantica115() {
        String rotulo_desempilhado = pilha_rotulos.pop();

        codigo.append("brfalse ").append(rotulo_desempilhado).append("\n");
    }

    private void acao_semantica116() {
        String tipo1 = pilha_tipos.pop();
        String tipo2 = pilha_tipos.pop();

        if (tipo1.equals(tipo2)) {
            pilha_tipos.push("bool");
        }

        codigo.append("and\n");
    }

    private void acao_semantica117() {
        String tipo1 = pilha_tipos.pop();
        String tipo2 = pilha_tipos.pop();

        if (tipo1.equals(tipo2)) {
            pilha_tipos.push("bool");
        }

        codigo.append("or\n");
    }

    private void acao_semantica118() {
        pilha_tipos.push("bool");

        codigo.append("ldc.i4.1\n");
    }

    private void acao_semantica119() {
        pilha_tipos.push("bool");

        codigo.append("ldc.i4.0\n");
    }

    private void acao_semantica120() {
        codigo.append("ldc.i4.1\n").append("xor\n");
    }

    private void acao_semantica121(Token token) {
        operador_relacional = token.getLexeme();
    }

    private void acao_semantica122() {
        String tipo1 = pilha_tipos.pop();
        String tipo2 = pilha_tipos.pop();

        if (tipo1.equals(tipo2)) {
            pilha_tipos.push("bool");
        }

        switch (operador_relacional) {
            case "==":
                codigo.append("ceq\n");
                break;
            case ">"://maior que
                codigo.append("cgt\n");
                break;
            case "<"://menor que
                codigo.append("clt\n");
                break;
            case "!=":
                codigo.append("cne\n");
                break;
        }
    }

    private void acao_semantica123() {
        String tipo1 = pilha_tipos.pop();
        String tipo2 = pilha_tipos.pop();

        if (tipo1.equals("float64") || tipo2.equals("float64")) {
            pilha_tipos.push("float64");
        } else {
            pilha_tipos.push("int64");
        }

        codigo.append("add\n");
    }

    private void acao_semantica124() {
        String tipo1 = pilha_tipos.pop();
        String tipo2 = pilha_tipos.pop();

        if (tipo1.equals("float64") || tipo2.equals("float64")) {
            pilha_tipos.push("float64");
        } else {
            pilha_tipos.push("int64");
        }

        codigo.append("sub\n");
    }

    private void acao_semantica125() {
        String tipo1 = pilha_tipos.pop();
        String tipo2 = pilha_tipos.pop();

        if (tipo1.equals("float64") || tipo2.equals("float64")) {
            pilha_tipos.push("float64");
        } else {
            pilha_tipos.push("int64");
        }

        codigo.append("mul\n");
    }

    private void acao_semantica126() {
        String tipo1 = pilha_tipos.pop();
        String tipo2 = pilha_tipos.pop();

        pilha_tipos.push("float64");

        codigo.append("div\n");
    }

    private void acao_semantica127(Token token) throws SemanticError {
        String identificador = token.getLexeme();

        if (!tabela_simbolos.containsKey(identificador)) {
            throw new SemanticError(identificador + " não declarado");
        }

        if (identificador.startsWith("i_")) {
            pilha_tipos.push("int64");
            codigo.append("ldloc ").append(token.getLexeme()).append("\n").append("conv.r8\n");

        } else if (identificador.startsWith("f_")) {
            pilha_tipos.push("float64");
            codigo.append("ldloc ").append(token.getLexeme()).append("\n");

        } else if (identificador.startsWith("s_")) {
            pilha_tipos.push("string");
            codigo.append("ldloc ").append(token.getLexeme()).append("\n");

        } else if (identificador.startsWith("b_")) {
            pilha_tipos.push("bool");
            codigo.append("ldloc ").append(token.getLexeme()).append("\n");
        }

    }

    private void acao_semantica128(Token token) {
        pilha_tipos.push("int64");

        codigo.append("ldc.i8 ").append(token.getLexeme()).append("\n").append("conv.r8 \n");
    }

    private void acao_semantica129(Token token) {
        pilha_tipos.push("float64");

        String lexema = token.getLexeme().replace(',', '.');

        codigo.append("ldc.r8 ").append(lexema).append("\n");

    }

    private void acao_semantica130(Token token) {
        pilha_tipos.push("string");

        codigo.append("ldstr ").append(token.getLexeme()).append("\n");
    }

    private void acao_semantica131() {
        codigo.append("ldc.r8 -1.0\n").append("mul\n");
        }

    public String getCodigoObjeto() {
        return codigo.toString();
    }
}