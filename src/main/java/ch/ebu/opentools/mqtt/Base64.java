package ch.ebu.opentools.mqtt;

public final class Base64 {
    private static final byte BYTE_START_UPPERCASE = 65;
    private static final byte BYTE_END_UPPERCASE = 90;
    private static final byte BYTE_START_LOWERCASE = 97;
    private static final byte BYTE_END_LOWERCASE = 122;
    private static final byte BYTE_START_NUMBER = 48;
    private static final byte BYTE_END_NUMBER = 57;
    private static final byte BYTE_PLUS = 43;
    private static final byte BYTE_SLASH = 47;
    private static final int BASE64_END_UPPERCASE = 26;
    private static final int BASE64_END_LOWERCASE = 52;
    private static final int BASE64_END_NUMBER = 62;
    private static final int BASE64_PLUS = 62;
    private static final int BASE64_SLASH = 63;
    private static final byte BASE64_PAD = 61;
    private static final int HALF_NIBBLE = 2;
    private static final int ONE_NIBBLE = 4;
    private static final int ONE_AND_HALF_NIBBLE = 6;
    private static final int ONE_BYTE = 8;
    private static final int TWO_BYTES = 16;
    private static final int THREE_BYTES = 24;
    private static final int ISOLATE_BYTE = 255;
    private static final int ISOLATE_BASE64 = 63;
    private static final int ISOLATE_LSB_BASE64 = 15;
    private static final int ISOLATE_MSB_BASE64 = 3;
    private static final int BYTE_GROUP_SIZE = 3;
    private static final int BASE64_GROUP_SIZE = 4;
    private static final int[] BASE64D16_CONVERSION_TABLE = new int[]{1296647489, 1666798929, 1936681831, 942944375};
    private static final int BASE64D8_CONVERSION_TABLE = 2003259713;

    public Base64() {
    }

    private static byte extractBase64FromInteger(int integerValue, int bytePosition) {
        return (byte)(integerValue >> (bytePosition << 3) & 255);
    }

    private static byte base64ToByte(byte base64Value) {
        if (base64Value < 26) {
            return (byte)(65 + base64Value);
        } else if (base64Value < 52) {
            return (byte)(97 + (base64Value - 26));
        } else if (base64Value < 62) {
            return (byte)(48 + (base64Value - 52));
        } else {
            return (byte)(base64Value == 62 ? 43 : 47);
        }
    }

    private static byte base64d16ToByte(byte base64d16Value) {
        return extractBase64FromInteger(BASE64D16_CONVERSION_TABLE[base64d16Value >> 2], base64d16Value & 3);
    }

    private static byte base64d8ToByte(byte base64d8Value) {
        return extractBase64FromInteger(2003259713, base64d8Value);
    }

    private static byte byteToBase64(byte byteValue) throws IllegalArgumentException {
        if (byteValue >= 65 && byteValue <= 90) {
            return (byte)(byteValue - 65);
        } else if (byteValue >= 97 && byteValue <= 122) {
            return (byte)(26 + (byteValue - 97));
        } else if (byteValue >= 48 && byteValue <= 57) {
            return (byte)(52 + (byteValue - 48));
        } else if (byteValue == 43) {
            return 62;
        } else if (byteValue == 47) {
            return 63;
        } else {
            throw new IllegalArgumentException("provided byte value out of base64 range");
        }
    }

    private static int numberOfValidBase64BytesWithoutPad(byte[] bytesToEncode) throws IllegalArgumentException {
        int validLength = bytesToEncode.length;
        if (bytesToEncode[validLength - 1] == 61) {
            --validLength;
        }

        if (bytesToEncode[validLength - 1] == 61) {
            --validLength;
        }

        return validLength;
    }

    private static int base64EstimatedLength(byte[] base64sToDecode) {
        if (base64sToDecode.length == 0) {
            return 0;
        } else {
            int estimatedLength = base64sToDecode.length / 4 * 3;
            if (base64sToDecode[base64sToDecode.length - 1] == 61) {
                if (base64sToDecode[base64sToDecode.length - 2] == 61) {
                    --estimatedLength;
                }

                --estimatedLength;
            }

            return estimatedLength;
        }
    }

    public static byte[] decodeBase64Local(byte[] base64Values) throws IllegalArgumentException {
        if (base64Values == null) {
            throw new IllegalArgumentException("null or empty base64Values");
        } else if (base64Values.length == 0) {
            return new byte[0];
        } else if (base64Values.length % 4 != 0) {
            throw new IllegalArgumentException("invalid base64Values length");
        } else {
            int numberOfEncodedBytes = numberOfValidBase64BytesWithoutPad(base64Values);
            int indexOfFirstEncodedByte = 0;
            int decodedIndex = 0;

            byte[] decodedResult;
            byte c1;
            byte c2;
            byte c3;
            for(decodedResult = new byte[base64EstimatedLength(base64Values)]; numberOfEncodedBytes >= 4; numberOfEncodedBytes -= 4) {
                c1 = byteToBase64(base64Values[indexOfFirstEncodedByte++]);
                c2 = byteToBase64(base64Values[indexOfFirstEncodedByte++]);
                c3 = byteToBase64(base64Values[indexOfFirstEncodedByte++]);
                byte c4 = byteToBase64(base64Values[indexOfFirstEncodedByte++]);
                decodedResult[decodedIndex++] = (byte)(c1 << 2 | c2 >> 4);
                decodedResult[decodedIndex++] = (byte)(c2 << 4 | c3 >> 2);
                decodedResult[decodedIndex++] = (byte)(c3 << 6 | c4);
            }

            if (numberOfEncodedBytes == 3) {
                c1 = byteToBase64(base64Values[indexOfFirstEncodedByte++]);
                c2 = byteToBase64(base64Values[indexOfFirstEncodedByte++]);
                c3 = byteToBase64(base64Values[indexOfFirstEncodedByte]);
                decodedResult[decodedIndex++] = (byte)(c1 << 2 | c2 >> 4);
                decodedResult[decodedIndex] = (byte)(c2 << 4 | c3 >> 2);
            }

            if (numberOfEncodedBytes == 2) {
                c1 = byteToBase64(base64Values[indexOfFirstEncodedByte++]);
                c2 = byteToBase64(base64Values[indexOfFirstEncodedByte]);
                decodedResult[decodedIndex] = (byte)(c1 << 2 | c2 >> 4);
            }

            return decodedResult;
        }
    }

    public static byte[] encodeBase64Local(byte[] dataValues) throws IllegalArgumentException {
        if (dataValues == null) {
            throw new IllegalArgumentException("null or empty dataValues");
        } else {
            return dataValues.length == 0 ? new byte[0] : encodeBase64Internal(dataValues);
        }
    }

    public static String encodeBase64StringLocal(byte[] dataValues) throws IllegalArgumentException {
        if (dataValues == null) {
            throw new IllegalArgumentException("null or empty dataValues");
        } else {
            return dataValues.length == 0 ? new String() : new String(encodeBase64Internal(dataValues));
        }
    }

    private static byte[] encodeBase64Internal(byte[] dataValues) throws IllegalArgumentException {
        int encodedLength = ((dataValues.length - 1) / 3 + 1) * 4;
        int destinationPosition = 0;
        int currentPosition = 0;

        byte[] encodedResult;
        for(encodedResult = new byte[encodedLength]; dataValues.length - currentPosition >= 3; currentPosition += 3) {
            encodedResult[destinationPosition++] = base64ToByte((byte)(dataValues[currentPosition] >> 2 & 63));
            encodedResult[destinationPosition++] = base64ToByte((byte)(dataValues[currentPosition] << 4 & 63 | dataValues[currentPosition + 1] >> 4 & 15));
            encodedResult[destinationPosition++] = base64ToByte((byte)(dataValues[currentPosition + 1] << 2 & 63 | dataValues[currentPosition + 2] >> 6 & 3));
            encodedResult[destinationPosition++] = base64ToByte((byte)(dataValues[currentPosition + 2] & 63));
        }

        if (dataValues.length - currentPosition == 2) {
            encodedResult[destinationPosition++] = base64ToByte((byte)(dataValues[currentPosition] >> 2 & 63));
            encodedResult[destinationPosition++] = base64ToByte((byte)(dataValues[currentPosition] << 4 & 63 | dataValues[currentPosition + 1] >> 4 & 15));
            encodedResult[destinationPosition++] = base64d16ToByte((byte)(dataValues[currentPosition + 1] & 15));
            encodedResult[destinationPosition] = 61;
        }

        if (dataValues.length - currentPosition == 1) {
            encodedResult[destinationPosition++] = base64ToByte((byte)(dataValues[currentPosition] >> 2 & 63));
            encodedResult[destinationPosition++] = base64d8ToByte((byte)(dataValues[currentPosition] & 3));
            encodedResult[destinationPosition++] = 61;
            encodedResult[destinationPosition] = 61;
        }

        return encodedResult;
    }
}
