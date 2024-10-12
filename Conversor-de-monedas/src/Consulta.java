public class Consulta {
    private final String baseCurrency;
    private final String targetCurrency;
    private final double valorUsuario;
    private final double resultado;
    private final double tasaCambio;

    public Consulta(String baseCurrency, String targetCurrency, double valorUsuario, double resultado, double tasaCambio) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.valorUsuario = valorUsuario;
        this.resultado = resultado;
        this.tasaCambio = tasaCambio;
    }

    @Override
    public String toString() {
        return "Consulta de " + valorUsuario + " " + baseCurrency + " a " + targetCurrency +
                " con tasa de cambio de " + tasaCambio + ". Resultado: " + resultado;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public double getValorUsuario() {
        return valorUsuario;
    }

    public double getResultado() {
        return resultado;
    }

    public double getTasaCambio() {
        return tasaCambio;
    }
}


