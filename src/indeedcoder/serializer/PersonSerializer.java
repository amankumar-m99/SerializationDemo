package indeedcoder.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import indeedcoder.model.Person;

public class PersonSerializer {

	public static Optional<Person> deSerializePerson(File file) {
		Person person = null;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			person = (Person) in.readObject();
			System.out.println("Object deserialized: " + person);
			return Optional.of(person);
		} catch (InvalidClassException ee) {
			System.err.println("Incompatible classes!");
			System.err.println(ee.getMessage());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static boolean serializePerson(Person person, String path) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
			out.writeObject(person);
			System.out.println("Object serialized: " + person);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
