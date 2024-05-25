import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static org.assertj.core.api.Assertions.assertThat;

public class ZipTests {

    private final ClassLoader cl = ZipTests.class.getClassLoader();

    @Test
    @DisplayName("Проверка pdf файла в архиве")
    void checkPdfContentInZip() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("ISTQB_CTFL_Syllabus_2018_RU.pdf")) {
                    PDF pdf = new PDF(zis);
                    assertThat(pdf.numberOfPages).isEqualTo(96);
                    assertThat(pdf.author).isEqualTo("ISTQB Working Party Advanced Level");
                    assertThat(pdf.text.contains("Программа обучения Базового уровня")).isTrue();
                }
            }
        }
    }

    @Test
    @DisplayName("Проверка xlsx файла в архиве")
    void checkXlsxContentInZip() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("stroyberg_pricelist.xlsx")) {
                    XLS xls = new XLS(zis);
                    String actualHeader = xls.excel.getSheetAt(0).getRow(8).getCell(2).getStringCellValue();
                    assertThat(xls.excel.getNumberOfSheets()).isEqualTo(34);
                    assertThat(actualHeader).contains("ПРАЙС-ЛИСТ ООО \"Стройберг\" от ");
                }
            }
        }
    }

    @Test
    @DisplayName("Проверка csv файла в архиве")
    void parsingCsvFileInZip() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
            )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("EKF.csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = csvReader.readAll();
                    assertThat(data.size()).isGreaterThan(1);
                    assertThat(data.get(0)).isEqualTo(new String[]{"Категория", "Артикул", "Наименование", "Серия",
                    "Базовая цена", "Статус", "Цвет", "Материал"});
                }
            }
        }
    }
}
