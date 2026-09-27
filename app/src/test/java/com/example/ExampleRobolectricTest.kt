package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DeliveryOrder
import com.example.ui.components.DeliveryStatus
import com.example.ui.components.normalizeStatus
import com.example.util.MapUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Rider Track", appName)
  }

  @Test
  fun `test target location helper`() {
    val orderWithCoords = DeliveryOrder(
        orderId = "#10244",
        customerName = "Rahul Sharma",
        address = "Sector 62, Noida",
        latitude = 28.6273,
        longitude = 77.3719
    )
    assertEquals("28.6273,77.3719", MapUtils.getTarget(orderWithCoords))

    val orderNoCoords = DeliveryOrder(
        orderId = "#10246",
        customerName = "Amit Singh",
        address = "Andheri West, Mumbai"
    )
    assertEquals("Andheri West, Mumbai", MapUtils.getTarget(orderNoCoords))
  }

  @Test
  fun `test status normalization`() {
    assertEquals(DeliveryStatus.PENDING, normalizeStatus("Pending"))
    assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, normalizeStatus("Out for delivery"))
    assertEquals(DeliveryStatus.DELIVERED, normalizeStatus("Delivered"))
    assertEquals(DeliveryStatus.FAILED, normalizeStatus("Failed"))
    assertEquals(DeliveryStatus.FAILED, normalizeStatus("Cancelled"))
  }
}
