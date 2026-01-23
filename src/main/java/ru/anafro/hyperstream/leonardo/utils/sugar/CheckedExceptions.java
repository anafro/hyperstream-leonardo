package ru.anafro.hyperstream.leonardo.utils.sugar;

public final class CheckedExceptions {
    @FunctionalInterface
    public static interface ThrowingRunnable {
        public void run() throws Exception;
    }

    @FunctionalInterface
    public static interface ThrowingSupplier<R> {
        public R supply() throws Exception;
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

    public static <R> R rethrowUnchecked(final ThrowingSupplier<R> code) {
        try {
            return code.supply();
        } catch (final RuntimeException uncheckedException) {
            throw uncheckedException;
        } catch (final Exception checkedException) {
            throw new RuntimeException(checkedException);
        }
    }
}
