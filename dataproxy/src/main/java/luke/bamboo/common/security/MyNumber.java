package luke.bamboo.common.security;

public class MyNumber {

	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static char[] BinaryToHex(byte[] binary) {
		char[] hex = new char[binary.length * 2];
		for (int i = 0; i < binary.length; i++) {
			hex[i * 2] = hexDigits[(binary[i] >> 4 & 0x0F)];
			hex[i * 2 + 1] = hexDigits[(binary[i] & 0x0F)];
		}
		return hex;
	}

	private static final char O = '0';
	private static final char A = 'A';

	public static byte[] HexToBinary(char[] hex) {
		byte[] binary = new byte[hex.length / 2];

		for (int i = 0; i < hex.length; i++) {

			char n1 = hex[i];
			if (i % 2 == 0) {
				binary[i / 2] = (byte) (n1 >= A ? n1 - A + 10 : n1 - O);
			} else {
				binary[i / 2] = (byte) ((binary[i / 2] << 4) | (n1 >= A ? n1 - A + 10 : n1 - O));
			}
		}
		return binary;
	}
}
