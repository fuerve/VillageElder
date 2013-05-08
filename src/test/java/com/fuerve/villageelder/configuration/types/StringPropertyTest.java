/**
 * 
 */
package com.fuerve.villageelder.configuration.types;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author lparker
 *
 */
public class StringPropertyTest {

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.types.StringProperty#StringProperty()}.
    */
   @Test
   public final void testStringProperty() {
      TypedProperty<?> target = new StringProperty();
      assertEquals(null, target.getValue());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.types.StringProperty#StringProperty(java.lang.String)}.
    */
   @Test
   public final void testStringPropertyString() {
      TypedProperty<?> target = new StringProperty("default!");
      assertEquals("default!", target.getValue());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.types.StringProperty#parse(java.lang.String)}.
    */
   @Test
   public final void testParseString() {
      TypedProperty<?> target = new StringProperty();
      target.doParse("test");
      assertEquals("test", target.getValue());
   }

}
