package org.example.value_object;

public class ResponseMessageCode {
    private ResponseMessageCode() {
    }

    public class CsvDagger {
        private CsvDagger() {
        }

        public class Success {
            public static final String GET_DATA = "csv_dagger.success.get_data";
            public static final String CREATE = "csv_dagger.success.create";

            private Success() {
            }
        }
    }
}
