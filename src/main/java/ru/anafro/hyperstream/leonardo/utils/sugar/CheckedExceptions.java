package ru.anafro.hyperstream.leonardo.utils.sugar;

public final class CheckedExceptions {
    @FunctionalInterface
    public static interface ThrowingRunnable {
        public void run() throws Exception;
    }

    private CheckedExceptions() {
        // util class
    }

    public static void rethrowUnchecked(final ThrowingRunnable code) {
        try {
            code.run();
        } catch (final RuntimeException uncheckedException) {
            throw uncheckedException;
        } catch (final Exception checkedException) {
            throw new RuntimeException(checkedException);
        }
    }
}
