// FILE: test.kt
import org.koin.core.annotation.Monitor

// This class is monitored but its functions won't be called
// because SDK is not initialized in tests
@Monitor
class MonitoredService {
    fun getValue(): Int = 42
    fun getMessage(): String = "hello"
    private fun privateMethod(): Int = 0  // Should NOT be monitored (private)
}

// Regular class to verify compilation works
class RegularService {
    fun getValue(): Int = 100
}

fun box(): String {
    // Only call the non-monitored class to verify compilation works
    // The @Monitor annotation transformation is verified via IR golden files
    val regular = RegularService()
    val v = regular.getValue()
    return if (v == 100) "OK" else "FAIL: v=$v"
}
