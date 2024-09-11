package ca.jrvs.insurance_api.repository;

import ca.jrvs.insurance_api.model.Person;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, String> {

    Optional<Person> findById(ObjectId id);

    @Query("{ '_id' : { $in: ?0 } }")
    List<Person> findAllByIds(List<ObjectId> ids);

    List<Person> findAll();

    void deleteById(ObjectId id);

    void deleteAllById(List<ObjectId> ids);
}
