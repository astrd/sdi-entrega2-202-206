package test;

import java.util.Random;

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

	//
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

	protected String getRandomEmail() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 10) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	@Test
	public void T01_registroDatosValidos() {
		driver.get("http://localhost:8081/");
		testUtil.waitChangeWeb();
		driver.findElement(By.linkText("registrate")).click();
		testUtil.waitChangeWeb();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Elena");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Diaz");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(getRandomEmail() + "@gmail.com");
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
		testUtil.waitChangeWeb();
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
		testUtil.searchText("Identificación de usuario", false);
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
		testUtil.waitChangeWeb();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Julio");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Perez");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(getRandomEmail() + "@gmail.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("1234533333678");
		driver.findElement(By.id("send")).click();
		// nos aseguramos que sigue en la misma pagina por datos incorrectos
		testUtil.searchText("Identificacion de usuario", false);
		testUtil.searchText("Registrate", true);

	}

	/*
	 * Registro email ya existe
	 */
	@Test
	public void T04_registroEmailExistente() {
		driver.get("http://localhost:8081/");
		testUtil.waitChangeWeb();
		driver.findElement(By.linkText("Registrate")).click();
		testUtil.waitChangeWeb();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Ana");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Alvarez");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("pruebaEmailExistente@prueba.com");
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

	// Inicio de sesión con datos válidos (usuario ).
	@Test
	public void Prueba04() {
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba6@prueba6.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("12345");

		driver.findElement(By.className("btn-primary")).click();
		testUtil.waitChangeWeb();
		testUtil.searchText("Bienvenidos", true);
		testUtil.searchText("autenticado", true);
		testUtil.searchText("prueba6@prueba6.com", true);
	}

	// Inicio de sesión con datos válidos (usuario estándar, email existente,
	// pero contraseñ incorrecta).
	@Test
	public void Prueba05() {
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba6@prueba6.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("error");

		driver.findElement(By.className("btn-primary")).click();
		testUtil.waitChangeWeb();
		testUtil.searchText("Bienvenidos", false);
		testUtil.searchText("autenticado", false);

	}

	// Inicio de sesión con datos inválidos (usuario estándar, campo email y
	// contraseña vacíos).
	@Test
	public void Prueba06() {

		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(" ");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(" ");

		driver.findElement(By.className("btn-primary")).click();
		testUtil.waitChangeWeb();
		testUtil.searchText("Bienvenidos", false);
		testUtil.searchText("autenticado", false);

	}

	// Inicio de sesión con datos inválidos (usuario estándar, email no
	// existente en la aplicación).
	@Test
	public void Prueba07() {
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("err@prueba6.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("error");

		driver.findElement(By.className("btn-primary")).click();
		testUtil.waitChangeWeb();
		testUtil.searchText("Bienvenidos", false);
		testUtil.searchText("autenticado", false);
	}

	// Hacer click en la opción de salir de sesión y comprobar que se redirige a
	// la página de inicio de sesión (Login).
	@Test
	public void Prueba08() {
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba6@prueba6.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("12345");

		driver.findElement(By.className("btn-primary")).click();
		driver.findElement(By.linkText("Identifícate")).click();

		testUtil.waitChangeWeb();
		testUtil.searchText("Identificación de usuario", true);
		testUtil.searchText("email", true);

	}

	// Comprobar que el botón cerrar sesión no está visible si el usuario no
	// está autenticado.
	@Test
	public void Prueba09() {
		driver.get("http://localhost:8081/");
		testUtil.searchText("Desconectar", false);
	}

	// Mostrar el listado de usuarios y comprobar que se muestran todos los que
	// existen en el
	@Test
	public void Prueba10() {
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.className("btn-primary")).click();
		driver.findElement(By.id("usersee")).click();
		testUtil.searchText("prueba6", true);
		testUtil.searchText("prueba1", true);
		testUtil.searchText("prueba2", true);

	}

	// Admin.Ir a la lista de usuarios, borrar el primer usuario de la lista,
	// comprobar que la lista se actualizay dicho usuario desaparece.
	// @Test
	public void Prueba11() {

	}

	// Admin.Ir a la lista de usuarios, borrar el último usuario de la lista,
	// comprobar que la lista se actualizay dicho usuario desaparece.
	@Test
	public void Prueba12() {

	}

	// Admin.Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la
	// lista se actualiza y dichosusuarios desaparecen.
	@Test
	public void Prueba13() {

	}

	// Ir al formulario de alta de oferta, rellenarla con datos válidos y pulsar
	// el botón Submit.
	// Comprobar que la oferta sale en el listado de ofertas de dicho usuario
	@Test
	public void Prueba14() {
		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba6@prueba6.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("12345");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");

		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Diamantes");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("25 Karats");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("25");

		driver.findElement(By.id("add")).click();
		driver.get("http://localhost:8081/offer/selling");
		testUtil.searchText("Diamantes", true);
		testUtil.searchText("25", true);
	}

	// Ir al formulario de alta de oferta, rellenarla con datos inválidos (campo
	// título vacío) y pulsarel botón Submit. Comprobar que se muestra el
	// mensaje de campo obligatorio.
	@Test
	public void Prueba15() {
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba6@prueba6.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("12345");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");

		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys(" ");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys(" ");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("25");

		driver.findElement(By.id("add")).click();

		testUtil.searchText("Agregar", true);

	}

	// Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran
	// todas los que
	@Test
	public void Prueba16() {
		// login user
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("prueba6@prueba6.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("12345");

		driver.findElement(By.className("btn-primary")).click();
		// adding offer
		driver.get("http://localhost:8081/offer/selling");

		testUtil.searchText("Diamantes", true);
	}

}
