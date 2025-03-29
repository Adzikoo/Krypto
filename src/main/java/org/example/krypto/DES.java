package org.example.krypto;

import java.util.Arrays;

public class DES {
    public static class Key {
        private Bits value;

        public Key(byte[] bytes) {
            if (bytes.length == 7) {
                value = new Bits(bytes);

                return;
            }
            if (bytes.length == 8) {
                value = new Bits(bytes).removeParityBits();

                return;
            }
        }

        public Key(Bits bits) {
            if (bits.count() == 56) {
                this.value = bits;
            }
            else if (bits.count() == 64) {
                this.value = bits.removeParityBits();
            }
        }

        public Bits getBits() {
            return this.value;
        }
    }

    private static final int[] subKeyPermutationTable = new int[] {
            50,	43,	36,	29,	22,	15,	8,
            1,	51,	44,	37,	30,	23,	16,
            9,	2,	52,	45,	38,	31,	24,
            17,	10,	3,	53,	46,	39,	32,
            56,	49,	42,	35,	28,	21,	14,
            7,	55,	48,	41,	34,	27,	20,
            13,	6,	54,	47,	40,	33,	26,
            19,	12,	5,	25,	18,	11,	4
    };
    private static final int[] compressionPBoxPermutationTable = new int[] {
            14,	17,	11,	24,	1,	5,	3,	28,	15,	6,	21,	10,
            23,	19,	12,	4,	26,	8,	16,	7,	27,	20,	13,	2,
            41,	52,	31,	37,	47,	55,	30,	40,	51,	45,	33,	48,
            44,	49,	39,	56,	34,	53,	46,	42,	50,	36,	29,	32
    };

    private static final int[] IPpermutationTable = new int[] {
            58,	50,	42,	34,	26,	18,	10,	2,
            60,	52,	44,	36,	28,	20,	12,	4,
            62,	54,	46,	38,	30,	22,	14,	6,
            64,	56,	48,	40,	32,	24,	16,	8,
            57,	49,	41,	33,	25,	17,	9,	1,
            59,	51,	43,	35,	27,	19,	11,	3,
            61,	53,	45,	37,	29,	21,	13,	5,
            63,	55,	47,	39,	31, 23,	15,	7
    };

    private static final int[] inverseIPpermutationTable = new int[] {
            40,	8,	48,	16,	56,	24,	64,	32,
            39,	7,	47,	15,	55,	23,	63,	31,
            38,	6,	46,	14,	54,	22,	62,	30,
            37,	5,	45,	13,	53,	21,	61,	29,
            36,	4,	44,	12,	52,	20,	60,	28,
            35,	3,	43,	11,	51,	19,	59,	27,
            34,	2,	42,	10,	50,	18,	58,	26,
            33,	1,	41,	9,	49,	17,	57,	25
    };

    private static final int[] columnsSwashPermutationTable = new int[] {
            32,	1,	2,	3,	4,	5,
            4,	5,	6,	7,	8,	9,
            8,	9,	10,	11,	12,	13,
            12,	13,	14,	15,	16,	17,
            16,	17,	18,	19,	20,	21,
            20,	21,	22,	23,	24,	25,
            24,	25,	26,	27,	28,	29,
            28,	29,	30,	31,	32,	1
    };

    private static final byte[] sBoxLookup1 = new byte[] {
            14,	0,	4,	15,	13,	7,	1,	4,	2,	14,	15,	2,	11,	13,	8,	1,
            3,	10,	10,	6,	6,	12,	12,	11,	5,	9,	9,	5,	0,	3,	7,	8,
            4,	15,	1,	12,	14,	8,	8,	2,	13,	4,	6,	9,	2,	1,	11,	7,
            15,	5,	12,	11,	9,	3,	7,	14,	3,	10,	10,	0,	5,	6,	0,	13
    };
    private static final byte[] sBoxLookup2 = new byte[] {
            15,	3,	1,	13,	8,	4,	14,	7,	6,	15,	11,	2,	3,	8,	4,	14,
            9,	12,	7,	0,	2,	1,	13,	10,	12,	6,	0,	9,	5,	11,	10,	5,
            0,	13,	14,	8,	7,	10,	11,	1,	10,	3,	4,	15,	13,	4,	1,	2,
            5,	11,	8,	6,	12,	7,	6,	12,	9,	0,	3,	5,	2,	14,	15,	9
    };
    private static final byte[] sBoxLookup3 = new byte[] {
            10,	13,	0,	7,	9,	0,	14,	9,	6,	3,	3,	4,	15,	6,	5,	10,
            1,	2,	13,	8,	12,	5,	7,	14,	11,	12,	4,	11,	2,	15,	8,	1,
            13,	1,	6,	10,	4,	13,	9,	0,	8,	6,	15,	9,	3,	8,	0,	7,
            11,	4,	1,	15,	2,	14,	12,	3,	5,	11,	10,	5,	14,	2,	7,	12
    };
    private static final byte[] sBoxLookup4 = new byte[] {
            7,	13,	13,	8,	14,	11,	3,	5,	0,	6,	6,	15,	9,	0,	10,	3,
            1,	4,	2,	7,	8,	2,	5,	12,	11,	1,	12,	10,	4,	14,	15,	9,
            10,	3,	6,	15,	9,	0,	0,	6,	12,	10,	11,	1,	7,	13,	13,	8,
            15,	9,	1,	4,	3,	5,	14,	11,	5,	12,	2,	7,	8,	2,	4,	14
    };
    private static final byte[] sBoxLookup5 = new byte[] {
            2,	14,	12,	11,	4,	2,	1,	12,	7,	4,	10,	7,	11,	13,	6,	1,
            8,	5,	5,	0,	3,	15,	15,	10,	13,	3,	0,	9,	14,	8,	9,	6,
            4,	11,	2,	8,	1,	12,	11,	7,	10,	1,	13,	14,	7,	2,	8,	13,
            15,	6,	9,	15,	12,	0,	5,	9,	6,	10,	3,	4,	0,	5,	14,	3
    };
    private static final byte[] sBoxLookup6 = new byte[] {
            12,	10,	1,	15,	10,	4,	15,	2,	9,	7,	2,	12,	6,	9,	8,	5,
            0,	6,	13,	1,	3,	13,	4,	14,	14,	0,	7,	11,	5,	3,	11,	8,
            9,	4,	14,	3,	15,	2,	5,	12,	2,	9,	8,	5,	12,	15,	3,	10,
            7,	11,	0,	14,	4,	1,	10,	7,	1,	6,	13,	0,	11,	8,	6,	13
    };
    private static final byte[] sBoxLookup7 = new byte[] {
            4,	13,	11,	0,	2,	11,	14,	7,	15,	4,	0,	9,	8,	1,	13,	10,
            3,	14,	12,	3,	9,	5,	7,	12,	5,	2,	10,	15,	6,	8,	1,	6,
            1,	6,	4,	11,	11,	13,	13,	8,	12,	1,	3,	4,	7,	10,	14,	7,
            10,	9,	15,	5,	6,	0,	8,	15,	0,	14,	5,	2,	9,	3,	2,	12
    };
    private static final byte[] sBoxLookup8 = new byte[] {
            13,	1,	2,	15,	8,	13,	4,	8,	6,	10,	15,	3,	11,	7,	1,	4,
            10,	12,	9,	5,	3,	6,	14,	11,	5,	0,	0,	14,	12,	9,	7,	2,
            7,	2,	11,	1,	4,	14,	1,	7,	9,	4,	12,	10,	14,	8,	2,	13,
            0,	15,	6,	12,	10,	9,	13,	0,	15,	3,	3,	5,	5,	6,	8,	11
    };

    private static final byte[][] sboxLookups = new byte[][] {
            sBoxLookup1,
            sBoxLookup2,
            sBoxLookup3,
            sBoxLookup4,
            sBoxLookup5,
            sBoxLookup6,
            sBoxLookup7,
            sBoxLookup8,
    };

    private static final int[] pBoxPermutationTable = new int[] {
            16,	7,	20,	21,	29,	12,	28,	17,	1,	15,	23,	26,	5,	18,	31,	10,
            2,	8,	24,	14,	32,	27,	3,	9,	19,	13,	30,	6,	22,	11,	4,	25
    };

    private static Key generateNextKey(int numRound, Key prev) {
        Bits bits = prev.getBits();

        Bits[] halves = bits.permutate(subKeyPermutationTable).split(2);

        Bits left = halves[0];
        Bits right = halves[1];

        int rolAmount = 2;

        if (numRound == 1 || numRound == 2 || numRound == 9 || numRound == 16) {
            rolAmount = 1;
        }

        left = left.rotate(-rolAmount);
        right = right.rotate(-rolAmount);

        return new Key(Bits.join(left, right));
    }

    private static byte[] encryptDecryptInternal(byte[] data, Key key, boolean decrypt) {
        Key masterKey = key;
        Bits[] subkeys = new Bits[16];

        for (int i = 1; i <= 16; i++) {
            masterKey = generateNextKey(i, masterKey);

            subkeys[i - 1] = masterKey.getBits().permutate(compressionPBoxPermutationTable);
        }

        int numChunks = data.length / 8;

        byte[] chunkData = new byte[8];
        byte[] resultData = new byte[Math.ceilDiv(data.length, 8) * 8];

        for (int chunkNo = 0; chunkNo < numChunks; chunkNo++) {
            System.arraycopy(data, chunkNo * 8, chunkData, 0, 8);

            // chunk := permutation(chunk, IP)
            chunkData = new Bits(chunkData).permutate(IPpermutationTable).getByteData();

            Bits[] halves = new Bits(chunkData).split(2);

            // left := chunk rightshift 32
            Bits left = halves[0];
            // right := chunk and 0xFFFFFFFF
            Bits right = halves[1];

            // for i from 1 to 16 do
            for (int iteration = 0; iteration < 16; iteration++) {
                // tmp := right
                // left := tmp
                Bits tmp = right;

                // right := expansion(right, E)
                right = right.permutate(columnsSwashPermutationTable);

                // if decrypt reverse keys
                if (decrypt) {
                    right = right.xor(subkeys[15 - iteration]);
                }
                else {
                    right = right.xor(subkeys[iteration]);
                }

                // right := substitution(right)
                Bits[] rightGroups = right.split(8);

                for (int groupNo = 0; groupNo < 8; groupNo++) {
                    Bits group = rightGroups[groupNo];
                    int index = 0;

                    if (group.getBit(0)) index += 32;
                    if (group.getBit(1)) index += 16;
                    if (group.getBit(2)) index += 8;
                    if (group.getBit(3)) index += 4;
                    if (group.getBit(4)) index += 2;
                    if (group.getBit(5)) index += 1;

                    byte lookupVal = sboxLookups[groupNo][index];
                    boolean[] newGroupBits = new boolean[] {
                            (lookupVal & 8) != 0,
                            (lookupVal & 4) != 0,
                            (lookupVal & 2) != 0,
                            (lookupVal & 1) != 0
                    };

                    rightGroups[groupNo] = new Bits(newGroupBits);
                }
                // right := permutation(right, P)
                right = Bits.join(rightGroups).permutate(pBoxPermutationTable);

                // right := right xor left
                right = right.xor(left);
                left = tmp;
            }

            // var cipher_chunk := (right leftshift 32) or left
            chunkData = Bits.join(right, left).getByteData();

            // cipher_chunk := permutation(cipher_chunk, FP)
            chunkData = new Bits(chunkData).permutate(inverseIPpermutationTable).getByteData();

            System.arraycopy(chunkData, 0, resultData, chunkNo * 8, 8);
        }

        return resultData;
    }

    public static byte[] encrypt(byte[] data, Key key) {
        if (data.length % 8 > 0) {
            byte[] newData = new byte[Math.ceilDiv(data.length, 8) * 8];

            int diff = newData.length - data.length;

            newData[newData.length - 1] = (byte) diff;

            System.arraycopy(data, 0, newData, 0, data.length);

            data = newData;
        }
        else {
            byte[] newData = new byte[data.length + 8];

            newData[newData.length - 1] = (byte) 8;

            System.arraycopy(data, 0, newData, 0, data.length);

            data = newData;
        }

        return encryptDecryptInternal(data, key, false);
    }

    public static byte[] decrypt(byte[] data, Key key) {
        byte[] resultData = encryptDecryptInternal(data, key, true);

        byte bytesToRemove = resultData[resultData.length - 1];

        return Arrays.copyOf(resultData, resultData.length - bytesToRemove);
    }


}
