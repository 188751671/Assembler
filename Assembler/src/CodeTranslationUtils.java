class CodeTranslationUtils{

    public boolean isRegister(String secondReg){
        if (secondReg.matches("(r|R)[0-7]")) return true; // r0-r7
        return false;
    }

    // return 3bits long binary string
    public String getRegisterBinaryNumber_3bits(String reg){
        int regNumber = Character.getNumericValue(reg.charAt(1));
        String binaryString = Integer.toBinaryString(regNumber);
        return GapFillerWith0(binaryString,3);
    }
    public boolean isConstant(String number){    //  0-32767
        if(number.startsWith("#"))
            if (Integer.parseInt(number.substring(1,number.length()))<=32767)
                return true;
        if (number.matches("[0-9]{1,5}"))
            return true;
        return false;
    }
    //      Constant is smaller than 32767 which takes up 15bits only.
    //      So if greater than 6bits, it uses the next 16bits, the 6bits above sets 0
    public String getConstantInBinary(String constant){

        String binaryString;
        if(constant.startsWith("#")){
            String cons = constant.substring(1,constant.length()); // trim off '#'
            binaryString = Integer.toBinaryString(Integer.parseInt(cons));
        }else {
            binaryString = Integer.toBinaryString(Integer.parseInt(constant));
        }


        if (binaryString.length()<=6){
            return GapFillerWith0(binaryString,6);
        }else {
            return GapFillerWith0(binaryString,16);
        }
    }

    // fill '0' to the start of a binary string till target length
    public String GapFillerWith0(String value, int targetLength){
        if (value.length()>targetLength){
            System.err.println("GapFiller Error at line"+ Assembler.lineCount);
            return null;
        }else if (value.length()==targetLength) return value;
        else {
            StringBuilder temp = new StringBuilder();
            for (int i=0; i<targetLength-value.length(); i++)
                temp.append("0");
            temp.append(value);
            return temp.toString();
        }
    }

}
