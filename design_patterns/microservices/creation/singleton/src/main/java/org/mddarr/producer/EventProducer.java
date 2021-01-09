package org.mddarr.producer;

public class EventProducer {
    public static void main(String[] args) {


        User user1 = new User.newBuilder("Lokesh", "Gupta")
                .setAge(30)
                .setPhone("1234567")
                .setAddress("Fake address 1234")
                .build();

        System.out.println(user1);

        User user2 = new User.newBuilder("Jack", "Reacher")
                .setAge(40)
                .setPhone("5655")
                .build();

        System.out.println(user2);

        User user3 = new User.newBuilder("Super", "Man")
                //No age
                //No phone
                //no address
                .build();

        System.out.println(user3);

        System.out.println("dfdf");

        EagerInitSingletonClass eagerInitSingletonClass = EagerInitSingletonClass.getInstance();

        eagerInitSingletonClass.print();
    }
}


class EagerInitSingletonClass{
    /*
    1) private constant static instance (class-member)
    2) static/factory method returns object of the singleton class
     */
    private static final EagerInitSingletonClass SINGLE_INSTANCE =  new EagerInitSingletonClass();
    private EagerInitSingletonClass(){}
    public void print(){
        System.out.println("I GET CALLED FROM THE CLIENT");
    }
    public static EagerInitSingletonClass getInstance(){
        return SINGLE_INSTANCE;
    }
}

class LazyInitSingletonClass{
    /*
        1) constructor as private
        2) make a private static instance (class-member) of this singleton class, BUT DO NOT INSTANTIATE
        3) Write a static/factory method checks the instance member for null and creates instance
     */

    private static LazyInitSingletonClass SINGLE_INSTANCE = null;
    private LazyInitSingletonClass(){}
    public static LazyInitSingletonClass getInstance(){
        if (SINGLE_INSTANCE==null){
            synchronized (LazyInitSingletonClass.class){
                SINGLE_INSTANCE = new LazyInitSingletonClass();
            }
        }
        return SINGLE_INSTANCE;
    }

}
