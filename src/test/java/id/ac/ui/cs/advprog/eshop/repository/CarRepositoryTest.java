package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class CarRepositoryTest {

    private CarRepository carRepository;
    private Car car;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
        car = new Car();
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);
    }

    @Test
    void testCreateWithNullId() {
        car.setCarId(null);
        Car result = carRepository.create(car);

        assertNotNull(result.getCarId());
        assertEquals("Toyota", result.getCarName());
    }

    @Test
    void testCreateWithExistingId() {
        car.setCarId("existing-id");
        Car result = carRepository.create(car);

        assertEquals("existing-id", result.getCarId());
    }

    @Test
    void testFindAll() {
        carRepository.create(car);

        Car car2 = new Car();
        car2.setCarName("Honda");
        car2.setCarColor("Blue");
        car2.setCarQuantity(3);
        carRepository.create(car2);

        Iterator<Car> iterator = carRepository.findAll();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testFindByIdFound() {
        Car created = carRepository.create(car);
        Car found = carRepository.findById(created.getCarId());

        assertNotNull(found);
        assertEquals(created.getCarId(), found.getCarId());
    }

    @Test
    void testFindByIdNotFound() {
        Car found = carRepository.findById("not-exist");
        assertNull(found);
    }

    @Test
    void testFindByIdNotFoundAfterCreate() {
        carRepository.create(car);
        Car found = carRepository.findById("id-yang-tidak-ada");
        assertNull(found);
    }

    @Test
    void testUpdateFound() {
        Car created = carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda");
        updatedCar.setCarColor("Blue");
        updatedCar.setCarQuantity(10);

        Car result = carRepository.update(created.getCarId(), updatedCar);

        assertNotNull(result);
        assertEquals("Honda", result.getCarName());
        assertEquals("Blue", result.getCarColor());
        assertEquals(10, result.getCarQuantity());
    }

    @Test
    void testUpdateNotFound() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Honda");

        Car result = carRepository.update("not-exist", updatedCar);

        assertNull(result);
    }

    @Test
    void testUpdateNotFoundAfterCreate() {
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda");
        updatedCar.setCarColor("Blue");
        updatedCar.setCarQuantity(10);

        Car result = carRepository.update("id-yang-tidak-ada", updatedCar);
        assertNull(result);
    }

    @Test
    void testDeleteFound() {
        Car created = carRepository.create(car);
        String id = created.getCarId();

        carRepository.delete(id);

        assertNull(carRepository.findById(id));
    }

    @Test
    void testDeleteNotFound() {
        carRepository.create(car);

        assertDoesNotThrow(() -> carRepository.delete("not-exist"));

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
    }
}