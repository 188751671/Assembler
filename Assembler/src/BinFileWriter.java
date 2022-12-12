import java.io.*;
class BinFileWriter {
    static FileOutputStream fileWriter;
    public static String buffer = "";
    static {
        try {
            fileWriter = new FileOutputStream(Assembler.newFilename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    static int nextCodeAddr = 0;

    public BinFileWriter() throws IOException {
        fileWriter = new FileOutputStream(new File(Assembler.newFilename));
    }
    public static int getNextCodeAddr() {
        return nextCodeAddr;
    }

    static public boolean writeBits(String binaryString) {
        buffer = buffer + binaryString +"\n";
        nextCodeAddr++;
        return true;
    }

    static public void writeFile() throws IOException {
        fileWriter.write(buffer.getBytes());
    }
    static public void writeFile(String file) throws IOException{
        fileWriter.write(file.getBytes());
    }
}
