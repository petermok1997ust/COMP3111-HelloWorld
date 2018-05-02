package testing.comp3111;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataManagement;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;

/**
 * A sample DataColumn test case written using JUnit. It achieves 100% test
 * coverage on the DataColumn class
 * 
 * @author cspeter
 *
 */
class DataTypeTest {
	
	@Test
	void testGetObj() {
		assertEquals(DataType.getObjStr(), DataType.TYPE_OBJECT);
	}

	@Test
	void testGetStr() {
		assertEquals(DataType.getStrStr(), DataType.TYPE_STRING);
	}
	
	@Test
	void testGetNum() {
		assertEquals(DataType.getNumStr(), DataType.TYPE_NUMBER);
	}
	
	@Test
	void testConstructor() {
		DataType d = new DataType();
		assertEquals(d.getNumberStr(), DataType.TYPE_NUMBER);
	}
}
