package Caw_Automation;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

import com.google.common.reflect.TypeToken;

import io.github.bonigarcia.wdm.WebDriverManager;

public class jsonparse {

	public jsonparse() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException, ParseException, InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//summary[normalize-space()='Table Data']")).click();
		Thread.sleep(2000);

		JSONParser parser = new JSONParser();

		// Read JSON file

		String destinationFile = System.getProperty("user.dir") + "/Caw_assert.json";

		System.out.println(destinationFile);

		Object obj = parser.parse(new FileReader(destinationFile));

		// Check if it's an array
		JSONArray jsonArray = (JSONArray) obj;
		System.out.println(jsonArray);
		String jsonArrayString = jsonArray.toJSONString();
		driver.findElement(By.xpath("//textarea[@id='jsondata']")).clear();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//textarea[@id='jsondata']")).sendKeys(jsonArrayString);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//button[@id='refreshtable']")).click();
		Thread.sleep(3000);
		List<WebElement> gendertable = driver.findElements(By.xpath("//table[@id=\"dynamictable\"]/tr/td[1]"));
		List<WebElement> nametable = driver.findElements(By.xpath("//table[@id=\"dynamictable\"]/tr/td[2]"));
		List<WebElement> agetable = driver.findElements(By.xpath("//table[@id=\"dynamictable\"]/tr/td[3]"));

		int i = 0;
		// Process each JSON object in the array
		for (Object arrayObj : jsonArray) {
			i++;
			JSONObject jsonObject = (JSONObject) arrayObj;
			String name = (String) jsonObject.get("name");
			String gender = (String) jsonObject.get("gender");
			String age = String.valueOf(jsonObject.get("age"));
			System.out.println("Name: " + name);
			System.out.println("Age: " + age);
			System.out.println("gender" + gender);
			System.out.println("json name=" + name + "  table name=" + nametable.get(i - 1).getText().trim());
			System.out.println("json age=" + age + "  table age=" + agetable.get(i - 1).getText().trim());
			System.out.println("json gender=" + gender + " table gender=" + gendertable.get(i - 1).getText().trim());
			Assert.assertEquals(name, nametable.get(i - 1).getText().trim());
			Assert.assertEquals(age, agetable.get(i - 1).getText().trim());
			Assert.assertEquals(gender, gendertable.get(i - 1).getText().trim());
		}

	}
}
