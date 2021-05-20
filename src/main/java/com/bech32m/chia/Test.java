package com.bech32m.chia;

import org.apache.commons.codec.binary.Hex;

public class Test {

    public static void main(String[] args) throws Exception {
        AddressUtil addressUtil = AddressUtil.getInstance();
        String hash = "0x"+ Hex.encodeHexString(addressUtil.decode("xch1jp6frj3ecddur7dxak3n7lq0j75ltquh2zyd44epdu0d6704y2hqyky5hf"));
        System.out.println(hash.substring(2));

        System.out.println("0x"+ Hex.encodeHexString(addressUtil.decode("xch1jp6frj3ecddur7dxak3n7lq0j75ltquh2zyd44epdu0d6704y2hqyky5hf")));
        System.out.println(addressUtil.encode("xch".getBytes(),
                Hex.decodeHex("907491ca39c35bc1f9a6eda33f7c0f97a9f583975088dad7216f1edd79f522ae")));
    }

}
