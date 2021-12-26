package pyshkin.alexandr.board.utils;

import pyshkin.alexandr.board.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class CheckUtilsTest {

    @Test
    void checkNotNull() {
        assertTrue(CheckUtils.checkNotNull(new Object(), "first message"));
        assertTrue(CheckUtils.checkNotNull("The String literal", "second message"));
        assertTrue(CheckUtils.checkNotNull(9L, "third message"));
        Throwable thrown = catchThrowable(() -> {
            CheckUtils.checkNotNull(null, "fourth message");
        });
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        assertEquals(thrown.getMessage(), "fourth message");
    }

    @Test
    void checkPositive() {
        assertTrue(CheckUtils.checkPositive(15L, "first message"));

        Throwable thrown = catchThrowable(() -> {
            assertTrue(CheckUtils.checkPositive(0L, "second message"));
        });
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        assertEquals(thrown.getMessage(), "second message");

        thrown = catchThrowable(() -> {
            assertTrue(CheckUtils.checkPositive(-90L, "third message"));
        });
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        assertEquals(thrown.getMessage(), "third message");

        thrown = catchThrowable(() -> {
            CheckUtils.checkPositive(null, "fourth message");
        });
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        assertEquals(thrown.getMessage(), "fourth message");
    }
}