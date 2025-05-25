package ru.informtb.test;

import ru.informtb.model.Product;
import ru.informtb.repository.OutsideFileRepository;

public class Test {

    public static void main(String[] args) {
//        List<Person> array = new ArrayList<>();
//
//        Person p = null;
//
//        for (int i = 0; i < 10; i++) {
//            Person el =  new Person("Bob" + i, 12);
//            if (i == 3) {
//                p = el;
//            }
//            array.add(el);
//        }
//        System.out.println(array);
//        System.out.println(" ");
//        System.out.println(p);
//
//        for (Person el : array) {
//            if (el.equals(p)) {
//                el.setAge(55);
//                el.setName("BOOOOB__COOL");
//            }
//        }
//
//        System.out.println(" ");
//        System.out.println(array);

//        Product p = new Product("dsadas", true, 123);
//
//        System.out.println(p);


        OutsideFileRepository outsideFileRepository = new OutsideFileRepository();

        Product product = new Product("81ea4772-faf7-4070-bc29-c55f217ceb53",
                "Potato", "true", "123"
        );
//        outsideFileRepository.add(product);
//        outsideFileRepository.get(product.getId());
//
//        for (int i = 0; i < 3; i++) {
//            outsideFileRepository.add(new Product(
//                    "Bobi" + i,
//                    "true",
//                    "112333"
//            ));
//        }

        outsideFileRepository.getAll();

        product.setName("Oguretz");

        outsideFileRepository.update(product);

        outsideFileRepository.getAll();


    }
}
