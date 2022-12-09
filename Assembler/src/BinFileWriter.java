import java.io.*;

class BinFileWriter {
    static FileOutputStream fileWriter;

    static {
        try {
            fileWriter = new FileOutputStream(new File(Assembler.newFilename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static int nextCodeAddr = 0;

    public BinFileWriter() throws IOException {
        fileWriter = new FileOutputStream(new File(Assembler.newFilename));
    }

    static public boolean writeBits(String binaryString) throws IOException {
        String High8 = binaryString.substring(0,8);
        String Low8 = binaryString.substring(8,16);
        fileWriter.write(Integer.parseInt(High8,2));
        fileWriter.write(Integer.parseInt(Low8,2));
        nextCodeAddr++;
        return true;
    }

}
