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
class DataTableTest {
	DataTable table = null;
	
	@BeforeEach
	void init() {
		table = new DataTable();
	}
	
	@Test
	void testCoverageEmptyDataTableConstructor() {
		assert(table.getNumCol()==0);
		assert(table.getNumRow()==0);
	}
	
	@Test
	void testGetNumNonEmpty() throws DataTableException {
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		table.addCol("col_1", xvaluesCol);
		assertEquals(table.getNumCol(), 1);
	}
	
	@Test
	void testGetNumNonExist() throws DataTableException {
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		table.addCol("col 1", xvaluesCol);
		DataColumn col = table.getCol("col 2");
		assertEquals(col, null);
	}
	
	@Test
	void testAddExistCol() throws DataTableException {
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		table.addCol("col 1", xvaluesCol);
		assertThrows(DataTableException.class, ()->table.addCol("col 1", xvaluesCol));
	}
	
	@Test
	void testAddNotSameRow() throws DataTableException {
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		table.addCol("col 1", xvaluesCol);
		assertThrows(DataTableException.class, ()->table.addCol("col 2", new DataColumn(DataType.TYPE_NUMBER, new Integer[] { 1})));
	}
	
	@Test
	void testAddSameRow() throws DataTableException {
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		table.addCol("col 1", xvaluesCol);
		table.addCol("col 2", xvaluesCol);
		assert(table.getNumCol()==2);
		assert(table.getNumRow()==5);
	}
	
	@Test
	void testGetCol() throws DataTableException {
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		table.addCol("col 1", xvaluesCol);
		assert(table.getCol("col 1").getSize()== 5);
	}

	@Test
	void testGetKey() throws DataTableException {
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		table.addCol("col 1", xvaluesCol);
		assert(table.getKeys().contains("col 1"));
	}

	
	@Test
	void testRemoveCol() throws DataTableException {
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		table.addCol("col 1", xvaluesCol);
		table.removeCol("col 1");
		assertEquals(table.getNumCol(), 0);
	}
	
	@Test
	void testRemoveNonExistCol() throws DataTableException {
		assertThrows(DataTableException.class, ()->table.removeCol("col 1"));
	}
}
