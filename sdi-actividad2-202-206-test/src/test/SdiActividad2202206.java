package test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import test.util.TestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdiActividad2202206 {

	private TestUtil testUtil;

	// En Windows (Debe ser la versión 65.0.1 y desactivar las actualizacioens
	// automáticas)):
	final static String PathFirefox64 = "C:\\Program Files\\"
			+ "Mozilla Firefox\\firefox.exe";
	// helen
	final static String Geckdriver022 = "C:\\Users\\ediaz\\"
			+ "Downloads\\PL-SDI-Sesión5-material\\"
			+ "PL-SDI-Sesión5-material\\geckodriver024win64.exe";

	// astrid
//	static String Geckdriver022 = "C:\\Users\\astri\\Downloads\\"
//			+ "PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\"
//			+ "geckodriver024win64.exe";

	static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
	String URL = "http://localhost:8081";

	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
	}

	// Al finalizar la última prueba
	// Cerramos el navegador al finalizar las pruebas
	@AfterClass
	static public void end() {
		driver.quit();
	}

	private static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	/**
	 * Registro de Usuario con datos válidos.
	 */
	@Test
	public void Test01_1_RegVal() {
		driver.get(URL);
		driver.findElement(By.linkText("Registrate")).click();
		testUtil.waitChangeWeb();
		testUtil.searchText("Registrate", true);
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("aaaa@uniovi.es");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Julio");
		driver.findElement(By.name("surname")).click();
		driver.findElement(By.name("surname")).clear();
		driver.findElement(By.name("surname")).sendKeys("Fernandez");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.name("password2")).click();
		driver.findElement(By.name("password2")).clear();
		driver.findElement(By.name("password2")).sendKeys("123456");
		driver.findElement(By.id("send")).click();
		testUtil.waitChangeWeb();
		testUtil.searchText("Registrate", false);
	}
}
