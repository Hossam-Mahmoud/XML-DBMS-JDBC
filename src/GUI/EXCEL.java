package GUI;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class EXCEL {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;

	public String[] getColName(ResultSet rs) throws SQLException {
		ResultSetMetaData ms = rs.getMetaData();
		int nCols = ms.getColumnCount();
		String[] colNames = new String[nCols];
		for (int i = 0; i < nCols; i++) {
			colNames[i] = ms.getColumnName(i + 1);
		}
		return colNames;
	}

	public void writeXCEL(ResultSet set, String inputFile, String URL)

	throws IOException, WriteException, SQLException {
		ResultSetMetaData rm = set.getMetaData();

		int cols = rm.getColumnCount();
		String[] colTypes = new String[cols];
		String[] colNames = new String[cols];

		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(java.sql.Types.INTEGER, "integer");
		map.put(java.sql.Types.DOUBLE, "double");
		map.put(java.sql.Types.BOOLEAN, "boolean");
		map.put(java.sql.Types.VARCHAR, "string");

		for (int i = 1; i <= cols; i++) {
			colTypes[i - 1] = map.get(rm.getColumnType(i));
			colNames[i - 1] = rm.getColumnName(i);
		}
		String[][] values = changeToArray(set);

		File file = new File(URL + inputFile + ".xls");
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 2);
		WritableSheet excelSheet = workbook.getSheet(0);
		WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, true,
				UnderlineStyle.SINGLE_ACCOUNTING);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);
		timesBoldUnderline.setLocked(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		// Write a few headers
		int length = colNames.length;
		for (int i = 0; i < length; i++) {
			addCaption(excelSheet, i * 2, 0, colNames[i]);
		}
		// for (String e []: values.get(k)) {
		// k++;
		int lengthArray = values.length;
		for (int k = 0; k < lengthArray; k++) {
			String e[] = values[k];
			for (int i = 0; i < length; i++) {
				if (colTypes[i].equals("integer") || colTypes.equals("double")
						|| colTypes[i].equals("long")) {

					addNumber(excelSheet, 2 * i, k + 1,
							Double.parseDouble(e[i]));

				} else {
					if (colTypes[i].equals("boolean")) {

						addBoolean(excelSheet, 2 * i, k + 1,
								(!e[i].equals("false")) ? true : false);
					} else {

						addLabel(excelSheet, 2 * i, k + 1, e[i]);
					}

				}

			}

		}

		workbook.write();

		workbook.close();

	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)

	throws RowsExceededException, WriteException {
		Label label;
		sheet.mergeCells(column, row, column + 1, row);

		label = new Label(column, row, s, timesBoldUnderline);

		sheet.addCell(label);

	}

	private void addNumber(WritableSheet sheet, int column, int row,

	Double d) throws WriteException, RowsExceededException {
		Number number;
		sheet.mergeCells(column, row, column + 1, row);

		number = new Number(column, row, d, times);
		sheet.addCell(number);
	}

	private void addBoolean(WritableSheet sheet, int column, int row, boolean b)
			throws RowsExceededException, WriteException {
		sheet.mergeCells(column, row, column + 1, row);
		jxl.write.Boolean x = new jxl.write.Boolean(column, row, b, times);
		sheet.addCell(x);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)

	throws WriteException, RowsExceededException {
		Label label;
		sheet.mergeCells(column, row, column + 1, row);

		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

	public String[][] changeToArray(ResultSet rs) throws SQLException {
		ResultSetMetaData ms = rs.getMetaData();
		ArrayList<String[]> table = new ArrayList<String[]>();
		int nCols = ms.getColumnCount();
		String[] row = new String[nCols];
		while (rs.next()) {
			for (int i = 0; i < nCols; i++) {
				row[i] = rs.getObject(i + 1) + "";
			}

			table.add(Arrays.copyOf(row, nCols));
		}

		String[][] result = new String[table.size()][];
		int length = result.length;
		for (int i = 0; i < length; i++) {
			result[i] = Arrays.copyOf(table.get(i), table.get(i).length);

		}

		return result;
	}
}
