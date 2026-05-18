package my.lokalan.posq.ui.utils

fun Double.formatCurrency(): String {
    val text = this.toLong().toString()
    if (text.isEmpty()) return "Rp 0"
    val reversed = text.reversed()
    val formatted = StringBuilder()
    for (i in reversed.indices) {
        formatted.append(reversed[i])
        if ((i + 1) % 3 == 0 && i != reversed.lastIndex) {
            formatted.append(".")
        }
    }
    return "Rp " + formatted.reverse().toString()
}

fun String.formatToRupiah(): String {
    if (this.isBlank()) return "0"

    return try {
        val number = this.replace("[^\\d]".toRegex(), "").toLongOrNull() ?: 0L
        formatToRupiah(number)
    } catch (e: Exception) {
        "0"
    }
}

fun formatToRupiah(amount: Long): String {
    if (amount == 0L) return "0"

    val amountString = amount.toString()
    val reversed = amountString.reversed()
    val formatted = StringBuilder()

    for (i in reversed.indices) {
        formatted.append(reversed[i])
        if ((i + 1) % 3 == 0 && i != reversed.lastIndex) {
            formatted.append('.')
        }
    }

    return formatted.reverse().toString()
}

fun formatCurrencyInput(input: String): String {
    val digitsOnly = input.replace("[^\\d]".toRegex(), "")
    if (digitsOnly.isEmpty()) return ""

    return formatToRupiah(digitsOnly.toLongOrNull() ?: 0L)
}

fun unformatCurrency(formatted: String): String {
    return formatted.replace("[^\\d]".toRegex(), "")
}

/**
 * Format a number string with custom thousand and decimal separators
 * @param input Raw input string (digits and decimal separator)
 * @param thousandSeparator Character to use for thousand separator (default '.')
 * @param decimalSeparator Character to use for decimal separator (default ',')
 * @param decimalPlaces Number of decimal places to show (default 2), null for variable decimals
 * @return Formatted string
 */
fun formatCurrencyWithDecimals(
    input: String,
    thousandSeparator: Char = '.',
    decimalSeparator: Char = ',',
    decimalPlaces: Int? = 2
): String {
    if (input.isBlank()) return "0"

    // Split by decimal separator
    val parts = input.split(decimalSeparator, '.')
    val integerPart = parts[0].replace("[^\\d]".toRegex(), "")
    val decimalPart = if (parts.size > 1) parts[1].replace("[^\\d]".toRegex(), "") else ""

    if (integerPart.isEmpty() && decimalPart.isEmpty()) return "0"

    // Format integer part with thousand separators
    val formattedInteger = if (integerPart.isEmpty()) {
        "0"
    } else {
        val reversed = integerPart.reversed()
        val formatted = StringBuilder()
        for (i in reversed.indices) {
            formatted.append(reversed[i])
            if ((i + 1) % 3 == 0 && i != reversed.lastIndex) {
                formatted.append(thousandSeparator)
            }
        }
        formatted.reverse().toString()
    }

    // Format decimal part
    val formattedDecimal = when {
        decimalPart.isEmpty() -> ""
        decimalPlaces != null -> {
            val truncated = decimalPart.take(decimalPlaces)
            decimalSeparator + truncated.padEnd(decimalPlaces, '0')
        }
        else -> decimalSeparator + decimalPart
    }

    return formattedInteger + formattedDecimal
}

/**
 * Parse formatted currency string to raw value (integer + decimal as string)
 * @param formatted Formatted string with separators
 * @param decimalSeparator Character used for decimal separator
 * @return Raw string in format "integer.decimal" for internal use
 */
fun parseCurrencyWithDecimals(
    formatted: String,
    decimalSeparator: Char = ','
): String {
    if (formatted.isBlank()) return ""

    // Find decimal separator position
    val decimalIndex = formatted.indexOf(decimalSeparator)

    return if (decimalIndex >= 0) {
        // Has decimal part
        val integerPart = formatted.substring(0, decimalIndex).replace("[^\\d]".toRegex(), "")
        val decimalPart = formatted.substring(decimalIndex + 1).replace("[^\\d]".toRegex(), "")
        if (integerPart.isEmpty() && decimalPart.isEmpty()) {
            ""
        } else {
            "${integerPart.ifEmpty { "0" }}.$decimalPart"
        }
    } else {
        // No decimal part
        formatted.replace("[^\\d]".toRegex(), "")
    }
}
