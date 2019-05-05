package test;

import static org.junit.Assert.assertTrue;

import java.util.List;
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import test.util.TestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdiActividad2202206 {

	private TestUtil testUtil = new TestUtil(driver);

	final static String PathFirefox64 = "../FirefoxPortable/FirefoxPortable.exe";
	final static String Geckdriver022 = "geckodriver024win64.exe";

	static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
	static String URL = "http://localhost:8081";

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
		TestUtil testUtil = new TestUtil(driver);
		driver.get(URL + "/identificarse");
		testUtil.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.className("btn-primary")).click();
		testUtil.waitChangeWeb();
		driver.findElement(By.linkText("reset DB")).click();
		testUtil.waitChangeWeb();
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
		driver.findElement(By.linkText("Registrate")).click();
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
 		testUtil.searchText("Identificación de usuario", true);
	}

	/*
	 * Registro con email vacio
	 */
	@Test
	public void T01_registroEmailVacio() {
		driver.get("http://localhost:8081/");
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
		testUtil.waitChangeWeb();
		// nos aseguramos que sigue en la misma pagina por datos incorrectos
		testUtil.searchText("Identificación de usuario", false);
		testUtil.searchText("Registrate", true);

	}

	/*
	 * registro contraseñas no coinciden
	 */
	@Test
	public void T02_registroContraseñasNoCoinciden() {
		driver.get("http://localhost:8081/");
 		driver.findElement(By.linkText("Registrate")).click();
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
		testUtil.waitChangeWeb();
		// nos aseguramos que sigue en la misma pagina por datos incorrectos
		testUtil.searchText("Identificacion de usuario", false);
		testUtil.searchText("Registrate", true);

	}

	/*
	 * Registro email ya existe
	 */
	@Test
	public void T03_registroEmailExistente() {
		driver.get("http://localhost:8081/");
 		driver.findElement(By.linkText("Registrate")).click();
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
	public void T04_IdentificarseValido() {
		driver.get("http://localhost:8081/identificarse");
 		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");
		driver.findElement(By.className("btn-primary")).click();
 		testUtil.searchText("Bienvenidos", true);
		testUtil.searchText("autenticado", true);
		testUtil.searchText("user6", true);
	}

	// Inicio de sesión con datos válidos (usuario estándar, email existente,
	// pero contraseñ incorrecta).
	@Test
	public void T05_IdentificarseContraseñaIncorrecta() {
		driver.get("http://localhost:8081/identificarse");
 		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("test6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("error");
		driver.findElement(By.className("btn-primary")).click();
 		testUtil.searchText("Bienvenidos", false);
		testUtil.searchText("autenticado", false);

	}

	// Inicio de sesión con datos inválidos (usuario estándar, campo email y
	// contraseña vacíos).
	@Test
	public void T06_IdentificarseEmailPasswordVacios() {

		driver.get("http://localhost:8081/identificarse");
		testUtil.waitChangeWeb();
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
	public void T07_IdentificarseEmailNoExiste() {
		driver.get("http://localhost:8081/identificarse");
		testUtil.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("err@email.com");
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
	public void T08_IdentificarseSalirSesion() {
		driver.get("http://localhost:8081/identificarse");
		testUtil.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("test6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("12345");
		driver.findElement(By.className("btn-primary")).click();
		testUtil.waitChangeWeb();
		driver.get("http://localhost:8081/desconectarse");
		testUtil.waitChangeWeb();
		testUtil.searchText("Identificación de usuario", true);

	}

	// Comprobar que el botón cerrar sesión no está visible si el usuario no
	// está autenticado.
	@Test
	public void T09_CerrarSesionNoVisibleNoAutenticado() {
		driver.get("http://localhost:8081/");
		testUtil.searchText("Desconectar", false);
	}

	// Mostrar el listado de usuarios y comprobar que se muestran todos los que
	// existen en el
	@Test
	public void T10_ListadoDeUsuarios() {
		driver.get("http://localhost:8081/");
 		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("send")).click();
 		driver.findElement(By.linkText("Ver Usuarios")).click();
		testUtil.waitChangeWeb();
		// comprobamos que no se liste el administrador y que se listen usuarios que
		// estan en la base de datos
		testUtil.searchText("Usuarios", true);
		testUtil.searchText("admin@email.com", false);
		testUtil.searchText("user6@email.com", true);
		testUtil.searchText("user1@email.com", true);

	}

	// Admin.Ir a la lista de usuarios, borrar el primer usuario de la lista,
	// comprobar que la lista se actualiza y dicho usuario desaparece.
	@Test
	public void T11_BorrarPrimerUsuario() {
		driver.get("http://localhost:8081/identificarse");
		testUtil.waitChangeWeb();

		driver.findElement(By.linkText("Registrate")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Ana");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Alvarez");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("aaaaaaaaaa@email.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("123456");
		driver.findElement(By.id("send")).click();

		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("send")).click();
		driver.get("http://localhost:8081/user/list");
		testUtil.searchText("aaaaaaaaaa@email.com", true);
		driver.findElements(By.className("check")).get(0).click();
		driver.findElement(By.id("DeleteButton")).click();
		testUtil.waitChangeWeb();

		testUtil.searchText("Usuarios", true);
		testUtil.searchText("admin@email.com", false);
		testUtil.searchText("aaaaaaaaaa@email.com", false);

	}

	// Admin.Ir a la lista de usuarios, borrar el último usuario de la lista,
	// comprobar que la lista se actualizay dicho usuario desaparece.
	@Test
	public void T12_BorrarUltimoUsuario() {
 
		driver.get("http://localhost:8081/registrarse");
		  
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Pepe");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Martinez");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("zzzzEmailBorrarUltimoUsuario@email.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("123456");
		driver.findElement(By.id("send")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("send")).click();
		driver.findElement(By.id("usersee")).click();
		testUtil.waitChangeWeb();
		testUtil.searchText("zzzzEmailBorrarUltimoUsuario@email.com", true);
		List<WebElement> usuarios = driver.findElements(By.className("check"));
		usuarios.get(usuarios.size() - 1).click();
		driver.findElement(By.id("DeleteButton")).click();
		testUtil.searchText("Usuarios", true);
		testUtil.searchText("admin@email.com", false);
		testUtil.searchText("zzzzEmailBorrarUltimoUsuario@email.com", false);

	}

	// Admin.Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la
	// lista se actualiza y dichosusuarios desaparecen.
	@Test
	public void T13_BorrarTresUsuarios() {
		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.linkText("Registrate")).click();
		testUtil.waitChangeWeb();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Paco");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Pelaez");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("aaBorrarPrimerUsuario@email.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("123456");
		driver.findElement(By.id("send")).click();
		driver.findElement(By.linkText("Registrate")).click();
		testUtil.waitChangeWeb();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Pepe");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Diez");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("aaSegundoUsuarioBorrar@email.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("123456");
		driver.findElement(By.id("send")).click();
		driver.findElement(By.linkText("Registrate")).click();
		testUtil.waitChangeWeb();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Andres");
		driver.findElement(By.id("surname")).click();
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Jimenez");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("aaTercerUsuarioBorrar@email.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("password2")).click();
		driver.findElement(By.id("password2")).clear();
		driver.findElement(By.id("password2")).sendKeys("123456");
		driver.findElement(By.id("send")).click();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("admin@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.id("send")).click();
		driver.get("http://localhost:8081/user/list");
		testUtil.waitChangeWeb();
		testUtil.searchText("aaBorrarPrimerUsuario@email.com", true);
		testUtil.searchText("aaSegundoUsuarioBorrar@email.com", true);
		testUtil.searchText("aaTercerUsuarioBorrar@email.com", true);
		driver.findElements(By.className("check")).get(0).click();
		driver.findElements(By.className("check")).get(1).click();
		driver.findElements(By.className("check")).get(2).click();
		driver.findElement(By.id("DeleteButton")).click();
		testUtil.waitChangeWeb();

		testUtil.searchText("Usuarios", true);
		testUtil.searchText("admin@email.com", false);
		testUtil.searchText("aaBorrarPrimerUsuario@email.com", false);
		testUtil.searchText("aaSegundoUsuarioBorrar@email.com", false);
		testUtil.searchText("aaTercerUsuarioBorrar@email.com", false);

	}

	// Ir al formulario de alta de oferta, rellenarla con datos válidos y pulsar
	// el botón Submit.
	// Comprobar que la oferta sale en el listado de ofertas de dicho usuario
	@Test
	public void T14_DarAltaOfertaValido() {
		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");

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
	public void T15_CrearOfertaTituloVacio() {
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");

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
	public void T16_MostrarOfertas() {
		// login user
		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");

		driver.findElement(By.className("btn-primary")).click();
		// adding offer
		driver.get("http://localhost:8081/offer/selling");

		testUtil.searchText("Diamantes", true);
	}

	// User. Dar de baja una oferta
	// Ir a la lista de ofertas, borrar la primera oferta de la lista, comprobar
	// que la lista se actualiza y que la oferta desaparece.
	@Test
	public void T17_EliminarPrimeraOferta() {
		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user1@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user1");
		driver.findElement(By.id("send")).click();

		driver.findElement(By.id("offersmanage")).click();
		driver.findElement(By.id("offeradd")).click();

		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Eliminar");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Eliminaro");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("2");
		driver.findElement(By.id("add")).click();
		testUtil.waitChangeWeb();

		driver.findElement(By.id("offersmanage")).click();
		driver.findElement(By.id("offerselling")).click();
		testUtil.waitChangeWeb();

		testUtil.searchText("Eliminaro", true);
		List<WebElement> elements = driver.findElements(By.className("eliminar"));
		int size = elements.size();
		driver.findElements(By.className("eliminar")).get(0).click();

		testUtil.waitChangeWeb();
		testUtil.searchText("Eliminaro", false);
		testUtil.searchText("Se ha eliminado correctamente la oferta", true);
		elements = driver.findElements(By.className("eliminar"));
		assertTrue(size - 1 == elements.size());

	}

	// User.Dar de baja una oferta
	// Ir a la lista de ofertas, borrar la última oferta de la lista, comprobar
	// que la lista se actualiza y que la oferta desaparece.
	@Test
	public void T18_EliminarUltimaOferta() {

		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user1@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user1");
		driver.findElement(By.id("send")).click();
 		driver.findElement(By.id("offersmanage")).click();
		driver.findElement(By.id("offeradd")).click();
 		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("zzzzUltimaOferta");
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Bonita");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("2");
		driver.findElement(By.id("add")).click();
		testUtil.waitChangeWeb();
		driver.findElement(By.id("offersmanage")).click();
		driver.findElement(By.id("offerselling")).click();
 		testUtil.searchText("zzzzUltimaOferta", true);
		List<WebElement> elements = driver.findElements(By.className("eliminar"));
		int size = elements.size();
		driver.findElements(By.className("eliminar")).get(size - 1).click();
 		testUtil.searchText("zzzzUltimaOferta", false);
		testUtil.searchText("Se ha eliminado correctamente la oferta", true);
		elements = driver.findElements(By.className("eliminar"));
		assertTrue(size - 1 == elements.size());// comprobamos que se ha eliminado un elemento->sumamos 1 al tamaño
												// actual para comprobar si coincide con el original)

	}

	// Hacer una búsqueda con el campo vacío y comprobar que se muestra la
	// página que corresponde con el listado de las ofertas existentes en el
	// sistema
	@Test
	public void T19_BusquedaOfertaVacia() {
		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user1@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user1");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("buscaA");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("Uno");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("0.1");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("BUSCAa");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("Uno");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("0.1");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/search");

		driver.findElement(By.name("busqueda")).click();
		driver.findElement(By.name("busqueda")).clear();
		driver.findElement(By.id("send")).click();

		driver.findElement(By.id("lastpage")).click();
		testUtil.waitChangeWeb();

		testUtil.searchText("BUSCAa", true);
		testUtil.searchText("buscaA", true);

	}

	// Hacer una búsqueda escribiendo en el campo un texto que no exista y
	// comprobar que se
	// muestra la página que corresponde, con la lista de ofertas vacía.
	@Test
	public void T20_BusquedaOfertaTextoNoExiste() {
		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/search");

		driver.findElement(By.name("busqueda")).click();
		driver.findElement(By.name("busqueda")).clear();
		driver.findElement(By.name("busqueda")).sendKeys("wasdfgsadgowowo");
		driver.findElement(By.id("send")).click();

		testUtil.searchText("BUSCAa", false);
	}

	// Hacer una búsqueda escribiendo en el campo un texto en minúscula o mayúscula
	// y
	// comprobar que se muestra la página que corresponde, con la lista de ofertas
	// que contengan dicho texto,
	// independientemente que el título esté almacenado en minúsculas o mayúscula.
	// // comprobar que se
	@Test
	public void T21_BusquedaOfertaMayusMinus() {
		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/search");

		driver.findElement(By.name("busqueda")).click();
		driver.findElement(By.name("busqueda")).clear();
		driver.findElement(By.name("busqueda")).sendKeys("buscaa");
		driver.findElement(By.id("send")).click();

		testUtil.searchText("BUSCAa", true);
		testUtil.searchText("buscaA", true);

	}

	@Test
	// Sobre una búsqueda determinada (a elección de desarrollador), comprar una
	// oferta que deja
	// un saldo positivo en el contador del comprobador. Y comprobar que el contador
	// se actualiza
	// correctamente en la vista del comprador
	public void T22_ComprarOfertaSaldoPositivo() {
		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user2@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user2");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("vende01");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("Uno");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("0.1");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");

		driver.findElement(By.className("btn-primary")).click();
 
		driver.get("http://localhost:8081/offer/search?busqueda=vende01");
		driver.findElements(By.className("buy")).get(0).click();

		driver.get("http://localhost:8081/offer/bought");
		testUtil.waitChangeWeb();

		testUtil.searchText("vende01", true);

	}

	// Sobre una búsqueda determinada (a elección de desarrollador), comprar una
	// oferta que deja
	// un saldo 0 en el contador del comprobador. Y comprobar que el contador se
	// actualiza correctamente en
	// la vista del comprador.
	@Test
	public void T23_ComprarOfertaSaldo0() {

		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user2@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user2");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("vendejustisimo");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("justo ");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("100");
		driver.findElement(By.className("btn")).click();

		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user1@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user1");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/search?busqueda=vendejustisimo");

		driver.findElements(By.className("buy")).get(0).click();
		testUtil.waitChangeWeb();

		
		driver.get("http://localhost:8081/offer/bought");
		testUtil.searchText("vendejustisimo", true); 
		driver.get("http://localhost:8081/home");
		testUtil.searchText("0", true);

	}

	// Sobre una búsqueda determinada (a elección de desarrollador), intentar
	// comprar una oferta
	// que esté por encima de saldo disponible del comprador. Y comprobar que se
	// muestra el mensaje de
	// saldo no suficiente.
	@Test
	public void T24_ComprarOfertaSaldoInsuficiente() {
		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user2@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user2");

		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("vende1199");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("Caro ");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("1199");
		driver.findElement(By.className("btn")).click();

		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/search?busqueda=vende1199");

		driver.findElements(By.className("buy")).get(0).click();

		testUtil.waitChangeWeb();
		testUtil.searchText("Sin sueldo suficiente", true);
	}

	// Ir a la opción de ofertas compradas del usuario y mostrar la lista. Comprobar
	// que aparecen
	// las ofertas que deben aparecer.
	@Test
	public void T25_MostrarOfertas() {
		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/selling");
		List<WebElement> elements = driver.findElements(By.className("sell"));
		int size = elements.size();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Vendo 1");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("Vendo 111");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("1.11");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/selling");
		int nelements = driver.findElements(By.className("sell")).size();
		assertTrue(size == nelements - 1);
		testUtil.searchText("Vendo 1", true);

	}

	// Al crear una oferta marcar dicha oferta como destacada y a continuación
	// comprobar:
	// que aparece en el listado de ofertas destacadas para los usuarios y que el
	// saldo del usuario se actualiza adecuadamente en la vista del ofertante
	// (-20)..
	@Test
	public void T26_DestacarDesdeCreacion() {
		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user2@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user2");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("destacada");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("fav ");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("1");
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.className("btn")).click();
		driver.get("http://localhost:8081/home");

		testUtil.searchText("100", false);
		testUtil.searchText("80", true);

		driver.get("http://localhost:8081/identificarse");

		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user6@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user6");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/fav");
		testUtil.searchText("destacada", true);

	}

	// Sobre el listado de ofertas de un usuario con mas de 20 euros de saldo,
	// pinchar en el enlace Destacada y a continuación comprobar:
	// i) que aparece en el listado de ofertas destacadas para los usuarios
	// y que el saldo del usuario se actualiza adecuadamente en la vista del
	// ofertante (-20). @Test
	@Test
	public void T27_DestacarOfertasDesdeEnlace() {
		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user3@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user3");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("a destacar");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("faverror ");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("1");
		driver.findElement(By.className("btn")).click();

		driver.get("http://localhost:8081/offer/selling");
		driver.findElements(By.className("destacar")).get(0).click();

		driver.get("http://localhost:8081/home");
		testUtil.searchText("100", false);
		testUtil.searchText("80", true);

		driver.get("http://localhost:8081/identificarse");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user1@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user1");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/fav");
		testUtil.searchText("a destacar", true);

	}

	// Teniendo menos de 20 euros de saldo no dejar destacar
	@Test
	public void T28_NoDejaDestacarSiSaldoInsuficiente() {
		driver.get("http://localhost:8081");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("user1@email.com");
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("user1");
		driver.findElement(By.className("btn-primary")).click();

		driver.get("http://localhost:8081/offer/add");
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("destacadajusto");
		driver.findElement(By.name("description")).click();
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("faverror ");
		driver.findElement(By.name("price")).click();
		driver.findElement(By.name("price")).clear();
		driver.findElement(By.name("price")).sendKeys("1");
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.className("btn")).click();

		testUtil.searchText("Sin sueldo suficiente para destacar oferta", true);

	}

	// Jquery: Inicio de sesión con datos válidos.
	@Test
	public void T29_InicioSesinDatosValidos() {
		driver.get("http://localhost:8081/cliente.html");
		testUtil.waitChangeWeb();

		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("user6@email.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("user6");
		driver.findElement(By.id("boton-login")).click();
		testUtil.waitChangeWeb();

		testUtil.searchText("Nombre", true);
		testUtil.searchText("Descripcion", true);
		testUtil.searchText("Precio", true);
		testUtil.searchText("Password", false);

	}

	// Jquery:] Inicio de sesión con datos inválidos (email existente, pero
	// contraseña incorrecta).

	@Test
	public void T30_InicioSesionContraseñaIncorrecta() {
		driver.get("http://localhost:8081/cliente.html");
		testUtil.waitChangeWeb();

		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("user6@email.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("qewrewq");
		driver.findElement(By.id("boton-login")).click();
		testUtil.waitChangeWeb();

		testUtil.searchText("Usuario no encontrado", true);

	}

	// Inicio de sesión con datos válidos (campo email o contraseña vacíos)
	@Test
	public void Test31() {
		driver.get("http://localhost:8081/cliente.html");
		testUtil.waitChangeWeb();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(" ");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("qewrewq");
		driver.findElement(By.id("boton-login")).click();
		testUtil.searchText("Usuario no encontrado", true);

	}

	// Mostrar el listado de ofertas disponibles y comprobar que se muestran todas
	// las que existen,
	// menos las del usuario identificado
	@Test
	public void T32_MostrarOfertas() {
		driver.get("http://localhost:8081/cliente.html");
		testUtil.waitChangeWeb();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("user6@email.com");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("user6");
		driver.findElement(By.id("boton-login")).click();
		testUtil.waitChangeWeb();
		testUtil.searchText("Nombre", true);
		testUtil.searchText("Vendo 111", false);
	}
	
	
	//Enviar y visualizar mensajes a una oferta.
	//Sobre una búsqueda determinada de ofertas enviar un mensaje a una oferta. 
	//Se abrirá dicha conversación por primera vez.
	//Comprobar que el mensaje aparece en el listado de conversaciones
	@Test
	public void T33_EnviarMensajePrimeraVez() {
		driver.get("http://localhost:8081/cliente.html?w=login");
		testUtil.waitChangeWeb();
	    driver.findElement(By.id("email")).click();
	    driver.findElement(By.id("email")).clear();
	    driver.findElement(By.id("email")).sendKeys("user6@email.com");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("user6");
	    driver.findElement(By.id("boton-login")).click();
	    testUtil.waitChangeWeb();
	    //vamos a probar mandar un mensaje a la primera oferta que aparece
	    driver.findElements(By.id("sendMessage")).get(0).click();
	    testUtil.waitChangeWeb();
	    driver.findElement(By.id("inputMessage")).click();
	    driver.findElement(By.id("inputMessage")).clear();
	    driver.findElement(By.id("inputMessage")).sendKeys("Hola");
	    driver.findElement(By.id("submitMessage")).click();
	    testUtil.waitChangeWeb();
	    testUtil.waitChangeWeb();
	  //debería de aparecer mensaje que acabamos de mandar y la hora de envío
	    testUtil.searchText("Hola", true);
	    //debería aparecer mensaje automático
	    testUtil.searchText("Hola! Me interesa su oferta", true);	    
	    //debería de aparecer el email del primer usuario user1@email.com
	    testUtil.searchText("user1@email.com", true);
	    driver.findElement(By.id("offersmanage")).click();
	    driver.findElement(By.id("conver")).click();
	    testUtil.waitChangeWeb();
	    testUtil.waitChangeWeb();
	    testUtil.searchText("user1@email.com", true);
	    
		
	}
	
	//Sobre el listado de conversaciones enviar mensaje a conversación ya abierta
	//Comprobamos que el mensaje aparece en listado de mensajes
	@Test
	public void T34_EnviarMensajeConverAbierta() {
		driver.get("http://localhost:8081/cliente.html?w=login");
		testUtil.waitChangeWeb();
	    driver.findElement(By.id("email")).click();
	    driver.findElement(By.id("email")).clear();
	    driver.findElement(By.id("email")).sendKeys("user6@email.com");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("user6");
	    driver.findElement(By.id("boton-login")).click();
	    testUtil.waitChangeWeb();
	    driver.findElements(By.id("sendMessage")).get(0).click();
	    driver.findElement(By.id("inputMessage")).click();
	    driver.findElement(By.id("inputMessage")).clear();
	    //abrimos una conversación y mandamos mensaje	   
	    driver.findElement(By.id("inputMessage")).sendKeys("Buenas!");
	    testUtil.waitChangeWeb();
	    driver.findElement(By.id("submitMessage")).click();
	    testUtil.waitChangeWeb();
	    driver.findElement(By.id("offersmanage")).click();
	    driver.findElement(By.id("conver")).click();
	    testUtil.waitChangeWeb();
	    //vamos a las conversaciones, 
	    //intentamos mandar un mensaje en esa conversacion y comprobamos que esta el mensaje anterior
	    testUtil.searchText("user1@email.com", true);
	    driver.findElement(By.linkText("Enviar mensaje")).click();
	    testUtil.waitChangeWeb();
	    testUtil.waitChangeWeb();
	    testUtil.searchText("Hola! Me interesa su oferta", true);
	    testUtil.searchText("Buenas!", true);
	    
	    
	}
	
	//Mostrar listado de conversaciones abiertas y comprobar que son las que tienen que ser
	@Test
	public void T35_MostrarConversaciones() {
		driver.get("http://localhost:8081/cliente.html?w=login");
		testUtil.waitChangeWeb();
	    driver.findElement(By.id("email")).click();
	    driver.findElement(By.id("email")).clear();
	    driver.findElement(By.id("email")).sendKeys("user6@email.com");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("user6");
	    driver.findElement(By.id("boton-login")).click();
	    testUtil.waitChangeWeb();
	    //mandamos mensaje a la oferta1 del primer usuario1
	    driver.findElements(By.id("sendMessage")).get(0).click();
	    driver.findElement(By.id("inputMessage")).click();
	    driver.findElement(By.id("inputMessage")).clear();
	    driver.findElement(By.id("inputMessage")).sendKeys("Hola!!");
	    driver.findElement(By.id("submitMessage")).click();
	    testUtil.waitChangeWeb();
	    driver.findElement(By.id("offersmanage")).click();
	    driver.findElement(By.id("ofertasv")).click();
	    //mandamos mensaje a la oferta1 del usuario2
	    testUtil.waitChangeWeb();
	    driver.findElements(By.id("sendMessage")).get(5).click();
	    driver.findElement(By.id("inputMessage")).click();
	    driver.findElement(By.id("inputMessage")).clear();
	    driver.findElement(By.id("inputMessage")).sendKeys("Buenas");
	    driver.findElement(By.id("submitMessage")).click();
	    testUtil.waitChangeWeb();
	    driver.findElement(By.id("offersmanage")).click();
	    driver.findElement(By.id("conver")).click();
	    testUtil.waitChangeWeb();
	    driver.findElements(By.id("sendMessageConver")).get(0).click();
	    //comprobar que esta el mensaje que mandamos al primer usuario
	    testUtil.waitChangeWeb();
	    testUtil.searchText("user1@email.com", true);
	    testUtil.searchText("Hola!!", true);
	    driver.findElement(By.id("offersmanage")).click();
	    driver.findElement(By.id("conver")).click();
	    testUtil.waitChangeWeb();
	    driver.findElements(By.id("sendMessageConver")).get(1).click();
	    //comprobar que esta el mensaje que mandamos al segundo usario
	    testUtil.waitChangeWeb();
	    testUtil.searchText("user2@email.com", true);
	    testUtil.searchText("Buenas", true);
	    
	}

}
