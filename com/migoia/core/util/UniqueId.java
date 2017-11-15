/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.migoia.core.util;

/**
 *
 * @author migoia
 */
public class UniqueId {

    private static UniqueId instance;
    private static int seq;

    /**
     * Creates a new instance of UniqueId
     */
    private UniqueId() {
        seq = 0;
    }

    /*
     Retorna un identificador unico de 10 caracteres, calculado en base al tiempo, una secuencia y
     un numero aleatorio. El resultado se codifica en base 64.
     El identificador se forma como:
     - XX, numero aleatorio
     - YY, numero secuencial
     - 99.999.999.999.999, tiempo actual en milisegundos, con 14 cifras valdria
     hasta el 5138-11-16 10:46:39.999
     El número máximo es 999999999999999999 que en binario es:
     110111.100000.101101.101011.001110.100111.011000.111111.111111.111111
    
     */
    static synchronized public String get() {
        if (instance == null) {
            instance = new UniqueId();
        }
        long res = (long) (Math.random() * 100);
        res = res * 100 + UniqueId.seq;
        res = res * 100000000000000L + System.currentTimeMillis();
        UniqueId.seq = UniqueId.seq == 99 ? 0 : UniqueId.seq + 1;
        return encode(res);
    }

    private static String encode(long value) {
        byte[] res = new byte[10];
        for (int i = res.length - 1; i >= 0; i--) {
            res[i] = EB64[(int) value & 0b00111111];
            value = value >> 6;
        }
        return new String(res);
    }
    private static final byte[] EB64 = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

}
