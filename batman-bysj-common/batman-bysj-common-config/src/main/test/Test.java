import com.batman.bysj.BatmanBysjCommonConfigApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author victor.qin
 * @date 2018/5/29 12:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    TestMapper testMapper;

    @Test
    public void testTr(){

    }


}
