package org.grobid.core.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class UnitBlockTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAsProduct() {
        assertThat(UnitBlock.asProduct(Arrays.asList(new UnitBlock("m", "m", "/"))), is("mm"));
    }

    @Test
    public void testAsProduct2() {
        assertThat(UnitBlock.asProduct(Arrays.asList(new UnitBlock("m", "m", "2/"), new UnitBlock("k", "m", "2"))), is("mm^2·km^2"));
    }

    @Test
    public void testAsString1() {
        assertThat(UnitBlock.asString(Arrays.asList(new UnitBlock("m", "m", "2"), new UnitBlock("k", "m", "2"))), is("mm^2/km^2"));
    }
}