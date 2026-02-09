// FILE: test.kt
import org.koin.core.annotation.Monitor

// This function is monitored but won't be called directly
// because SDK is not initialized in tests
@Monitor
fun monitoredFunction(): String {
    return "monitored"
}

// Regular function to verify compilation works
fun regularFunction(): String {
    return "regular"
}

fun box(): String {
    // Only call the non-monitored function to verify compilation works
    // The @Monitor annotation transformation is verified via IR golden files
    val regular = regularFunction()
    return if (regular == "regular") "OK" else "FAIL: regular=$regular"
}
