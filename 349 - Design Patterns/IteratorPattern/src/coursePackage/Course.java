package coursePackage;

import java.util.ArrayList;
import java.util.Iterator;

public class Course implements Iterable<Student>, Cloneable {
	private final ArrayList<Student> students;

	public Course() {
		students = new ArrayList<>();
	}

	public void enroll(Student s) {
		students.add(s);
	}

	@Override
	public CourseIterator iterator() {
		CourseIterator i = new CourseIterator();
		i.next();
		return i;
	}

	private class CourseIterator implements Iterator<Student> {
		private Student next;
		private Student curr;
		private int index;

		CourseIterator() {
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Student next() {
			curr = next;
			try {
				next = students.get(index);
			} catch (IndexOutOfBoundsException e) {
				next = null;
			}
			index++;
			return curr;
		}
	}
}
