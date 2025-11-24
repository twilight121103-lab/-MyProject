package app.model;

import org.springframework.stereotype.Component;

@Component("dog")
public class Dog extends Animal {
    public Dog() {
    }

    @Override
    public String toString() {
        return "Dog{}";
    }
}
