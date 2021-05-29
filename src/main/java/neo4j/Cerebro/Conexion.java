package neo4j.Cerebro;

import java.util.List;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

public class Conexion {
	private static Driver driver;
	private static String usuario;
	private static String uri;

	public Conexion(String uri, String user, String password) {
		Conexion.uri = uri;
		usuario = user;
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}

	public boolean comprobarCredentials() {
		try (Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro"))) {
			return session.writeTransaction(new TransactionWork<Boolean>() {
				@Override
				public Boolean execute(Transaction tx) {
					tx.run("MATCH (n) RETURN n LIMIT 1");
					return true;
				}
			});
		} catch (Exception e) {
			try (Session session = Conexion.getDriver().session(SessionConfig.forDatabase("neo4j"))) {
				return session.writeTransaction(new TransactionWork<Boolean>() {
					@Override
					public Boolean execute(Transaction tx) {
						tx.run("MATCH (n) RETURN n LIMIT 1");
						return true;
					}
				});
			} catch (Exception e1) {
				return false;
			}
		}
	}

	public static boolean createUserRole(String user, String password, List<String> roles) {
		try (Session session = Conexion.getDriver().session(SessionConfig.forDatabase("system"))) {
			return session.writeTransaction(new TransactionWork<Boolean>() {
				@Override
				public Boolean execute(Transaction tx) {
					tx.run("CREATE USER " + user + " SET PASSWORD '" + password + "'CHANGE NOT REQUIRED SET STATUS ACTIVE");
					for (String role : roles) {
						tx.run("GRANT ROLE " + role +" TO " + user + "");
					}
					return true;
				}
			});
		} catch (Exception e) {
			driver = GraphDatabase.driver(uri, AuthTokens.basic("admin", "123"));
			try (Session session = Conexion.getDriver().session(SessionConfig.forDatabase("system"))) {
				return session.writeTransaction(new TransactionWork<Boolean>() {
					@Override
					public Boolean execute(Transaction tx) {
						tx.run("CREATE USER " + user + " SET PASSWORD '" + password + "'CHANGE NOT REQUIRED SET STATUS ACTIVE");
						for (String role : roles) {
							tx.run("GRANT ROLE " + role +" TO " + user + "");
						}
						return true;
					}
				});
			} catch (Exception e1) {
				return false;
			}
		}
	}

	public static Driver getDriver() {
		return driver;
	}

	public void close() throws Exception {
		driver.close();
	}

	public static String getUsuario() {
		return usuario;
	}
}
