package org.example.krypto;

import java.util.Arrays;

public class Bits {
    private final boolean[] data;

    public Bits(boolean[] data) {
        this.data = data;
    }

    public Bits(byte[] data) {
        this.data = new boolean[data.length * 8];

        for (int i = 0; i < data.length; i++) {
            this.data[i * 8] = (data[i] & 128) != 0;
            this.data[i * 8 + 1] = (data[i] & 64) != 0;
            this.data[i * 8 + 2] = (data[i] & 32) != 0;
            this.data[i * 8 + 3] = (data[i] & 16) != 0;
            this.data[i * 8 + 4] = (data[i] & 8) != 0;
            this.data[i * 8 + 5] = (data[i] & 4) != 0;
            this.data[i * 8 + 6] = (data[i] & 2) != 0;
            this.data[i * 8 + 7] = (data[i] & 1) != 0;
        }
    }

    public static Bits join(Bits left, Bits right) {
        boolean[] joinedData = new boolean[left.data.length + right.data.length];

        System.arraycopy(left.data, 0, joinedData, 0, left.data.length);
        System.arraycopy(right.data, 0, joinedData, left.data.length, right.data.length);

        return new Bits(joinedData);
    }

    public static Bits join(Bits... parts) {
        if (parts.length < 1) {
            return null;
        }
        if (parts.length == 1) {
            return parts[0];
        }
        if (parts.length == 2) {
            return Bits.join(parts[0], parts[1]);
        }

        Bits result = Bits.join(parts[0], parts[1]);

        for (int i = 2; i < parts.length; i++) {
            result = Bits.join(result, parts[i]);
        }

        return result;
    }

    public byte getByte(int pos) {
        if (pos < 0 || pos > Math.floorDiv(this.data.length, 8)) {
            throw new ArrayIndexOutOfBoundsException(pos);
        }

        byte result = 0;

        if (this.data[pos * 8]) result |= 128;
        if (this.data[pos * 8 + 1]) result |= 64;
        if (this.data[pos * 8 + 2]) result |= 32;
        if (this.data[pos * 8 + 3]) result |= 16;
        if (this.data[pos * 8 + 4]) result |= 8;
        if (this.data[pos * 8 + 5]) result |= 4;
        if (this.data[pos * 8 + 6]) result |= 2;
        if (this.data[pos * 8 + 7]) result |= 1;

        return result;
    }

    public byte[] getByteData() {
        byte[] result = new byte[Math.floorDiv(this.data.length, 8)];

        for (int i = 0; i < result.length; i++) {
            byte b = 0;

            if (this.data[i * 8]) b += 128;
            if (this.data[i * 8 + 1]) b += 64;
            if (this.data[i * 8 + 2]) b += 32;
            if (this.data[i * 8 + 3]) b += 16;
            if (this.data[i * 8 + 4]) b += 8;
            if (this.data[i * 8 + 5]) b += 4;
            if (this.data[i * 8 + 6]) b += 2;
            if (this.data[i * 8 + 7]) b += 1;

            result[i] = b;
        }

        return result;
    }

    public Bits removeParityBits() {
        boolean[] newBits = new boolean[this.data.length * 7 / 8];

        int destOffset = 0;

        for (int i = 0; i < Math.floorDiv(this.data.length, 8); i++) {
            int baseOffset = i * 8;

            System.arraycopy(this.data, baseOffset, newBits, destOffset, 7);
            destOffset += 7;
        }

        return new Bits(newBits);
    }

    public Bits xor(Bits other) {
        boolean[] result = new boolean[this.data.length];

        for (int i = 0; i < this.data.length; i++) {
            result[i] = Boolean.logicalXor(this.data[i], other.data[i]);
        }

        return new Bits(result);
    }

    public Bits[] split(int parts) {
        if (this.data.length % parts != 0) {
            throw new IllegalArgumentException();
        }

        Bits[] result = new Bits[parts];

        int baseOffset = 0;

        for (int part = 0; part < parts; part++) {
            boolean[] bits = Arrays.copyOfRange(this.data, baseOffset, baseOffset + this.data.length / parts);
            baseOffset += this.data.length / parts;

            result[part] = new Bits(bits);
        }

        return result;
    }

    public Bits rotate(int amount) {
        amount = amount % this.data.length;

        if (amount == 0) {
            return this.clone();
        }

        if (amount < 0) {
            amount = this.data.length + amount;
        }

        boolean[] newBits = new boolean[this.data.length];

        System.arraycopy(this.data, 0, newBits, amount, this.data.length - amount);
        System.arraycopy(this.data, this.data.length - amount, newBits, 0, amount);

        return new Bits(newBits);
    }

    public Bits permutate(int[] permutationTable) {
        boolean[] result = new boolean[permutationTable.length];

        for (int i = 0; i < permutationTable.length; i++) {
            result[i] = this.data[permutationTable[i] - 1];
        }

        return new Bits(result);
    }

    public boolean getBit(int pos) {
        return this.data[pos];
    }

    public int count() {
        return this.data.length;
    }

    public Bits clone() {
        return new Bits(Arrays.copyOf(this.data, this.data.length));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");

        for (boolean bit : this.data) {
            sb.append(bit ? "1" : "0");
            sb.append(" ");
        };

        sb.append("]");

        return sb.toString();
    }
    // Metoda do konwersji HEX -> bajty
    static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}

