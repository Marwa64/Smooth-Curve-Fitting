import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
	
	/*public static void PrintAnswer(String Answer, ArrayList<Pair> weights_and_values, int Case) {
		int chosenItemsCount = 0;
		int totalValuePerCase = 0;
		
		// Answer consists of string of zeros and ones
		// 0 means the item is not being selected, whereas 1 means the item is being selected
		// count the number of the chosen items as well as their values
		for (int i = 0; i < Answer.length(); i++) if (Answer.charAt(i) == '1') {
			chosenItemsCount++;
			totalValuePerCase += weights_and_values.get(i).value();
		}
		
		System.out.println("Case# " + Case + ": " +totalValuePerCase);
		System.out.println(chosenItemsCount);
		for (int i = 0; i < Answer.length(); i++) {
			if (Answer.charAt(i) == '1')
				System.out.println(weights_and_values.get(i).weight() + " " + weights_and_values.get(i).value());
		}
	}*/

	public static void main(String[] args) throws NumberFormatException, IOException {
		// Open the file
	    try {
	        FileWriter myWriter = new FileWriter("Solution.txt");
	        myWriter.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
		FileInputStream fstream = new FileInputStream("input-2.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		//Read File Line By Line
		int Case = 0;
		while ((strLine = br.readLine()) != null)   {
			int numberOfTestCases = Integer.parseInt(strLine);
			while(numberOfTestCases != 0) {
				ArrayList<Pair> points = new ArrayList<>();
				
				//read the number of items and polynomial degree
				strLine = br.readLine();
				String[] points_degree = strLine.split(" ");
				int numberOfPoints = Integer.parseInt(points_degree[0]);
				int degree =  Integer.parseInt(points_degree[1]);
				
				//get the pairs of x and y of each point
				for (int i = 0; i < numberOfPoints; i++) {
					strLine = br.readLine();
					String[] coordinates = strLine.split(" ");
					float x = Float.parseFloat(coordinates[0]);
					float y = Float.parseFloat(coordinates[1]);
					Pair pair = new Pair(x, y);
					points.add(pair);
				}
								
				CurveFitting curveFitting = new CurveFitting(points, degree, 200 * numberOfPoints, 50);
				curveFitting.findBestIndividual();
				numberOfTestCases--;
			}
		}
		//Close the input stream
		fstream.close();
	}
}