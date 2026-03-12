package examples.crosspackage.models

/**
 * Types defined in a different package from their provider functions.
 * Tests that @ComponentScan("examples.crosspackage.providers") discovers
 * functions even though return types live in "examples.crosspackage.models".
 */
interface ApiClient
class OkHttpApiClient : ApiClient

data class AppSettings(val debug: Boolean = false)
