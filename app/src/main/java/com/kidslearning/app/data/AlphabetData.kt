package com.kidslearning.app.data

data class AlphabetData(
    val letter: String,
    val pronunciation: String,
    val example: String,
    val exampleEmoji: String,
    val soundFileName: String
)

object AlphabetDataProvider {
    val arabicAlphabet = listOf(
        AlphabetData("ا", "Alif", "أسد", "🦁", "alif.mp3"),
        AlphabetData("ب", "Ba", "بطة", "🦆", "ba.mp3"),
        AlphabetData("ت", "Ta", "تفاحة", "🍎", "ta.mp3"),
        AlphabetData("ث", "Tha", "ثعلب", "🦊", "tha.mp3"),
        AlphabetData("ج", "Jeem", "جمل", "🐫", "jeem.mp3"),
        AlphabetData("ح", "Ha", "حصان", "🐴", "ha.mp3"),
        AlphabetData("خ", "Kha", "خروف", "🐑", "kha.mp3"),
        AlphabetData("د", "Dal", "دب", "🐻", "dal.mp3"),
        AlphabetData("ذ", "Thal", "ذئب", "🐺", "thal.mp3"),
        AlphabetData("ر", "Ra", "رمان", "🍊", "ra.mp3"),
        AlphabetData("ز", "Zay", "زرافة", "🦒", "zay.mp3"),
        AlphabetData("س", "Seen", "سمكة", "🐟", "seen.mp3"),
        AlphabetData("ش", "Sheen", "شمس", "☀️", "sheen.mp3"),
        AlphabetData("ص", "Sad", "صقر", "🦅", "sad.mp3"),
        AlphabetData("ض", "Dad", "ضفدع", "🐸", "dad.mp3"),
        AlphabetData("ط", "Ta", "طائر", "🐦", "ta2.mp3"),
        AlphabetData("ظ", "Dha", "ظبي", "🦌", "dha.mp3"),
        AlphabetData("ع", "Ayn", "عنب", "🍇", "ayn.mp3"),
        AlphabetData("غ", "Ghayn", "غزال", "🦌", "ghayn.mp3"),
        AlphabetData("ف", "Fa", "فيل", "🐘", "fa.mp3"),
        AlphabetData("ق", "Qaf", "قرد", "🐵", "qaf.mp3"),
        AlphabetData("ك", "Kaf", "كلب", "🐕", "kaf.mp3"),
        AlphabetData("ل", "Lam", "ليمون", "🍋", "lam.mp3"),
        AlphabetData("م", "Meem", "موز", "🍌", "meem.mp3"),
        AlphabetData("ن", "Noon", "نحلة", "🐝", "noon.mp3"),
        AlphabetData("ه", "Ha", "هدهد", "🦜", "ha2.mp3"),
        AlphabetData("و", "Waw", "وردة", "🌹", "waw.mp3"),
        AlphabetData("ي", "Ya", "يد", "✋", "ya.mp3")
    )

    val frenchAlphabet = ('A'..'Z').mapIndexed { index, letter ->
        val examples = mapOf(
            'A' to Pair("Avion", "✈️"),
            'B' to Pair("Ballon", "⚽"),
            'C' to Pair("Chat", "🐱"),
            'D' to Pair("Dauphin", "🐬"),
            'E' to Pair("Éléphant", "🐘"),
            'F' to Pair("Fleur", "🌸"),
            'G' to Pair("Gâteau", "🎂"),
            'H' to Pair("Hibou", "🦉"),
            'I' to Pair("Île", "🏝️"),
            'J' to Pair("Jardin", "🌳"),
            'K' to Pair("Kiwi", "🥝"),
            'L' to Pair("Lion", "🦁"),
            'M' to Pair("Maison", "🏠"),
            'N' to Pair("Nuage", "☁️"),
            'O' to Pair("Oiseau", "🐦"),
            'P' to Pair("Papillon", "🦋"),
            'Q' to Pair("Queue", "🎯"),
            'R' to Pair("Rose", "🌹"),
            'S' to Pair("Soleil", "☀️"),
            'T' to Pair("Tortue", "🐢"),
            'U' to Pair("Usine", "🏭"),
            'V' to Pair("Voiture", "🚗"),
            'W' to Pair("Wagon", "🚂"),
            'X' to Pair("Xylophone", "🎹"),
            'Y' to Pair("Yeux", "👁️"),
            'Z' to Pair("Zèbre", "🦓")
        )

        val example = examples[letter] ?: Pair("", "")
        AlphabetData(
            letter = letter.toString(),
            pronunciation = letter.toString(),
            example = example.first,
            exampleEmoji = example.second,
            soundFileName = "${letter.lowercase()}.mp3"
        )
    }
}