import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConversorDeMoedas {

    // Substitua pela sua chave de API
    private static final String API_KEY = "<API KEY HERE>";
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao Conversor de Moedas!");

        while (true) {
            System.out.println("Escolha uma opção de conversão:");
            System.out.println("1. Dólar para Real");
            System.out.println("2. Real para Dólar");
            System.out.println("3. Euro para Real");
            System.out.println("4. Real para Euro");
            System.out.println("5. Libra Esterlina para Real");
            System.out.println("6. Real para Libra Esterlina");
            System.out.println("7. Sair");
            System.out.print("Opção: ");

            int opcao = scanner.nextInt();
            if (opcao == 7) {
                System.out.println("Encerrando o programa. Até mais!");
                break;
            }

            System.out.print("Digite o valor para conversão: ");
            double valor = scanner.nextDouble();

            double resultado = realizarConversao(opcao, valor);
            if (resultado != -1) {
                System.out.printf("Valor convertido: %.2f%n", resultado);
            } else {
                System.out.println("Opção inválida ou erro na conversão.");
            }
        }

        scanner.close();
    }

    private static double realizarConversao(int opcao, double valor) {
        try {
            String baseCurrency = "BRL";
            String targetCurrency;

            switch (opcao) {
                case 1: targetCurrency = "BRL"; baseCurrency = "USD"; break;
                case 2: targetCurrency = "USD"; break;
                case 3: targetCurrency = "BRL"; baseCurrency = "EUR"; break;
                case 4: targetCurrency = "EUR"; break;
                case 5: targetCurrency = "BRL"; baseCurrency = "GBP"; break;
                case 6: targetCurrency = "GBP"; break;
                default: return -1;
            }

            URL url = new URL(API_URL + baseCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            double exchangeRate = jsonObject.getAsJsonObject("rates").get(targetCurrency).getAsDouble();

            return valor * exchangeRate;
        } catch (Exception e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
            return -1;
        }
    }
}
