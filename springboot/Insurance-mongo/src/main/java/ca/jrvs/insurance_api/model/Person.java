package ca.jrvs.insurance_api.model;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document("people")
public class Person {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private int age;
    private RabbitConnectionDetails.Address addressEntity;
    private Date createdAt = new Date();
    private Boolean insurance;
    private List<Car> carEntities;

    // Constructors
    public Person() {}
    public Person(ObjectId id, String firstName, String lastName, int age, RabbitConnectionDetails.Address addressEntity, Date createdAt, Boolean insurance, List<Car> carEntities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.addressEntity = addressEntity;
        this.createdAt = createdAt;
        this.insurance = insurance;
        this.carEntities = carEntities;
    }
    // Getters and Setters

    public ObjectId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public RabbitConnectionDetails.Address getAddressEntity() {
        return addressEntity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public List<Car> getCarEntities() {
        return carEntities;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddressEntity(RabbitConnectionDetails.Address addressEntity) {
        this.addressEntity = addressEntity;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public void setCarEntities(List<Car> carEntities) {
        this.carEntities = carEntities;
    }

    // ToString, Hashcode, Equals


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", addressEntity=" + addressEntity +
                ", createdAt=" + createdAt +
                ", insurance=" + insurance +
                ", carEntities=" + carEntities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(addressEntity, person.addressEntity) && Objects.equals(createdAt, person.createdAt) && Objects.equals(insurance, person.insurance) && Objects.equals(carEntities, person.carEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age, addressEntity, createdAt, insurance, carEntities);
    }
}
