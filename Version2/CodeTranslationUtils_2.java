class CodeTranslationUtils_2 {

    public boolean isRegister(String secondReg){
        if (secondReg.matches("(r|R)[0-7]")) return true; // r0-r7
        return false;
    }

    // return 3bits long binary string
    public int getRegisterNumber(String reg){
        return Character.getNumericValue(reg.charAt(1));
    }


    //      Constant is smaller than 32767 which takes up 15bits only.
    //      So if greater than 6bits, it uses the next 16bits, the 6bits above sets 0
    public Integer getConstant(String number){    //  0-32767
        int i = Integer.parseInt(number.substring(1,number.length()));   // strip off '#'
        if (i<=32767 && i>0)
            return i;
        return null;
    }

}