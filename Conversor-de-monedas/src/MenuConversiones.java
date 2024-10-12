import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class MenuConversiones {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        int opcion = 0;
        String baseCurrency = "";
        String targetCurrency = "";
        double valorUsuario = 0, resultado = 0;

        do {
            // Mostrar menú de opciones
            System.out.println("Seleccione una opción:");
            System.out.println("1) Dólar => Peso Argentino");
            System.out.println("2) Peso Argentino => Dólar");
            System.out.println("3) Dólar => Real Brasileño");
            System.out.println("4) Real Brasileño => Dólar");
            System.out.println("5) Dólar => Peso Colombiano");
            System.out.println("6) Peso Colombiano => Dólar");
            System.out.println("7) Salir");

            // Validar que el usuario ingrese un número
            try {
                opcion = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Por favor, ingrese un número válido.");
                scanner.next(); // Limpiar el buffer para evitar bucles infinitos
                continue;       // Saltar a la siguiente iteración del ciclo
            }

            // Procesar la opción seleccionada
            switch (opcion) {
                case 1:
                    baseCurrency = "USD";  // Dólar
                    targetCurrency = "ARS"; // Peso Argentino
                    break;
                case 2:
                    baseCurrency = "ARS";  // Peso Argentino
                    targetCurrency = "USD"; // Dólar
                    break;
                case 3:
                    baseCurrency = "USD";  // Dólar
                    targetCurrency = "BRL"; // Real Brasileño
                    break;
                case 4:
                    baseCurrency = "BRL";  // Real Brasileño
                    targetCurrency = "USD"; // Dólar
                    break;
                case 5:
                    baseCurrency = "USD";  // Dólar
                    targetCurrency = "COP"; // Peso Colombiano
                    break;
                case 6:
                    baseCurrency = "COP";  // Peso Colombiano
                    targetCurrency = "USD"; // Dólar
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    continue; // Volver al inicio del bucle si la opción no es válida
            }

            // Si el usuario elige salir, no hacemos ninguna consulta
            if (opcion == 7) {
                break;
            }

            // URL para la API con las monedas seleccionadas
            String direccion = "https://v6.exchangerate-api.com/v6/c157d1c3dad6bafc3178cb22/pair/"
                    + baseCurrency + "/" + targetCurrency;

            // Crear el cliente y la solicitud HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(direccion))
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Obtener la respuesta en formato JSON
            String json = response.body();

            // Usar Gson para deserializar la respuesta JSON
            Gson gson = new Gson();
            ConversionesCambio misConversiones = gson.fromJson(json, ConversionesCambio.class);

            // Extraer el tipo de cambio desde el objeto deserializado
            double tasaCambio = misConversiones.conversion_rate();
            String baseCode = misConversiones.base_code();  // Moneda base
            String targetCode = misConversiones.target_code();

            // Pedir al usuario que ingrese el valor a convertir
            System.out.println("Por favor ingrese el valor a convertir: ");
            valorUsuario = scanner.nextDouble();

            // Realizar la operación de conversión multiplicando el valor por la tasa de cambio
            resultado = valorUsuario * tasaCambio;

            // Mostrar el resultado con 2 decimales
            System.out.println("Conversión de " + baseCode + " a " + targetCode + ": "
                    + String.format("%.2f", resultado));

            guardarConsulta(baseCurrency, targetCurrency, valorUsuario, resultado, tasaCambio);

            System.out.println("Finalizó la ejecución del programa.");

        } while (opcion != 7);

        // Cerrar el scanner
        scanner.close();
    }

    private static void guardarConsulta(String baseCurrency, String targetCurrency, double valorUsuario, double resultado, double tasaCambio) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Consulta consulta = new Consulta(baseCurrency, targetCurrency, valorUsuario, resultado, tasaCambio);

        FileWriter escritura = new FileWriter("conversiones.json", true);
        gson.toJson(consulta, escritura);
        escritura.write(gson.toJson(resultado) + "\n");
        escritura.close();
    }

}
