import com.fasterxml.jackson.databind.ObjectMapper;
import model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.io.Reader;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonTests {

    private final ClassLoader cl = JsonTests.class.getClassLoader();

    @Test
    @DisplayName("Проверка json объекта")
    void jsonFileParsingImprovedTest() throws Exception {
        try (Reader reader = new InputStreamReader(
                cl.getResourceAsStream("book.json")
            )) {
            ObjectMapper objectMapper = new ObjectMapper();
            Book book = objectMapper.readValue(reader, Book.class);
            assertThat(book.getId()).isEqualTo(1);
            assertThat(book.getTitle()).isEqualTo("Anna Karenina");
            assertThat(book.getAuthor()).isEqualTo("Lev Tolstoy");
            assertThat(book.getYear()).isEqualTo(1873);
            assertThat(book.getTags()).isEqualTo(new String[]{"novel", "classic", "realism"});
        }
    }
}
