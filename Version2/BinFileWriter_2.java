import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class BinFileWriter_2 {
    static FileOutputStream fileWriter;

    static {
        try {
            fileWriter = new FileOutputStream(new File(Assembler.newFilename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static int nextCodeAddr = 0;

    public BinFileWriter_2() throws IOException {
        fileWriter = new FileOutputStream(new File(Assembler.newFilename));
    }

    static public boolean writeBits(int Integer_16bits) throws IOException {

        int high8 = (Integer_16bits & 0B1111111100000000) >> 8;
        int low8  = Integer_16bits & 0B0000000011111111;

        fileWriter.write(high8);
        fileWriter.write(low8);
        nextCodeAddr++;
        return true;
    }


}
