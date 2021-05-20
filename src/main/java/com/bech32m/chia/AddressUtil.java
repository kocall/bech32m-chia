package com.bech32m.chia;

import com.google.common.primitives.Bytes;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressUtil {
    private static AddressUtil instance = new AddressUtil();

    private AddressUtil() {}

    public static AddressUtil getInstance() {
        return instance;
    }

    public String getPuzzleHash(String addr) throws Exception {
       return  "0x"+ Hex.encodeHexString(decode(addr));
    }

    public String getAddress(String puzzleHash) throws Exception {
        if (puzzleHash.startsWith("0x") || puzzleHash.startsWith("0X")){
            puzzleHash = puzzleHash.substring(2);
        }
       return  encode("xch".getBytes(),Hex.decodeHex(puzzleHash));
    }

    public byte[] decode(String addr) throws Exception {
        Pair<byte[], byte[]> p = Bech32Util.getInstance().bech32Decode(addr);

        String hrpgot = new String(p.getLeft());
        if (!hrpgot.equalsIgnoreCase("xch"))    {
            throw new Exception("invalid segwit human readable part");
        }

        byte[] data = p.getRight();
        return convertBits(Bytes.asList(Arrays.copyOfRange(data, 0, data.length)), 5, 8, false);
    }

    public String encode(byte[] hrp, byte[] witnessProgram) throws Exception    {
        byte[] prog = convertBits(Bytes.asList(witnessProgram), 8, 5, true);
        return Bech32Util.getInstance().bech32Encode(hrp, prog);
    }

    private byte[] convertBits(List<Byte> data, int fromBits, int toBits, boolean pad) throws Exception    {
        int acc = 0;
        int bits = 0;
        int maxv = (1 << toBits) - 1;
        List<Byte> ret = new ArrayList<Byte>();

        for(Byte value : data)  {
            short b = (short)(value & 0xff);

            if (b < 0) {
                throw new Exception();
            }
            else if ((b >> fromBits) > 0) {
                throw new Exception();
            }

            acc = (acc << fromBits) | b;
            bits += fromBits;
            while (bits >= toBits)  {
                bits -= toBits;
                ret.add((byte)((acc >> bits) & maxv));
            }
        }

        if(pad && (bits > 0))    {
            ret.add((byte)((acc << (toBits - bits)) & maxv));
        }
        else if (bits >= fromBits || (byte)(((acc << (toBits - bits)) & maxv)) != 0)    {
            throw new Exception("panic");
        }
        return Bytes.toArray(ret);
    }
}
