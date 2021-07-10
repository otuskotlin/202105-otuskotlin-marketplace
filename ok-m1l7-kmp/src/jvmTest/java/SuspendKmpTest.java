import kotlin.jvm.JvmField;
import org.junit.Test;
import ru.otus.otuskotlin.marketplace.kmp.java.SuspendJava;

import static org.junit.Assert.assertEquals;

public class SuspendKmpTest {
    @Test
    public void suspendTest() {
        assertEquals("Suspend JVM", new SuspendJava().susp());
    }

    @Test
    public void suspendManyTest() {
        var res = SuspendJava.suspMany(
                new SuspendJava(),
                new SuspendJava()
        ).toArray();
        assertEquals("Suspend JVM", res[0]);
        assertEquals("Suspend JVM", res[1]);
    }

    @Test
    public void overloadsTest() {
        new SuspendJava().manyDefaults("our-value");
    }
}
