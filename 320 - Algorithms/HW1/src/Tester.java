public class Tester {
	public static void main(String[] args) {
		int[] A = {1, 3, 5, 7, 9, 14, 16, 19};
		System.out.println("performing quickSearch(A,8), it return " + quickSearch(A, 8) + ", the index of number 9");
		System.out.println("performing quickSearch(A,19), it return " + quickSearch(A, 19) + ", the index of number 19");
		System.out.println("performing quickSearch(A, 20), it return " + quickSearch(A, 20) + ", indicating there is no such value that is bigger than or equal to 20 in array A");
		System.out.println("performing quickSearch(A,6), it return " + quickSearch(A, 6) + ", the index of number 7");
		System.out.println("performing quickSearch(A,-1), it return " + quickSearch(A, -1) + ", the index of number 10");
		System.out.println("performing quickSearch(A,-1), it return " + quickSearch(A, 1) + ", the index of number 1");
	}

	private static int quickSearch(int[] A, int f) {
		int max = A.length - 1;
		if (A[max] < f) return -1;
		if (A[0] > f) return -1;
		return binarySearch(A, f, max, max / 2);
	}

	private static int binarySearch(int[] A, int f, int max, int mid) {
		int g = A[mid];
		if (g < f) {
			if (A[mid + 1] >= f) return mid + 1;
			return binarySearch(A, f, max, (max + mid) / 2);
		}
		if (g > f) return binarySearch(A, f, mid, mid / 2);
		return mid;
	}
}
