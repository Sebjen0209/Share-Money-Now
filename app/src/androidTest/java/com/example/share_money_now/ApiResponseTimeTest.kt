import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Response
import okhttp3.Call

@RunWith(AndroidJUnit4::class)
class ApiResponseTimeTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testApiResponseTime() {

        val apiUrl = "https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app"

        // Create an OkHttp client with a logging interceptor to monitor request/response
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        // Get the current time before making the API call
        val startTime = System.currentTimeMillis()

        // Execute the API call asynchronously
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                // Get the current time after receiving the API response
                val endTime = System.currentTimeMillis()

                // Calculate the response time in milliseconds
                val responseTime = endTime - startTime

                // Log or assert the response time as per your requirement
                println("API Response Time: $responseTime ms")

                // You can assert the response time to ensure it's within acceptable limits
                // For example: assertTrue(responseTime < MAX_RESPONSE_TIME)
                val maxResponseTime = 1000

                assert(responseTime<maxResponseTime)
            }
        })

        // You may need to add a delay here to allow the API call to complete before finishing the test
        // Thread.sleep(5000) // Adjust delay as needed
    }
}
