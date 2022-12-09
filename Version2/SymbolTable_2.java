import java.util.HashMap;

class SymbolTable_2 {
    public HashMap<String , Integer> symbolValueTable;
    public HashMap<String , Integer> codeAddrTagTable;
    private int nextHeapMemoryAddress = Assembler.HEAP_START_ADDR;
    public SymbolTable_2() {
        symbolValueTable = new HashMap<String, Integer>();
        codeAddrTagTable = new HashMap<String, Integer>();
    }

    public boolean setCodeAddrTag(String tag){
        if(!isValidSymbol(tag)){
            System.err.println("Not valid symbol name at line " + Assembler.lineCount);
            return false;
        }
        if (codeAddrTagTable.containsKey(tag)){
            System.err.println("symbol name already defined, at line " + Assembler.lineCount);
            return  false;
        }

        codeAddrTagTable.put(tag,BinFileWriter.nextCodeAddr);
        nextHeapMemoryAddress++;
        return true;
    }

    public boolean setSymbolValue(String valueName){
        if(!isValidSymbol(valueName)){
            System.err.println("Not valid symbol name at line " + Assembler.lineCount);
            return false;
        }
        if (symbolValueTable.containsKey(valueName)){
            System.err.println("symbol name already defined, at line " + Assembler.lineCount);
            return  false;
        }
        symbolValueTable.put(valueName,nextHeapMemoryAddress);

        nextHeapMemoryAddress++;
        return true;
    }

    public boolean isValidSymbol(String string){
        return string.matches("^[a-zA-Z][a-zA-Z0-9_]*");  // starting with a letter, followed by some numbers letters and underscore
    }


}