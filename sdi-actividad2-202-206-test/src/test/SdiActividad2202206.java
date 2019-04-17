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

	private TestUtil testUtil = new TestUtil(driver);

	// En Windows (Debe ser la versiÃ³n 65.0.1 y desactivar las actualizacioens
	// automÃ¡ticas)):
	final static String PathFirefox64 = "../FirefoxPortable/FirefoxPortable.exe";
	// helen
	final static String Geckdriver022 = "geckodriver024win64.exe";

	// astrid
//	static String Geckdriver022 = "C:\\Users\\astri\\Downloads\\"
//			+ "PL-SDI-SesiÃ³n5-material\\PL-SDI-SesioÌ�n5-material\\"
//			+ "geckodriver024win64.exe";

	static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
	String URL = "http://localhost:8081";

	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// DespuÃ©s de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
	}

	// Al finalizar la Ãºltima prueba
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
	 * Registro de Usuario con datos vÃ¡lidos.
	 */
	@Test
	public void T01_registroDatosValidos() {
		driver.get("http://localhost:8081/");
		testUtil.waitChangeWeb();
		driver.findElement(By.linkText("Registrate")).click();
		testUtil.waitChangeWeb();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Elena");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Diaz");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("prueba6@prueba6.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("12345");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("12345");
		driver.findElement(By.id("send")).click();
		testUtil.waitChangeWeb();
		// nos aseguramos que se registra correctamente y nos redirige a
		// indentificar usuario
		testUtil.searchText("Identificación de usuario", true);
	}

	/*
	 * Registro con email vacio
	 */
	@Test
	public void T02_registroEmailVacio() {
		driver.get("http://localhost:8081/");
		testUtil.waitChangeWeb();
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Peter");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Parker");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("123456");
		driver.findElement(By.id("send")).click();
		// nos aseguramos que sigue en la misma pagina por datos incorrectos
		testUtil.searchText("Identificacion de usuario", false);
		testUtil.searchText("Registrate", true);

	}

	/*
	 * registro contraseñas no coinciden
	 */
	@Test
	public void T03_registroContraseñasNoCoinciden() {
		driver.get("http://localhost:8081/");
		testUtil.waitChangeWeb();
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Julio");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Perez");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("prueba2@prueba2.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("12345678");
		driver.findElement(By.id("send")).click();
		// nos aseguramos que sigue en la misma pagina por datos incorrectos
		testUtil.searchText("Identificacion de usuario", false);
		testUtil.searchText("Registrate", true);
		testUtil.searchText("Las contraseñas no coinciden", true);

	}

	/*
	 * Registro email ya existe
	 */
	@Test
	public void T04_registroEmailExistente() {
		driver.get("http://localhost:8081/");
		testUtil.waitChangeWeb();
		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Elena");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Diaz");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("prueba1@prueba1.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("123456");
		driver.findElement(By.id("send")).click();
		// nos aseguramos que sigue en la misma pagina por datos incorrectos
		testUtil.searchText("Identificacion de usuario", false);
		testUtil.searchText("Registrate", true);
		testUtil.searchText("El email ya está registrado", true);
	}
}
