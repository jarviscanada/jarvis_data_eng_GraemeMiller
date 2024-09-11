package ca.jrvs.insurance_api.service;

import ca.jrvs.insurance_api.model.Car;
import ca.jrvs.insurance_api.model.Person;
import ca.jrvs.insurance_api.repository.PersonRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    private final PersonRepository repo;
    private final MongoTemplate mongoTemplate;

    public PersonService(PersonRepository repo, MongoTemplate mongoTemplate) {
        this.repo = repo;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(Person person) {
        repo.save(person);
    }

    public void saveAll(List<Person> people) {
        repo.saveAll(people);
    }

    public Optional<Person> findOne(ObjectId id) {
        return repo.findById(id);
    }

    public List<Person> findAll(List<ObjectId> ids) {
        return repo.findAllByIds(ids);
    }

    public List<Person> findAll() {
        return repo.findAll();
    }

    public void delete(ObjectId id) {
        repo.deleteById(id);
    }

    public void delete(List<ObjectId> ids) {
        repo.deleteAllById(ids);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

    public Optional<Person>  update(Person person) {
        return repo.findById(person.getId())
                .map(existingPerson -> {
                    existingPerson.setFirstName(person.getFirstName());
                    existingPerson.setLastName(person.getLastName());
                    existingPerson.setAge(person.getAge());
                    existingPerson.setAddressEntity(person.getAddressEntity());
                    existingPerson.setCreatedAt(person.getCreatedAt());
                    existingPerson.setInsurance(person.getInsurance());
                    existingPerson.setCarEntities(person.getCarEntities());
                    return repo.save(existingPerson);
                });
    }

    public List<Optional<Person>> update(List<Person> people) {
        List<Optional<Person>> results = new ArrayList<>();
        for (Person person : people) {
            results.add(update(person));
        }
        return results;
    }

    public long count() {
        return mongoTemplate.count(new Query(), Person.class);
    }

    public double getAverageAge() {
        Aggregation agg = Aggregation.newAggregation(Aggregation.group().avg("age").as("averageAge"));

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, Person.class, Document.class);
        Document result = results.getUniqueMappedResult();

        if(result == null) {
            return 0;
        }

        return result.get("averageAge", Double.class);
    }

    public int getMaxCars() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.project().andExpression("size(carEntities)").as("numCars"),
                Aggregation.group().max("numCars").as("maxCars")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, Person.class, Document.class);
        Document result = results.getUniqueMappedResult();
        if(result == null) {
            return 0;
        }
        return result.getInteger("maxCars", 0);
    }
}
