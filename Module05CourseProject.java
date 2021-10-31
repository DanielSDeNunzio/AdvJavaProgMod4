package Module05CourseProject;
import java.io.*;
import java.util.*;

interface patient {
	public float calculateBmi(float height, float weight);
}
public class Module05CourseProject implements patient{
	public float calculateBmi(float height, float weight)
	{
		return (weight / (height * height)) * 703;
	}
	public static void main(String[] args) {
		Scanner scanner = null;
		try (BufferedWriter bWriter = new BufferedWriter(new FileWriter("C:\\Users\\ender\\OneDrive\\Desktop\\Module05CourseProject\\patients.txt")) ){
			List<List<String>> outputValues = new ArrayList<>();
			scanner = new Scanner(System.in);
			while(true) {
				List<String> outputValue = new ArrayList<>();
				System.out.println("Please enter your patients name: ");
				outputValue.add(scanner.nextLine());
				
				System.out.println("Please enter your patients birthday: ");
				outputValue.add(scanner.nextLine());
				
				System.out.println("Please enter the patients weight in pounds: ");
				outputValue.add(scanner.nextLine());
				float weight = Integer.parseInt(outputValue.get(2));
				
				System.out.println("Please enter the patients height in inches: ");
				outputValue.add(scanner.nextLine());
				float height = Integer.parseInt(outputValue.get(3));
				
				Module05CourseProject trial = new Module05CourseProject();
				
				float bmi = trial.calculateBmi(height, weight);
				
				outputValue.add(String.valueOf(bmi));
				
				if (bmi < 18.5){
					outputValue.add("underweight");
					outputValue.add("Low");
					outputValue.add("Increase food consumption");
				}
				else if (18.5 <= bmi && bmi < 25) {
					outputValue.add("normal weight");
					outputValue.add("Low");
					outputValue.add("Healthy! Keep up the good work!");
				}
				else if (25 <= bmi && bmi <30) {
					outputValue.add("overweight");
					outputValue.add("High");
					outputValue.add("Slightly high, please lower food consumption");
				}
				else {
					outputValue.add("Obese");
					outputValue.add("Highest");
					outputValue.add("Please lower food consumption, danger to your heath");
				}
				
				outputValues.add(outputValue);
				
				System.out.println("Press any key to continue or 'q' to quit");
				if(scanner.nextLine().equals("q"))
				{
					break;
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append("Name");
			sb.append(",");
			sb.append("Birthday");
			sb.append(",");
			sb.append("Weight");
			sb.append(",");
			sb.append("height");
			sb.append(",");
			sb.append("BMI");
			sb.append(",");
			sb.append("Weight Category");
			sb.append(",");
			sb.append("Insurance Payment Category");
			sb.append(System.lineSeparator());
			
			bWriter.write(sb.toString());
			
			for(int i = 0; i < outputValues.size(); i++)
			{
				sb.delete(0, sb.length());
				
				sb.append(outputValues.get(i).get(0));
				sb.append(",");
				sb.append(outputValues.get(i).get(1));
				sb.append(",");
				sb.append(outputValues.get(i).get(2));
				sb.append(",");
				sb.append(outputValues.get(i).get(3));
				sb.append(",");
				sb.append(outputValues.get(i).get(4));
				sb.append(",");
				sb.append(outputValues.get(i).get(5));
				sb.append(",");
				sb.append(outputValues.get(i).get(6));
				sb.append(System.lineSeparator());
				
				bWriter.write(sb.toString());
			}
		}
		catch(Exception e) {
			System.out.println("An error has occured!");
			return;
		}
		finally {
			scanner.close();
		}
	}
}
