class MorseCodeDecoder {
	
	private static StringBuilder decodedMsg = new StringBuilder();

	public static String decode(String morseCode) {
		String[] words = morseCode.trim().split("   ");
		decodedMsg.setLength(0);

		for (var word : words) {
			System.out.println(word);
			String[] letters = word.split(" ");

			for (var letter : letters) {
				System.out.println(letter);
				decodedMsg.append(MorseCode.get(letter));
			}

			decodedMsg.append(' ');
		}

		decodedMsg.deleteCharAt(decodedMsg.length() - 1);
		return decodedMsg.toString();
    }

}