import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SimpleTest {
    @Test
    fun `this is a simple test to see if this allows for complex names`() {
        assertTrue(true)
    }

    @Test
    fun `this feature should throw a NullPointerException when something is null`() {
        assertFalse(false)
    }
}