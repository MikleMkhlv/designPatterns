package ru.informtb.test;

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
        System.out.println(outsideFileRepository.get("7c664580-5b06-4d5b-82dc-a9b37e168379"));

    }
}

class Person {

    private String name;
    private int age;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;


    }
}
