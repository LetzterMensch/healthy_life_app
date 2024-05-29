package com.example.gr.data.sample;

public interface StressSample extends TimeSample {
    enum Type {
        MANUAL(0),
        AUTOMATIC(1),
        UNKNOWN(2),
        ;

        private final int num;

        Type(final int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public static Type fromNum(final int num) {
            for (Type value : Type.values()) {
                if (value.getNum() == num) {
                    return value;
                }
            }

            throw new IllegalArgumentException("Unknown num " + num);
        }
    }

    /**
     * Returns the measurement type for this stress value.
     */
    Type getType();

    /**
     * Returns the stress value between 0 and 100.
     */
    int getStress();
}
