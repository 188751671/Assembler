import java.io.*;

public class Assembler_2 {
    static final int HEAP_START_ADDR = 1024;
    static int lineCount=0;
    static String newFilename;
    protected static BufferedReader br;
    protected static SymbolTable symbolTable = new SymbolTable();
    static CodeTranslationUtils_2 utils = new CodeTranslationUtils_2();

    public static void main(String[] args) throws IOException {

        // No File
        if (args[0] == null || args[0].isEmpty()){
            System.err.println("no file parameters");
            return;
        }

        // Wrong File Extension
        int lastPoint_index = args[0].lastIndexOf(".");
        if (!args[0].substring(lastPoint_index + 1).equals("sal")){
            System.err.println("file extension isn't sal");
        }
        newFilename = args[0].substring(0,lastPoint_index + 1) + "bin";
        br= new BufferedReader(new FileReader(new File(args[0])));

        // in sal file, first line has to be .data or .code
        String[] DataOrCode = getSubstringsOfNextValidLine();
        if (!DataOrCode[0].equals(".data") && !DataOrCode[0].equals(".code"))   return;

        if (DataOrCode[0].equals(".data"))
            dataProcessor();

        codeProcessor();
    }

    private static void dataProcessor() throws IOException {
        String[] substrings;
        while((substrings = getSubstringsOfNextValidLine()) !=null ){
            if (substrings[0].equals(".code"))      return;
            if (symbolTable.setSymbolValue(substrings[0]) == false)  return;
            // here successfully added the symbol, do the next
        }
    }
    private static void codeProcessor() throws IOException {
        String[] substrings;
        while((substrings = getSubstringsOfNextValidLine()) !=null ){

            if (substrings[0].equals("JMP")){
                if (substrings.length>2)  return;  // illegal string at [2]

                    int bits16 = 0;
                    bits16 += 0B0100 << 12;
                    bits16 += 0B000  << 9;
                    if(!operandHanlder(substrings[1]))  return;
                    // successfully run

            }else {
                // apart from JMP, instructions have substring[1] as Reg
                if (substrings.length>1 && !utils.isRegister(substrings[1])){
                    System.err.println("[1] isn't a Register, at line " + Assembler.lineCount);
                    return;
                }

                StringBuilder bits16 = new StringBuilder(16);
                switch (substrings[0]) {
                    case "ADD":
                        bits16.append("0000");//  ADD
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));  //e.g. r3  011
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;
                    case "SUB":
                        bits16.append("0001");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;
                    case "AND":
                        bits16.append("0010");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;
                    case "OR":
                        bits16.append("0011");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;

                    case "LOAD":
                        bits16.append("1100");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;
                    case "STORE":
                        bits16.append("1101");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;

                    case "JGT":
                        bits16.append("0101");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;
                    case "JLT":
                        bits16.append("0110");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;
                    case "JEQ":
                        bits16.append("0111");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        if (!operandHanlder(substrings[2],bits16))  return;
                        break;

                        //  Size:0  RVA:11    No Operand:000000  for INC, DEC, NOT
                    case "INC":
                        bits16.append("1001");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        bits16.append("0"); // size
                        bits16.append("11");  // RVA
                        bits16.append("000000"); // 6bits operand
                        if(!BinFileWriter.writeBits(bits16.toString()))  return;
                        break;
                    case "DEC":
                        bits16.append("1010");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        bits16.append("0"); // size
                        bits16.append("11");  // RVA
                        bits16.append("000000"); // 6bits operand
                        if(!BinFileWriter.writeBits(bits16.toString()))  return;
                        break;
                    case "NOT":
                        bits16.append("1011");
                        bits16.append(utils.getRegisterBinaryNumber_3bits(substrings[1]));
                        bits16.append("0"); // size
                        bits16.append("11");  // RVA
                        bits16.append("000000"); // 6bits operand
                        if(!BinFileWriter.writeBits(bits16.toString()))  return;
                        break;

                    default:
                        // code address tag
                        if (substrings.length == 1 && substrings[0].endsWith(":")) {
                            // trim off the last colon
                            if (!symbolTable.setCodeAddrTag(
                                    substrings[0].substring(0, substrings[0].length() - 1)))
                                return;   //if setCodeAddrTag return false, just return; to end the program
                        } else {
                            System.err.println("Unrecognized string at line" + lineCount);
                            return;  // [0] unrecognized instruction string
                        }

                } // Instructions Switch
            }
        } // While loop
    }

    /**
     * @return one to three substrings containing [0]instruction/label/variable, [1]operand1, [2]operand2 (if any)
     * Comments are completely discarded
     * @throws IOException
     */
    private static String[] getSubstringsOfNextValidLine() throws IOException {

        String oneLine;
        while ((oneLine = br.readLine()) != null){
            if (oneLine.isEmpty()){
                lineCount++;
                continue;
            }
            lineCount++;

            // a line splits into 4 substrings at most, with regexes: "OneOrMoreSpaces" Or ",ZeroOrMoreSpaces"
            // Last substring is disregarded if it correctly starts with "//" ,otherwise exit with syntax error
            oneLine = oneLine.trim();
            if (oneLine.indexOf("//")!=-1)  // if there are // comment, cut it off
                oneLine = oneLine.substring(0,oneLine.indexOf("//"));

            if (oneLine.isEmpty())  continue;
            String[] substrings = oneLine.split("\s+|,\s*",4);   // at most, there are 4 substrings, last is '//'

            return substrings;
        }
        return null;  // null means Finished reading
    }

    static boolean operandHanlder(String operand, int bits16) throws IOException {
        // 1. First  isRegister?  r3
        // 2. Second isConstant?  #55555
        // 3. Third  isValidSymbol  --Yes, check 2 Tables--
        //              if(getCodeAddrTag !=null)
        //              if(getSymbolValue !=null)
        // 4. Lastly Unrecognized Operand, return; to end
        if (utils.isRegister(operand)) {    // r1 is actually memory addr[1]

            String operand6bits = utils.GapFillerWith0(utils.getRegisterBinaryNumber_3bits(operand),6);
            return BinFileWriter.writeBits(bits16.toString());
        }
        if (utils.isConstant(operand)) {
            String binaryBits = utils.getConstantInBinary(operand);
            if (binaryBits.length()==6){
                bits16.append("0");     // size
                bits16.append("01");    // RVA 01 -- Operand is Constant
                bits16.append(binaryBits);
                return BinFileWriter.writeBits(bits16.toString());
            }else {
                bits16.append("1");     // size 1
                bits16.append("01");    // RVA 01 -- Operand is Constant / value
                bits16.append("000000");// 000000   -- value is in the next 16bits
                BinFileWriter.writeBits(bits16.toString());
                return BinFileWriter.writeBits(binaryBits);
            }
        }
        if (symbolTable.isValidSymbol(operand)){
            Integer value = symbolTable.getSymbolValue(operand);
            if (value!=null){                                    // found as CodeTag
                String str = Integer.toBinaryString(value);
                if (str.length()>6){
                    str = utils.GapFillerWith0(str,22);
                    bits16.append("1");     // size 1
                    bits16.append("01");    // RVA 01 -- Operand is Constant / value
                    bits16.append(str.substring(0,6));
                    BinFileWriter.writeBits(bits16.toString());
                    return BinFileWriter.writeBits(str.substring(6,22));
                }else {
                    bits16.append("0");     // size 1
                    bits16.append("01");
                    bits16.append(str.substring(0,6));
                    return BinFileWriter.writeBits(bits16.toString());
                }
            }
            Integer addr = symbolTable.getCodeAddrTag(operand);
            if (addr!=null){
                String str = Integer.toBinaryString(addr);
                if (str.length()>6){
                    str = utils.GapFillerWith0(str,22);
                    bits16.append("1");     // size 1
                    bits16.append("10");    // RVA 10 -- Operand is an address
                    bits16.append(str.substring(0,6));
                    BinFileWriter.writeBits(bits16.toString());
                    return BinFileWriter.writeBits(utils.GapFillerWith0(str,22).substring(6,22));
                }else {
                    bits16.append("0");     // size 1
                    bits16.append("10");
                    bits16.append(utils.GapFillerWith0(str,6));
                    return BinFileWriter.writeBits(bits16.toString());
                }
            }
        }
        System.err.println("operandHanlder : Unrecognized Operand at line"+lineCount);
        return false;   // Unrecognized Operand
    }

}

