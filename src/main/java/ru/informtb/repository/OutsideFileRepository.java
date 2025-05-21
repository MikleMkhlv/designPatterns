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


//    @Override
//    public Product get(String id) {
//        File file = new File("C:\\Users\\User\\Documents\\data.db");
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line = reader.readLine();
//            boolean inStorageSection = false;
//
//            HashMap<String, String> currentProduct = new HashMap<>();
//
//            while (true) {
//                line = reader.readLine().trim();
//
//                if (line.equals("[Storage]")) {
//                    inStorageSection = true;
//                    continue;
//                }
//
//                if (!inStorageSection || line.isEmpty()) continue;
//
//                HashMap<String, String> element = getElement(line);
//                if (element.isEmpty()) continue;
//
//                currentProduct.putAll(element);
//
//                // Если нашли нужный ID
//                if ("id".equals(element.get("key")) && id.equals(element.get("value"))) {
//                    return new Product(
//                            currentProduct.get("id"),
//                            currentProduct.get("name"),
//                            currentProduct.get("expirationDate"),
//                            currentProduct.get("bulk")
//                    );
//                }
//
//                // Проверяем следующую строку
//                String nextLine = reader.readLine();
//                if (nextLine == null || nextLine.trim().isEmpty()) {
//                    currentProduct.clear();
//                } else {
//                    line = nextLine; // Подставляем её как текущую
//                    // ...и продолжаем цикл
//                }
//                System.out.println("asfdas");
//            }
//
//        } catch (IOException e) {
//            System.err.println("Ошибка чтения файла: " + e.getMessage());
//        }
//
//        return null;
//    }
//
//    private HashMap<String, String> getElement(String line) {
//        HashMap<String, String> map = new HashMap<>();
//        if (line.contains("=")) {
//            String[] parts = line.split("=", 2);
//            String keyPart = parts[0].trim();
//            String value = parts[1].trim();
//
//            // Получаем только имя поля, например: "expirationDate"
//            String key = keyPart.split("\\.")[2];
//
//            map.put(key, value);
//        }
//        return map;
//    }
//
//    public Product productFilling(Product product, HashMap<String, String> item) throws IllegalAccessException {
//        Class<? extends BaseElement> productClass = product.getClass();
//
//        for (Map.Entry<String, String> entry : item.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//
//            boolean found = false;
//
//            // Поиск в текущем классе
//            for (Field field : productClass.getDeclaredFields()) {
//                if (field.getName().equals(key)) {
//                    field.setAccessible(true);
//                    setFieldValue(field, product, value);
//                    found = true;
//                    break;
//                }
//            }
//
//            // Если не найдено — ищи в родителе
//            if (!found) {
//                for (Field field : productClass.getSuperclass().getDeclaredFields()) {
//                    if (field.getName().equals(key)) {
//                        field.setAccessible(true);
//                        setFieldValue(field, product, value);
//                        break;
//                    }
//                }
//            }
//        }
//
//        return product;
//    }
//
//    // Вспомогательный метод для корректной установки значений разных типов
//    private void setFieldValue(Field field, Object target, String value) throws IllegalAccessException {
//        Class<?> fieldType = field.getType();
//
//        if (fieldType == boolean.class || fieldType == Boolean.class) {
//            field.set(target, Boolean.parseBoolean(value));
//        } else if (fieldType == int.class || fieldType == Integer.class) {
//            field.set(target, Integer.parseInt(value));
//        } else if (fieldType == String.class) {
//            field.set(target, value);
//        } else {
//            throw new UnsupportedOperationException("Неизвестный тип поля: " + fieldType);
//        }
//    }


    @Override
    public Product get(String id) {
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

        return createProduct(id, productsMap);
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
                if (id == null || name == null || expirationDate == null || bulk == null) {
                    throw new IllegalArgumentException("Не хватает данных для создания продукта");
                }
                return new Product(identy, name, expirationDate, bulk);
            }
        }
        return null;
    }

    @Override
    public List<Product> getAll() {
        return List.of();
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
