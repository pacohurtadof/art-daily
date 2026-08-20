package com.artdaily.core.normalize

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CenturyCalculatorTest {

    @Test
    fun `year 1667 is 17th century`() {
        assertEquals(17, CenturyCalculator.fromYear(1667))
    }

    @Test
    fun `year 1700 (century boundary) is 17th century`() {
        assertEquals(17, CenturyCalculator.fromYear(1700))
    }

    @Test
    fun `year 1701 is 18th century`() {
        assertEquals(18, CenturyCalculator.fromYear(1701))
    }

    @Test
    fun `year 1 is 1st century`() {
        assertEquals(1, CenturyCalculator.fromYear(1))
    }

    @Test
    fun `BCE years return a negative century`() {
        // ca. 2300 BCE (Etapa 2, "Head of a ruler") -> siglo 23 a.C.
        assertEquals(-23, CenturyCalculator.fromYear(-2300))
    }

    @Test
    fun `year 0 is not a valid calendar year, returns null`() {
        assertNull(CenturyCalculator.fromYear(0))
    }

    @Test
    fun `null year returns null`() {
        assertNull(CenturyCalculator.fromYear(null))
    }
}
