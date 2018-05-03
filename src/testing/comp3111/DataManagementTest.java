package testing.comp3111;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataManagement;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;
import ui.comp3111.Main;

/**
 * A sample DataColumn test case written using JUnit. It achieves 100% test
 * coverage on the DataColumn class
 * 
 * @author cspeter
 *
 */
class DataManagementTest {
	DataManagement dc;
	File file = null;
	DataTable d = null;
	
	@BeforeEach
	void init() throws IOException {
		d = new DataTable();
		dc = DataManagement.getInstance(); 	
		file = File.createTempFile("temp-file-name", ".comp3111");
	}
	
	@Test
	void testDataManagementTestConstructor() {
//		assert (dc.getChartArray() != null);
		assert (dc.getTableName() != null);
		assert (dc.getDataTables() != null);
		assert (dc.getNumTable() == dc.getDataTables().size());
//		assert (dc.getNumChart() == dc.getChartArray().size());
	}
	
	@Test
	void testDataManagementImportNullCsv() throws IOException, DataTableException{
		//null file
		System.out.println("Null file");
		assertEquals(dc.importCSV(null), null);
	}
	
	@Test
	void testDataManagementImportNotFoundCsv() throws IOException, DataTableException{
		//null file
		System.out.println("Null file");
		assertEquals(dc.importCSV(null), null);
	}
	
	@Test
	void testDataManagementImportEmptyCsv() throws IOException, DataTableException{
		//null file
		assert(dc.importCSV(new File("ndjiwqni"+file.getAbsolutePath()))==null);
	}
	@Test
	void testDataManagementImportNullFileCsv() throws IOException, DataTableException{
		//null file
		assert(dc.importCSV(null) == null);
	}
	

	
	@Test
	void testDataManagementImportOnlyHeaderCsv() throws IOException, DataTableException{
		//only header
				String text = "dnqwij, ewqewq, 123, dqw";
				System.out.println("file only with header");
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(text);
				fileWriter.close();
				assert(dc.importCSV(file) != null);
	}
	
	@Test
	void testDataManagementImportOneRowCsv() throws IOException, DataTableException{
		//only header
				String text = "dnqwij, ewqewq, 123, dqw";
				System.out.println("file only with header");
				FileWriter fileWriter = new FileWriter(file);
				text += " \n dnqwij, ewqewq, 2, dqw";
				fileWriter.write(text);
				fileWriter.close();
				assert(dc.importCSV(file) != null);
	}
	
	@Test
	void testDataManagementImportMoreRowsCsv() throws IOException, DataTableException{
		//only header
				String text = "dnqwij, ewqewq, 123, dqw";
				FileWriter fileWriter = new FileWriter(file);
				for(int i = 0; i < Math.round(Math.random()*100);i++) {
					text += " \n dnqwij, ewqewq, 2, dqw";
				}
				fileWriter.write(text);
				fileWriter.close();
				assert(dc.importCSV(file) != null);
	}
	
	@Test
	void testDataManagementImportEmptyStringCsv() throws IOException, DataTableException{
		//only header
				String text = "dnqwij, ewqewq, 123, dqw";
				text += " \n dnqwij,,59, dqw";
				FileWriter fileWriter = new FileWriter(file);
				text += " \n dnqwij, ewqewq, 2, dqw";
				fileWriter.write(text);
				fileWriter.close();
				assert(dc.importCSV(file) != null);
	}

	@Test
	void testDataManagementImportNullNumCsv() throws IOException, DataTableException{
		System.out.println("null number");
				String text = "dnqwij, ewqewq, 123, dqw \n dnqwij,dsa,59, dqw \n dnqwij,das,, dqw \n dnqwij, ewqewq,, dqw";
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(text);
				fileWriter.close();
				assert(dc.importCSV(file)!=null);
	}
	
	@Test
	void testDataManagementTestExportEmptyCSV() throws DataTableException {
		//empty
		assert(dc.exportTableToCSV(d, file) == true);
	}
	@Test
	void testDataManagementTestExportOnlyHeadersCSV() throws DataTableException {
		//empty
		DataTable d = new DataTable();
		d.addCol("header", new DataColumn(DataType.TYPE_OBJECT, null));
		assert(dc.exportTableToCSV(d, file) == true);
	}

	@Test
	void testDataManagementTestExportOneRowCSV() throws DataTableException {
		//one column
		Number[] xvalues = new Integer[] { 1 };
		d.addCol("col1", new DataColumn(DataType.TYPE_NUMBER, xvalues));
		d.addCol("col2", new DataColumn(DataType.TYPE_NUMBER, xvalues));
		assert(dc.exportTableToCSV(d, file) == true);
	}
	@Test
	void testDataManagementTestExportRowsCSV() throws DataTableException {
		//one column
		Number[] xvalues = new Integer[] { 1,2,3,4, 5 };
		String[] labels = new String[] { "P1", "P2", "P3", "P4", "P5" };
		d.addCol("col1", new DataColumn(DataType.TYPE_NUMBER, xvalues));
		d.addCol("col2", new DataColumn(DataType.TYPE_STRING, labels));
		assert(dc.exportTableToCSV(d, file) == true);
	}
	
	@Test
	void testDataManagementExportNullFileCsv() throws IOException{
		//null file
		assert(dc.exportTableToCSV(d, null) == false);
	}
	
//	@Test
//	void testDataManagementTestExportNull() throws DataTableException {
//		//one column
//		assert(dc.exportTableToCSV(d, null) == false);
//	}
	
	
	@Test
	void testDataManagementTestSaveProject() {
		assert(dc.saveProject(file)!=null);
	}
	
	@Test
	void testDataManagementTestLoadProject() {
		assert(dc.loadProject(dc.saveProject(file)) != null);
	}
	
	@Test
	void testDataManagementTestLoadNullProject() {
		//exception
		assertEquals(null, dc.loadProject(null));

	}
//	@Test
//	void testDataManagementTestSaveNullProject() {
//		//exception
//		assertThrows(NullPointerException.class, ()->dc.saveProject(null));
//
//	}
	
	@Test
	void testDataManagementTestSaveNullProject() {
		//exception
		assertEquals(dc.saveProject(null), null);

	}
	
	@Test
	void testDataManagementGetIdx() throws DataTableException{
		dc.importCSV(file);
		assert(dc.getDataTableByIndex(0) != null);
	}
	

}
