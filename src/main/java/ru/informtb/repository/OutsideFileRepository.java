package ru.informtb.repository;

import ru.informtb.model.Product;
import ru.informtb.patterns.singleton.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutsideFileRepository implements IReadStorage<Product>, IWriteStorage<Product> {

    private int size;
    private Logger logger;


    public OutsideFileRepository(Logger logger) {
        this.size = 0;
        this.logger = logger;
    }

    @Override
    public Product get(String id) {
        HashMap<String, ArrayList<HashMap<String, String>>> productsMap = readFile();
        return createProduct(id, productsMap);
    }

    private HashMap<String, ArrayList<HashMap<String, String>>> readFile() {
//        File file = new File("/Users/mikle/Documents/data.db");
        File file = new File("C:\\Users\\User\\Documents\\data.db");
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
                    if (!finalList.isEmpty()) {
                        productsMap.put(String.valueOf(index), finalList);
                    }
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
                    logger.logWarning("Не хватает данных для создания продукта");
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
                logger.logWarning("Не хватает данных для создания продукта");
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
        setSize(productsMap.size());
        return productList;
    }


    @Override
    public List<Product> getAll() {
        HashMap<String, ArrayList<HashMap<String, String>>> productsMap = readFile();
        return createProduct(productsMap);
    }

    @Override
    public void add(Product item) {
        if (item == null || item.getId() == null || item.getName() == null || !item.isExpirationDate() || item.getBulk() == -1) {
            logger.logWarning("Product содержит некорректные данные");
            throw new IllegalArgumentException("Product содержит некорректные данные");
        }
        getAll();
        int currentIndexProduct = this.size;
        String id = String.format("product.%d.id = %s", currentIndexProduct, item.getId());
        String name = String.format("product.%d.name = %s", currentIndexProduct, item.getName());
        String expirationDate = String.format("product.%d.expirationDate = %s", currentIndexProduct, item.isExpirationDate());
        String bulk = String.format("product.%d.bulk = %d", currentIndexProduct, item.getBulk());

        String product = String.format(
                "%s\n%s\n%s\n%s\n\n", id, name, expirationDate, bulk
        );

        File file = new File("C:\\Users\\User\\Documents\\data.db");
//        File file = new File("/Users/mikle/Documents/data.db");
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file, true))) {
            bf.write(product);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void update(Product item) {
        List<Product> products = getAll();
        boolean isFoundProduct = false;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(item.getId())) {
                isFoundProduct = true;
                products.set(i, item);
                logger.logInfo(String.format("%s:$s - Измененный продукт", products.get(i).getId(), products.get(i).getName()));
            }
        }

        if (!isFoundProduct) {
            logger.logError(String.format("%s: %s - не найден", item.getId(), item.getName()));
            return;
        }
        deleteAll();
        for (Product product : products) {
            System.out.println(product);
            add(product);
        }
    }

    private void deleteAll() {
        File file = new File("C:\\Users\\User\\Documents\\data.db");
        StringBuilder result = new StringBuilder();
        boolean foundStorage = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().equals("[Storage]")) {
                    foundStorage = true;
                    result.append(line).append("\n");
                    break;
                }
                result.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(result.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (foundStorage) {
            logger.logInfo("Файл успешно обрезан после '[Storage]'");
        } else {
            logger.logError("Строка '[Storage]' не найдена.");
        }

    }

    @Override
    public void delete(String id) {
        List<Product> products = getAll();
        deleteAll();
        for (Product el : products) {
            if (el.getId().equals(id)) {
                logger.logInfo(String.format("Продукт с id = %s удален", id));
                continue;
            }
            add(el);
        }

    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
