import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class A1 {

	/**
	 * @param args
	 */
	private static double circle_perimeter(int[] coords, double radius) {
		return 2 * Math.PI * radius;
	}

	private static double circle_area(int[] coords, double radius) {
		return Math.PI * radius * radius;		
	}

	private static double rectangle_perimeter(int[] coords) {
		return 2 * (Math.abs(coords[0]-coords[2]) + Math.abs(coords[1]-coords[3]));
	}

	private static double rectangle_area(int[] coords) {
		return Math.abs(coords[0]-coords[2]) * Math.abs(coords[1]-coords[3]);		
	}

	private static double triangle_area(int[] coords) {	
		// ax ay bx by cx cy
		// 0  1  2  3  4  5
		return Math.abs((coords[0] * (coords[3] - coords[5]) + coords[2] * (coords[5] - coords[1]) +
				coords[4] * (coords[1] - coords[3]))/2);		
	}

	private static double triangle_perimeter(int[] coords) {
		return dist(coords[0], coords[1], coords[2], coords[3]) +
				dist(coords[2], coords[3], coords[4], coords[5]) + 
				dist(coords[4], coords[5], coords[0], coords[1]);		
	}
	
	private static double dist(int x1, int y1, int x2, int y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String CurLine = ""; // Line read from standard in
		
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		double smallP = Double.MAX_VALUE, largeP = Double.MIN_VALUE;
		double smallA = Double.MAX_VALUE, largeA = Double.MIN_VALUE;
		int count = 0;
		Scanner s = new Scanner(System.in);
		while (true){
			System.out.print("Enter the shape details (type 'end' to exit): ");
			String shape = s.next();
			if(shape.equals("end")){
				break;			
			}
			count += 1;
			double area = 0.0, perimeter = 0.0;
			if(shape.equals("triangle")){
				int []coords = new int[6];
				for(int i = 0; i < 6; i++){
					coords[i] = s.nextInt();
				}
				area = triangle_area(coords);
				perimeter = triangle_perimeter(coords);
			}else if(shape.equals("rectangle")){
				int []coords = new int[4];
				for(int i = 0; i < 4; i++){
					coords[i] = s.nextInt();
				}
				area = rectangle_area(coords);
				perimeter = rectangle_perimeter(coords);
			}else if(shape.equals("circle")){
				int []coords = new int[2];
				for(int i = 0; i < 2; i++){
					coords[i] = s.nextInt();
				}
				double radius = s.nextDouble();
				area = circle_area(coords, radius);
				perimeter = circle_perimeter(coords, radius);			
			}else{
				System.out.println(shape+" is not a valid shape, unable to compute area/perimeter");
				continue;
			}
			// Update largest and smallest values
			if(area > largeA) { largeA = area; }
			if(area < smallA) { smallA = area; }
			if(perimeter > largeP) { largeP = perimeter; }
			if(perimeter < smallP) { smallP = perimeter; }
			
			System.out.format("Shape = %s, Area = %.4f, Perimeter = %.4f\n", shape, area, perimeter);			
		}
		if(count > 0){
			System.out.format("The smallest perimeter is %.4f\n", smallP);
			System.out.format("The smallest area is %.4f\n", smallA);
			System.out.format("The largest perimeter is %.4f\n", largeP);
			System.out.format("The largest area is %.4f\n", largeA);
		}
		System.out.println("Done");
	}
}