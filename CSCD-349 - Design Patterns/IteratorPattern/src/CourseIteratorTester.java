import coursePackage.Course;
import coursePackage.Student;

import java.util.Iterator;

class CourseIteratorTester {
	public static void main(String[] args) {
		Course course = new Course();
		course.enroll(new Student("Lars"));
		course.enroll(new Student("Daniel"));
		course.enroll(new Student("David"));
		course.enroll(new Student("Robert"));
		System.out.println("Foreach test");
		for (Student s : course) {
			System.out.println(s.getName());
		}
		System.out.println("Normal iteration test");
		Iterator<Student> i = course.iterator();
		while (i.hasNext()) {
			System.out.println(i.next().getName());
		}
	}
}
