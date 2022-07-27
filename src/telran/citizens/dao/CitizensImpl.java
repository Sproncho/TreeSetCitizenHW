package telran.citizens.dao;

import java.time.LocalDate;
import java.util.*;

import telran.citizens.interfaces.Citizens;
import telran.citizens.model.Person;

public class CitizensImpl implements Citizens {
	private TreeSet<Person> idSet;
	private TreeSet<Person> lastNameSet;
	private TreeSet<Person> ageSet;
	private static Comparator<Person> lastNameComparator;
	private static Comparator<Person> ageComparator;
	
	static {
		lastNameComparator = (p1, p2) -> {
			int res = p1.getLastName().compareTo(p2.getLastName());
			return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
		};
		ageComparator = (p1, p2) -> {
			int res = Integer.compare(p1.getAge(), p2.getAge());
			return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
		};
	}

	public CitizensImpl() {
		idSet = new TreeSet<>();
		lastNameSet = new TreeSet<>(lastNameComparator);
		ageSet = new TreeSet<>(ageComparator);
	}

	public CitizensImpl(List<Person> citizens) {
		this();
		citizens.forEach(p -> add(p));
	}
	
	public CitizensImpl(Person... citizens) {
		this();
		for (int i = 0; i < citizens.length; i++) {
			add(citizens[i]);
		}
	}

	// O(n)
	@Override
	public boolean add(Person person) {
		if (person == null) {
			return false;
		}
		idSet.add(person);
		lastNameSet.add(person);
		return ageSet.add(person);
	}

	// O(n)
	@Override
	public boolean remove(int id) {
		if(!idSet.contains(new Person(id,null,null,null)))
			return false;
		Person victim = idSet.floor(new Person(id,null,null,null));
		return idSet.remove(victim) && lastNameSet.remove(victim) && ageSet.remove(victim);
	}

	// O(log(n))
	@Override
	public Person find(int id) {
		Person pattern = new Person(id, null, null, null);
		return idSet.contains(pattern) ? idSet.ceiling(pattern) : null;
	}

	// O(log(n))
	@Override
	public Iterable<Person> find(int minAge, int maxAge) {
		LocalDate now = LocalDate.now();
		Person pattern = new Person(Integer.MIN_VALUE, null, null,  now.minusYears(minAge));
		Person from = ageSet.ceiling(pattern);
		pattern = new Person(Integer.MAX_VALUE, null, null, now.minusYears(maxAge));
		Person to = ageSet.floor(pattern);
		return ageSet.subSet(from,true, to,true);
	}

	// O(log(n))
	@Override
	public Iterable<Person> find(String lastName) {
		Person pattern = new Person(Integer.MIN_VALUE, null, lastName, null);
		Person from = lastNameSet.ceiling(pattern);
		pattern = new Person(Integer.MAX_VALUE, null, lastName, null);
		Person to = lastNameSet.floor(pattern);
		return lastNameSet.subSet(from,true, to,true);
	}

	// O(1)
	@Override
	public Iterable<Person> getAllPersonSortedById() {
		return idSet;
	}

	// O(1)
	@Override
	public Iterable<Person> getAllPersonSortedByLastName() {
		return lastNameSet;
	}

	// O(1)
	@Override
	public Iterable<Person> getAllPersonSortedByAge() {
		return ageSet;
	}

	// O(1)
	@Override
	public int size() {
		return idSet.size();
	}

}
