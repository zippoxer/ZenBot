package network;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.*;

public class NetworkMessage {

    private ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(
            bos);
    private ByteArrayInputStream bis;
    private LittleEndianDataInputStream dis;
    private byte[] bytes;

    public NetworkMessage() {
    }

    public NetworkMessage(byte[] bytes) {
        this.bytes = bytes;
        bis = new ByteArrayInputStream(bytes);
        dis = new LittleEndianDataInputStream(bis);
    }
    
    public void write(short[] b) throws IOException {
        for(int i = 0; i < b.length; i++) {
            writeByte(b[i]);
        }
    }
    
    public void writeChars(byte[] b) throws IOException {
        dos.write(b);
    }
    
    public void writeByte(short v) throws IOException {
        dos.writeByte(v);
    }
    
    public void writeShort(short v) throws IOException {
        dos.writeShort(v);
    }
    
    public void writeUshort(int v) throws IOException {
        writeShort((short) v);
    }
    
    public void writeInt(int v) throws IOException {
        dos.writeInt(v);
    }
    
    public void writeUint(long v) throws IOException {
        writeInt((int) v);
    }
    
    public void writeString(String v) throws IOException {
        writeUshort(v.length());
        writeChars(v.getBytes());
    }
    
    public void writeBool(boolean v) throws IOException {
        writeByte((byte) (v ? 1 : 0));
    }
    
    public short[] read(int length) throws IOException {
        short[] b = new short[length];
        for(int i = 0; i < length; i++) {
            b[i] = readByte();
        }
        return b;
    }
    
    public byte[] readChars(int length) throws IOException {
        byte[] b = new byte[length];
        dis.read(b);
        return b;
    }

    public short readByte() throws IOException {
        return (short) dis.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return dis.readShort();
    }
    
    public int readUshort() throws IOException {
        int n = readShort();
        return n & 0x0000FFFF;
    }

    public int readInt() throws IOException {
        return dis.readInt();
    }
    
    public long readUint() throws IOException {
        long n = readInt();
        return n & 0x00000000FFFFFFFFL;
    }

    public String readString() throws IOException {
        int n = readUshort();
        if (n < 1) {
            return "";
        }
        return new String(readChars(n));
    }

    public boolean readBool() throws IOException {
        return (readByte() == 1);
    }

    public void send(OutputStream os) throws IOException {
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        LittleEndianDataOutputStream dos2 = new LittleEndianDataOutputStream(
                bos2);
        dos2.writeShort(bos.size());
        dos2.write(bos.toByteArray());
        os.write(bos2.toByteArray());
    }

    public static NetworkMessage receive(InputStream is) throws IOException {
        LittleEndianDataInputStream dis = new LittleEndianDataInputStream(is);
        int size = dis.readUnsignedShort();
        byte[] data = new byte[size];
        dis.readFully(data); // readFully won't continue until it fills data
        return new NetworkMessage(data);
    }

    public int available() {
        return bis.available();
    }
    
    public byte[] getBytes() {
        return bytes;
    }
}
