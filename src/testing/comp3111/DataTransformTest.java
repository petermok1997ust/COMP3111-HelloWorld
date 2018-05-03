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
import core.comp3111.Transform;

/**
 * A sample DataColumn test case written using JUnit. It achieves 100% test
 * coverage on the DataColumn class
 * 
 * @author cspeter
 *
 */
class DataTransformTest {
	Transform t;
	@BeforeEach
	void init() {
//		t = new Transform();
	}
	
	@Test
	void testGetColName() {
		assert(t.getColName() != null);
	}
	
	@Test
	void testGetNullColName() {
		assert(t.getNumColName() != null);
	}

}
