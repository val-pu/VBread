package val.mx.vbread.views;

import java.util.HashMap;

public class ParameterRequestEvent {

    private HashMap<String, Parameter> parameterRequests;

    public ParameterRequestEvent() {
        parameterRequests = new HashMap<String, Parameter>();
    }

    public void request(String name, double min_value, double max_value) {
        parameterRequests.put(name, new Parameter(name, min_value, max_value));
    }

    public HashMap<String, Parameter> getParameterRequests() {
        return parameterRequests;
    }

    public static class Parameter {

        private double value;
        private final String name;
        private final double min_value;
        private final double max_value;

        public Parameter(String name, double min_value, double max_value) {
            this.name = name;
            this.min_value = min_value;
            this.max_value = max_value;
            value = (min_value + max_value) / 2;
        }

        public double getAbsW() {
            return (Math.abs(min_value) + Math.abs(max_value));


        }

        public double getMax_value() {
            return max_value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public double getMin_value() {
            return min_value;
        }

        public String getName() {
            return name;
        }
    }

}
