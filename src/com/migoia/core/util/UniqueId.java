package com.migoia.core.util;

/**
 * Genera identificadores únicos dentro del ámbito de una aplicación.
 *
 * <p>
 * El identificador único tiene una longitud de 10 caracteres, calculado en base
 * al tiempo, una secuencia y un número aleatorio.</p>
 * <p>
 * El resultado se codifica en base 64.</p>
 * <p>
 * Basada en el patrón Singleton se invocaría como:
 * <code>String id = UniqueId.get();</code></p>
 *
 * <p>
 * El identificador se forma como:
 * <ul>
 * <li>XX, número aleatorio
 * <li>YYY, número secuencial
 * <li>9.999.999.999.999, tiempo actual en milisegundos
 * </ul>
 * </p>
 * <p>
 * Si se alcanza para un mismo milisegundo todos los posibles valores de secuencia
 * se espera al siguiente milisegundo.
 * <p>
 * Con las 14 cifras del tiempo valdría hasta el 2286-11-20 18:46:39.999.</p>
 * <p>
 * El número máximo es 999999999999999999 que en binario es
 * 110111.100000.101101.101011.001110.100111.011000.111111.111111.111111 que da
 * lugar a los 10 caracteres del identificador.</p>
 *
 * @author migoia
 * @version 1.0
 * @since 2016.04.23
 */
public class UniqueId {

    private static UniqueId instance;
    private int seq;
    private long t0 = 0;
    private int s0 = -1;

    private UniqueId() {
        seq = (int) (Math.random()*1000);
    }

    /**
     * Retorna un identificador único
     *
     * @return String de 10 caracteres con un identificador único
     * para el sistema
     */
    static synchronized public String get() {
        if (instance == null) {
            instance = new UniqueId();
        }
        return instance.generate();
    }
    
    private String generate(){
        long res = (long) (Math.random() * 100);
        res = res * 1000 + seq;
        long t1 = System.currentTimeMillis();
        if(t0 == t1){
            if(s0 == seq){
                while(t0 == t1){
                    t1 = System.currentTimeMillis();
                }
                t0 = t1;
                s0 = seq;
            }
        } else {
            t0 = t1;
            s0 = seq;
        }
        res = res * 10000000000000L + t1;
        seq = seq == 999 ? 0 : seq + 1;
        return encode(res);
    }

    private String encode(long value) {
        byte[] res = new byte[10];
        for (int i = res.length - 1; i >= 0; i--) {
            res[i] = EB64[(int) value & 0b00111111];
            value = value >> 6;
        }
        return new String(res);
    }

    private final byte[] EB64 = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '-'
    };

}
