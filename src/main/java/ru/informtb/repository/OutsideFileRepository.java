package ru.informtb.repository;

import ru.informtb.model.Product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutsideFileRepository implements IReadStorage<Product>, IWriteStorage<Product> {

    @Override
    public Product get(String id) {
        HashMap<String, ArrayList<HashMap<String, String>>> productsMap = readFile();
        return createProduct(id, productsMap);
    }

    private HashMap<String, ArrayList<HashMap<String, String>>> readFile() {
        File file = new File("/Users/mikle/Documents/data.db");
        HashMap<String, ArrayList<HashMap<String, String>>> productsMap = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            boolean inStorageSection = false;
            String prevLine = "";
            ArrayList<HashMap<String, String>> products = new ArrayList<>();
            while (true) {
                if ((line = bufferedReader.readLine()) == null) {
                    ArrayList<HashMap<String, String>> finalList = new ArrayList<>(products);
                    productsMap.put(String.valueOf(index), finalList);
                    break;
                }

                if (line.equals("[Storage]")) {
                    inStorageSection = true;
                    continue;
                }

                if (!inStorageSection || line.isEmpty()) {
                    prevLine = line;
                    continue;
                }

                if (prevLine.isEmpty() && getIndexElement(line) > index) {
                    ArrayList<HashMap<String, String>> finalList = new ArrayList<>(products);
                    productsMap.put(String.valueOf(index), finalList);
                    products.clear();
                    index++;
                }

                HashMap<String, String> element = getElement(line, index);
                assert element != null;
                if (element.containsKey("id")) {
                    HashMap<String, String> product = new HashMap<>();
                    products.add(element);
                    prevLine = line;
                } else {
                    HashMap<String, String> product = new HashMap<>();
                    products.add(element);
                    prevLine = line;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return productsMap;

    }

    private HashMap<String, String> getElement(String line, int index) {
        HashMap<String, String> item = new HashMap<>();
        String[] array = line.split("\\.");
        String key = array[2].split(" ")[0];
        String value = array[2].split(" ")[2];
        if (array[1].equals(Integer.toString(index))) {
            item.put(key, value);
            return item;
        }
        return null;
    }

    private int getIndexElement(String line) {
        return Integer.parseInt(line.split("\\.")[1]);
    }

    private Product createProduct(String id, HashMap<String, ArrayList<HashMap<String, String>>> productsMap) {
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> el : productsMap.entrySet()) {
            if (el.getValue().getFirst().get("id").equals(id)) {
                String identy = null;
                String name = null;
                String expirationDate = null;
                String bulk = null;

                for (HashMap<String, String> listElements : el.getValue()) {
                    for (Map.Entry<String, String> entry : listElements.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        switch (key) {
                            case "id":
                                identy = value;
                                break;
                            case "name":
                                name = value;
                                break;
                            case "expirationDate":
                                expirationDate = value;
                                break;
                            case "bulk":
                                bulk = value;
                                break;
                        }
                    }
                }
                if (identy == null || name == null || expirationDate == null || bulk == null) {
                    throw new IllegalArgumentException("Не хватает данных для создания продукта");
                }
                return new Product(identy, name, expirationDate, bulk);
            }
        }
        return null;
    }


    private List<Product> createProduct(HashMap<String, ArrayList<HashMap<String, String>>> productsMap) {
        List<Product> productList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> el : productsMap.entrySet()) {
            String identy = null;
            String name = null;
            String expirationDate = null;
            String bulk = null;
            for (HashMap<String, String> listElements : el.getValue()) {
                for (Map.Entry<String, String> entry : listElements.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    switch (key) {
                        case "id":
                            identy = value;
                            break;
                        case "name":
                            name = value;
                            break;
                        case "expirationDate":
                            expirationDate = value;
                            break;
                        case "bulk":
                            bulk = value;
                            break;
                    }
                }
            }
            if (identy == null || name == null || expirationDate == null || bulk == null) {
                throw new IllegalArgumentException("Не хватает данных для создания продукта");
            } else {
                productList.add(new Product(
                        identy,
                        name,
                        expirationDate,
                        bulk
                ));
            }
        }
        return productList;
    }

    @Override
    public List<Product> getAll() {
        HashMap<String, ArrayList<HashMap<String, String>>> productsMap = readFile();
        return createProduct(productsMap);
    }

    @Override
    public void add(Product item) {

    }

    @Override
    public void update(Product item) {

    }

    @Override
    public void delete(String id) {

    }
}
