package my.lokalan.posq.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import my.lokalan.posq.ui.utils.formatToRupiah
import my.lokalan.posq.ui.utils.formatCurrencyWithDecimals

@Composable
fun DecimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Amount",
    placeholder: String = "0",
    prefix: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    thousandSeparator: Char = '.',
    decimalSeparator: Char = ',',
    decimalPlaces: Int? = 2,
    allowDecimals: Boolean = false
) {
    var amountDisplay by remember { mutableStateOf(TextFieldValue("")) }

    // Update display when external value changes
    LaunchedEffect(value, thousandSeparator, decimalSeparator, allowDecimals) {
        val formatted = if (value.isNotEmpty()) {
            if (allowDecimals) {
                formatCurrencyWithDecimals(
                    value,
                    thousandSeparator = thousandSeparator,
                    decimalSeparator = decimalSeparator,
                    decimalPlaces = decimalPlaces
                )
            } else {
                value.formatToRupiah()
            }
        } else {
            ""
        }
        if (amountDisplay.text != formatted) {
            amountDisplay = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        }
    }

    OutlinedTextField(
        value = amountDisplay,
        onValueChange = { newValue ->
            val newText = newValue.text
            val newCursorPos = newValue.selection.start

            if (allowDecimals) {
                // Handle decimal input
                // Allow digits and one decimal separator
                val cleanText = newText.filter { it.isDigit() || it == decimalSeparator || it == '.' || it == ',' }

                // Normalize decimal separator (both . and , should work)
                val normalizedText = cleanText.replace(
                    if (decimalSeparator == ',') '.' else ',',
                    decimalSeparator
                )

                // Ensure only one decimal separator
                val decimalCount = normalizedText.count { it == decimalSeparator }
                val processedText = if (decimalCount > 1) {
                    val firstDecimalIndex = normalizedText.indexOf(decimalSeparator)
                    normalizedText.filterIndexed { index, c ->
                        c != decimalSeparator || index == firstDecimalIndex
                    }
                } else {
                    normalizedText
                }

                // Split into parts
                val parts = processedText.split(decimalSeparator)
                val integerPart = parts[0].replace("[^\\d]".toRegex(), "")
                val decimalPart = if (parts.size > 1) {
                    val rawDecimal = parts[1].replace("[^\\d]".toRegex(), "")
                    // Limit decimal places if specified
                    if (decimalPlaces != null) rawDecimal.take(decimalPlaces) else rawDecimal
                } else ""

                // Build raw value for parent (using . as internal separator)
                val rawValue = if (processedText.contains(decimalSeparator)) {
                    if (integerPart.isEmpty() && decimalPart.isEmpty()) {
                        ""
                    } else {
                        "${integerPart.ifEmpty { "0" }}.$decimalPart"
                    }
                } else {
                    integerPart
                }

                // Notify parent
                onValueChange(rawValue)

                if (rawValue.isNotEmpty()) {
                    // Format with separators
                    val formatted = formatCurrencyWithDecimals(
                        rawValue,
                        thousandSeparator = thousandSeparator,
                        decimalSeparator = decimalSeparator,
                        decimalPlaces = decimalPlaces
                    )

                    // Calculate cursor position
                    val digitsBeforeCursor = newText.take(newCursorPos).count { it.isDigit() }
                    val hadDecimalBeforeCursor = newText.take(newCursorPos).any { it == decimalSeparator || it == '.' || it == ',' }

                    var targetPos = 0
                    var digitCount = 0
                    var seenDecimal = false

                    for (i in formatted.indices) {
                        if (formatted[i] == decimalSeparator) {
                            seenDecimal = true
                            if (hadDecimalBeforeCursor) {
                                targetPos = i + 1
                            }
                        }

                        if (digitCount >= digitsBeforeCursor && (!hadDecimalBeforeCursor || seenDecimal)) {
                            break
                        }

                        targetPos = i + 1
                        if (formatted[i].isDigit()) {
                            digitCount++
                        }
                    }

                    targetPos = targetPos.coerceIn(0, formatted.length)

                    amountDisplay = TextFieldValue(
                        text = formatted,
                        selection = TextRange(targetPos)
                    )
                } else {
                    amountDisplay = TextFieldValue("")
                }
            } else {
                // Original integer-only logic
                // Extract only digits
                val newDigits = newText.replace("[^\\d]".toRegex(), "")

                // Notify parent with raw digits
                onValueChange(newDigits)

                if (newDigits.isNotEmpty()) {
                    // Format to Rupiah
                    val formatted = newDigits.formatToRupiah()

                    // Calculate cursor position based on digits before cursor
                    val digitsBeforeCursor = newText.take(newCursorPos).count { it.isDigit() }

                    var targetPos = 0
                    var digitCount = 0

                    for (i in formatted.indices) {
                        if (digitCount >= digitsBeforeCursor) {
                            break
                        }
                        targetPos = i + 1
                        if (formatted[i].isDigit()) {
                            digitCount++
                        }
                    }

                    targetPos = targetPos.coerceIn(0, formatted.length)

                    amountDisplay = TextFieldValue(
                        text = formatted,
                        selection = TextRange(targetPos)
                    )
                } else {
                    amountDisplay = TextFieldValue("")
                }
            }
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        prefix = { Text(prefix) },
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        supportingText = supportingText,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (allowDecimals) KeyboardType.Decimal else KeyboardType.Number
        ),
        singleLine = true
    )
}

/**
 * Alternative: Stateful version that manages its own state
 * Useful when you want the component to handle everything internally
 */
@Composable
fun rememberCurrencyTextFieldState(
    initialValue: String = ""
): CurrencyTextFieldState {
    return remember { CurrencyTextFieldState(initialValue) }
}

class CurrencyTextFieldState(initialValue: String = "") {
    var value by mutableStateOf(initialValue)
        private set

    fun update(newValue: String) {
        value = newValue
    }

    fun clear() {
        value = ""
    }

    fun isEmpty(): Boolean = value.isBlank()

    fun getLong(): Long? = value.replace("[^\\d]".toRegex(), "").toLongOrNull()

    fun getDouble(): Double? {
        return try {
            // Handle format like "123.45" or "123"
            value.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Stateful version of CurrencyTextField
 *
 * Example usage:
 *
 * For Rupiah (IDR) - uses . as thousand separator, , as decimal separator:
 * ```
 * val state = rememberCurrencyTextFieldState()
 * CurrencyTextField(
 *     state = state,
 *     prefix = "Rp ",
 *     thousandSeparator = '.',
 *     decimalSeparator = ',',
 *     allowDecimals = true,
 *     decimalPlaces = 2
 * )
 * // Displays: Rp 1.234.567,89
 * ```
 *
 * For USD - uses , as thousand separator, . as decimal separator:
 * ```
 * val state = rememberCurrencyTextFieldState()
 * CurrencyTextField(
 *     state = state,
 *     prefix = "$ ",
 *     thousandSeparator = ',',
 *     decimalSeparator = '.',
 *     allowDecimals = true,
 *     decimalPlaces = 2
 * )
 * // Displays: $ 1,234,567.89
 * ```
 */
@Composable
fun CurrencyTextField(
    state: CurrencyTextFieldState,
    modifier: Modifier = Modifier,
    label: String = "Amount",
    placeholder: String = "0",
    prefix: String = "IDR ",
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    thousandSeparator: Char = '.',
    decimalSeparator: Char = ',',
    decimalPlaces: Int? = 2,
    allowDecimals: Boolean = false
) {
    DecimalTextField(
        value = state.value,
        onValueChange = { state.update(it) },
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        prefix = prefix,
        enabled = enabled,
        isError = isError,
        supportingText = supportingText,
        thousandSeparator = thousandSeparator,
        decimalSeparator = decimalSeparator,
        decimalPlaces = decimalPlaces,
        allowDecimals = allowDecimals
    )
}
