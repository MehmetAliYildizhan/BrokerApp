package com.example.brokerr.repository;

import com.example.brokerr.model.Customer;
import com.example.brokerr.model.Employee;
import com.example.brokerr.model.Stock;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.example.brokerr.model.Asset;

@Repository
public class StockRepository {

    private static final String API_KEY = "7GO432VEZUD71CTY";
    private static final String BASE_URL = "https://www.alphavantage.co/query";

    @PersistenceContext
    private EntityManager entityManager;

    /*public int updateStockData(){

        String function = "TIME_SERIES_DAILY";  // Alpha Vantage fonksiyonu
        List<String> stockList = new ArrayList<>();
        stockList.add("IBM");
        stockList.add("AAPL");
        stockList.add("MSFT");
        stockList.add("AMZN");
        stockList.add("TSLA");
        stockList.add("META");
        stockList.add("PEP");
        stockList.add("GOOG");
        stockList.add("COST");

        try {
            for(int i=0;i<stockList.size();i++) {
                String response = getStockData(stockList.get(i).toString(), function);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject timeSeries = jsonObject.getJSONObject("Time Series (Daily)");
                String firstDate = timeSeries.keys().next();  // İlk tarihe erişim
                JSONObject firstDayData = timeSeries.getJSONObject(firstDate);

                System.out.println("Date: " + firstDate);
                System.out.println("Open: " + firstDayData.getString("1. open"));
                System.out.println("High: " + firstDayData.getString("2. high"));
                System.out.println("Low: " + firstDayData.getString("3. low"));
                System.out.println("Close: " + firstDayData.getString("4. close"));
                System.out.println("Response: ");
                System.out.println(response);

                Stock stock = new Stock();
                stock.setStockName(stockList.get(i).toString());
                stock.setStockPrice((long) Double.parseDouble(firstDayData.getString("1. open")));
                stock.setVolume(Long.valueOf(0));
                save(stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }*/

    public Asset findByCustomerAndAssetName(Customer customer, String assetName) {
        String sql = "SELECT * FROM asset WHERE customer_id = :customerId AND asset_name = :assetName";

        // SQL sorgusu ile arama
        try {
            return (Asset) entityManager.createNativeQuery(sql, Asset.class)
                    .setParameter("customerId", customer.getId())  // Müşterinin ID'sini set et
                    .setParameter("assetName", assetName)  // Asset name parametresini set et
                    .getSingleResult();  // Tek bir sonuç döner, bulunmazsa exception fırlatır
        } catch (NoResultException e) {
            return null;  // Eğer sonuç bulunamazsa null döndür
        }
    }

    public int updateStockData() {
        String function = "TIME_SERIES_DAILY";
        List<String> stockList = List.of("IBM", "AAPL", "MSFT", "AMZN", "TSLA", "META", "PEP", "GOOG", "COST");

        try {
            for (String stockName : stockList) {
                String response = getStockData(stockName, function);
                JSONObject jsonObject = new JSONObject(response);
                // API limiti aşımı kontrolü
                if (jsonObject.has("Information")) {
                    System.out.println("API limiti aşıldı: " + jsonObject.getString("Information"));
                    break;  // Eğer limite ulaşıldıysa döngüden çık
                }

                // "Time Series (Daily)" verisinin varlığını kontrol et
                if (!jsonObject.has("Time Series (Daily)")) {
                    System.out.println("Time Series (Daily) verisi bulunamadı. Gelen yanıt: " + response);
                    continue;
                }
                JSONObject timeSeries = jsonObject.getJSONObject("Time Series (Daily)");
                String firstDate = timeSeries.keys().next();  // İlk tarihe erişim
                JSONObject firstDayData = timeSeries.getJSONObject(firstDate);

                System.out.println("Date: " + firstDate);
                System.out.println("Open: " + firstDayData.getString("1. open"));
                System.out.println("High: " + firstDayData.getString("2. high"));
                System.out.println("Low: " + firstDayData.getString("3. low"));
                System.out.println("Close: " + firstDayData.getString("4. close"));

                // Açılış fiyatını long değere dönüştürme
                Long stockPrice = (long) Double.parseDouble(firstDayData.getString("1. open"));

                // Veritabanında hisseyi bulma veya yeni kayıt oluşturma
                Stock stock = findByStockName(stockName)
                        .orElse(new Stock());  // Yeni kayıt oluşturulması için boş Stock nesnesi

                stock.setStockName(stockName);
                stock.setStockPrice(stockPrice);
                stock.setVolume(0L);
                stock.setLastUpdateTime(LocalDateTime.now());  // Güncelleme zamanını ayarlayın

                save(stock);  // Güncelleme veya ekleme
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;  // Hata durumunda farklı bir değer döndürebiliriz
        }
        return 1;  // Başarılı güncelleme işlemi tamamlandı
    }

    public Optional<Stock> findByStockName(String stockName) {
        try {
            String query = "SELECT s FROM Stock s WHERE s.stockName = :stockName";
            TypedQuery<Stock> typedQuery = entityManager.createQuery(query, Stock.class);
            typedQuery.setParameter("stockName", stockName);
            return Optional.ofNullable(typedQuery.getSingleResult());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();  // Eğer kayıt yoksa veya hata varsa boş döner
        }
    }

    public static String getStockData(String symbol, String function) throws Exception {
        // API isteği için URL oluşturma
        String urlString = BASE_URL + "?function=" + function + "&symbol=" + symbol + "&apikey=" + API_KEY;
        URL url = new URL(urlString);

        // HTTP bağlantısını açma
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // HTTP yanıtını okuma
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // Başarılı istek
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("HTTP GET Request Failed with Error Code : " + responseCode);
        }
    }
    @Transactional
    public void save(Stock stock) {
        entityManager.persist(stock);
        return;
    }

    public List<Stock> getAllStocks() {
        String query = "SELECT s FROM Stock s";  // JPQL sorgusu
        TypedQuery<Stock> typedQuery = entityManager.createQuery(query, Stock.class);
        return typedQuery.getResultList();  // Sonuç listesini döner
    }

    public BigDecimal getStockPriceByName(String stockName) {
        String query = "SELECT s.stockPrice FROM Stock s WHERE s.stockName = :stockName";
        TypedQuery<Long> typedQuery = entityManager.createQuery(query, Long.class);
        typedQuery.setParameter("stockName", stockName);
        try {
            Long l = typedQuery.getSingleResult();
            BigDecimal d = new BigDecimal(l);
            return d;  // Return the stock price for the given stock name
        } catch (NoResultException e) {
            return null;  // Return null if no stock is found with the given name
        }
    }

    public String getStockNameById(Long stockId) {
        String query = "SELECT s.stockName FROM Stock s WHERE s.stockId = :stockId";
        TypedQuery<String> typedQuery = entityManager.createQuery(query, String.class);
        typedQuery.setParameter("stockId", stockId);
        try {
            return typedQuery.getSingleResult();  // Return the stock price for the given stock name
        } catch (NoResultException e) {
            return null;  // Return null if no stock is found with the given name
        }
    }



    /*public int checkIfDataExists(String symbol){

        String query = "SELECT e FROM Employee e WHERE e.stock_name = :symbol"; // JPQL sorgusu
        TypedQuery<Employee> typedQuery = entityManager.createQuery(query, Employee.class);
        typedQuery.setParameter("symbol", symbol);
        try {
            return 1; // Sonuç döner
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            // Diğer olası hataları loglayabiliriz
            e.printStackTrace();
            return 0;
        } // Sonuç döner
    }*/
}
