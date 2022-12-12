import java.util.HashMap;

class SymbolTable {
    private HashMap<String , Integer> symbolTable;
    private int nextHeapMemoryAddress = Assembler.HEAP_START_ADDR;
    public SymbolTable() {
        symbolTable = new HashMap<String, Integer>();
    }

    public boolean setCodeAddrTag(String tag){
        if(!isValidSymbol(tag)){
            System.err.println("Not valid symbol name at line " + Assembler.lineCount);
            return false;
        }
        if (symbolTable.containsKey(tag)){
            System.err.println("symbol name already defined, at line " + Assembler.lineCount);
            return  false;
        }

        symbolTable.put(tag,BinFileWriter.nextCodeAddr);
        nextHeapMemoryAddress++;
        return true;
    }

    public boolean setSymbolValue(String valueName){
        if(!isValidSymbol(valueName)){
            System.err.println("Not valid symbol name at line " + Assembler.lineCount);
            return false;
        }
        if (symbolTable.containsKey(valueName)){
            System.err.println("symbol name already defined, at line " + Assembler.lineCount);
            return  false;
        }
        symbolTable.put(valueName,nextHeapMemoryAddress);

        nextHeapMemoryAddress++;
        return true;
    }

    public Integer getSymbolValue(String valueName){
        return symbolTable.get(valueName);
    }

    public boolean isValidSymbol(String string){
        return string.matches("^[a-zA-Z][a-zA-Z0-9_]*");  // starting with a letter, followed by some numbers letters and underscore
    }

}
