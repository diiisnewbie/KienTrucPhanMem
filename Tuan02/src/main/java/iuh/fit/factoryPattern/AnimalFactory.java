package iuh.fit.factoryPattern;

public abstract class AnimalFactory {
    public abstract Animal createAnimal();

    public void describeAnimal() {
        Animal animal = createAnimal();
        animal.speak();
    }
}
