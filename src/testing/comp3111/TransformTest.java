package testing.comp3111;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
class TransformTest {
	Transform t = null;
	
	@BeforeEach
	void init() throws DataTableException{
		DataTable dt = new DataTable();
		Number[] xvalues = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn xvaluesCol = new DataColumn(DataType.TYPE_NUMBER, xvalues);
		Number[] yvalues = new Number[] { 30, 25, 16, 8, 22 };
		DataColumn yvaluesCol = new DataColumn(DataType.TYPE_NUMBER, yvalues);
		String[] labels = new String[] { "P1", "P2", "P3", "P4", "P5" };
		DataColumn labelsCol = new DataColumn(DataType.TYPE_STRING, labels);
			dt.addCol("X", xvaluesCol);
			dt.addCol("Y", yvaluesCol);
			dt.addCol("label", labelsCol);
		t = new Transform(dt);
	}
	
	@Test
	void testTransformConstructor() throws CloneNotSupportedException{
		assertNotNull(t);
	}
	
	@Test
	void testTransformClone() throws CloneNotSupportedException{
		t.splitData(30);
		Transform d = null;
		d = t.clone();
		assertNotNull(d);
	}

	@Test
	void testTransformSplit() {
		assertTrue(t.splitData(30));
	}

	@Test
	void testTransformSplitFail() {
		ArrayList<ArrayList<String>> rowlist = new ArrayList<ArrayList<String>>();
		ArrayList<String> k = new ArrayList<String> ();
		rowlist.add(k);
		t.setRowList(rowlist);
		assertFalse(t.splitData(30));
	}
	
	@Test
	void testTransformFilter() {
		String[][] d = null;
		d = t.filterData("X", ">", 3);
		assertNotNull(d);
		d = t.filterData("X", "<", 3);
		assertNotNull(d);
		d = t.filterData("X", ">=", 3);
		assertNotNull(d);
		d = t.filterData("X", "<=", 3);
		assertNotNull(d);
		d = t.filterData("X", "==", 3);
		assertNotNull(d);
		d = t.filterData("X", "!=", 3);
		assertNotNull(d);
		d = t.filterData("X", "=", 3);
		assertNotNull(d);
	}
	
	@Test
	void testTransformGetColName() {
		String[] k = t.getColName();
		assertNotNull(k);
	}
	
	@Test
	void testTransformGetNumColName() {
		ArrayList<String> k = t.getNumColName();
		assertNotNull(k);
	}
	
	@Test
	void testTransformGetNumCol() {
		int k = t.getNumColOfFilteredList();
		assertEquals(k, 4);
	}
	
	@Test
	void testTransformGetNumRow() {
		int k = t.getNumRowOfFilteredList();
		assertEquals(k, 5);
	}
	
	@Test
	void testTransformGetRowList() {
		String[][] k = t.getRowList();
		assertNotNull(k);
	}
	
	@Test
	void testTransformGetFilteredList() {
		String[][] k = t.getFilteredList();
		assertNotNull(k);
	}

	@Test
	void testTransformGetFiltered() {
		t.splitData(30);
		ArrayList<ArrayList<String>> k = t.getFirstSplitedT();
		assertNotNull(k);
	}
	
	@Test
	void testTransformGetFilteredFail() {
		ArrayList<ArrayList<String>> k = t.getFirstSplitedT();
		assertNull(k);
	}
	
	@Test
	void testTransformGetSecondSplitedT() {
		t.splitData(30);
		ArrayList<ArrayList<String>> k = t.getSecondSplitedT();
		assertNotNull(k);
	}
	@Test
	void testTransformGetSecondSplitedTFail() {
		ArrayList<ArrayList<String>> k = t.getSecondSplitedT();
		assertNull(k);
	}

	@Test
	void testTransformToListWTitle() {
		List<String> k = t.toListwTitle();
		assertNotNull(k);
		List<String> p = new ArrayList<String>();
		p.add("X");
		p.add("Y");
		p.add("label");
		p.add("1");
		p.add("30");
		p.add("P1");
		p.add("2");
		p.add("25");
		p.add("P2");
		p.add("3");
		p.add("16");
		p.add("P3");
		p.add("4");
		p.add("8");
		p.add("P4");
		p.add("5");
		p.add("22");
		p.add("P5");
		assertTrue(k.equals(p));
	}


	
}
