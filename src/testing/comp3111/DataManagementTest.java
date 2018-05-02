package testing.comp3111;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataManagement;
import core.comp3111.DataType;

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
	@BeforeEach
	void init() throws IOException {
		dc = DataManagement.getInstance(); 	
		
		
	}
	
	@Test
	void testDataManagementTestConstructor() {
		assert (dc.getChartArray() != null);
		assert (dc.getTableName() != null);
		assert (dc.getDataTables() != null);
		assert (dc.getNumTable() == dc.getDataTables().size());
		assert (dc.getNumChart() == dc.getChartArray().size());
	}
	
/*
	@Test
	void testDataManagementImportCsv() throws IOException {
		//null file
		System.out.println("Null file");
		dc.importCSV(file);
		//empty file
		System.out.println("Empty file");
		file = File.createTempFile("temp-file-name", ".csv");
//		dc.importCSV(file);
		//only header
		String text = "dnqwij, ewqewq, 123, dqw";
		System.out.println("file only with header");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(text);
		fileWriter.close();
		dc.importCSV(file);
//		// 1 row
		System.out.println("file with one row");
		fileWriter = new FileWriter(file);
		text += " \n dnqwij, ewqewq, 2, dqw";
		fileWriter.write(text);
		fileWriter.close();
		dc.importCSV(file);
		//with more than 1 rows(Math.round(Math.random()*100))
		System.out.println("file with more than one rows");
		fileWriter = new FileWriter(file);
		
		for(int i = 0; i < Math.round(Math.random()*100);i++) {
			text += " \n dnqwij, ewqewq, 2, dqw";
		}
		fileWriter.append(text);
		fileWriter.close();
		dc.importCSV(file);
		//empty string
		System.out.println("file with empty string");
		fileWriter = new FileWriter(file);
		text += " \n dnqwij,,59, dqw";
		fileWriter.append(text);
		fileWriter.close();
		dc.importCSV(file);
		//empty integer
		System.out.println("file with empty int");
		fileWriter = new FileWriter(file);
		text += " \n dnqwij,dsadsa,, dqw";
		fileWriter.append(text);
		fileWriter.close();
		dc.importCSV(file);
	}
*/	
	@Test
	void testDataManagementTestExportCSV() {
		//empty
		//only header
		//one column
		//more than one column
	}
	
	@Test
	void testDataManagementTestSaveProject() throws IOException {
		file = File.createTempFile("temp-file-name", ".comp3111");
		dc.saveProject(file);
		//exception
		assertThrows(IOException.class, ()->dc.saveProject(null));
	}
	
	
	@Test
	void testDataManagementTestLoadProject() {
//		dc.saveProject(file);
	}
}
