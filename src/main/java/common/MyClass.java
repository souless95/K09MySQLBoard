package common;

public class MyClass {
	
	//정수 a부터 b까지의 합을 반환하는 정적메서드
	public static int myFunc(int a, int b) {
		int sum = 0;
		for(int i=a; i<=b; i++) {
			sum += i;
		}
		return sum;
	}
}
